package com.amazonaws.services.lambda.runtime.tests;

import com.amazonaws.lambda.thirdparty.com.fasterxml.jackson.databind.JsonNode;
import com.amazonaws.lambda.thirdparty.com.fasterxml.jackson.databind.ObjectMapper;
import com.amazonaws.lambda.thirdparty.com.fasterxml.jackson.databind.exc.InvalidDefinitionException;
import com.amazonaws.services.lambda.runtime.serialization.PojoSerializer;
import com.amazonaws.services.lambda.runtime.serialization.factories.GsonFactory;
import com.amazonaws.services.lambda.runtime.serialization.factories.JacksonFactory;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pins the POJO serialization differences between Gson, used before RIC 2.13.0 for
 * invocations whose client context reported {@code env.platform = Android}, and Jackson,
 * used for every invocation since. Each pattern asserts the old and new JSON, and a
 * type change that makes Jackson produce the old JSON.
 *
 * <p>
 * The runtime relocates Gson and Jackson, so customer {@code @SerializedName} and
 * {@code @JsonProperty} annotations are ignored by both and are not covered here.
 * </p>
 *
 * <p>
 * Delete this class when {@code GsonFactory} is removed.
 * </p>
 */
public class GsonToJacksonPojoSerializationTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    // 1. Private fields without getters or setters.
    //    Symptom: request fields arrive as defaults and the response is {}.
    //    Fix: add getters and setters (or make the fields public).

    public static class PrivateFields {
        private String name;
    }

    public static class PrivateFieldsFixed {
        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    @Test
    void privateFields_areDroppedByJackson() {
        assertEquals("x", gson(PrivateFields.class).fromJson("{\"name\":\"x\"}").name);
        assertNull(jackson(PrivateFields.class).fromJson("{\"name\":\"x\"}").name);

        PrivateFields value = new PrivateFields();
        value.name = "x";
        assertEquals("{\"name\":\"x\"}", toJson(gson(PrivateFields.class), value));
        assertEquals("{}", toJson(jackson(PrivateFields.class), value));
    }

    @Test
    void privateFields_fix_addGettersAndSetters() {
        assertEquals("x", jackson(PrivateFieldsFixed.class).fromJson("{\"name\":\"x\"}").getName());

        PrivateFieldsFixed value = new PrivateFieldsFixed();
        value.setName("x");
        assertEquals("{\"name\":\"x\"}", toJson(jackson(PrivateFieldsFixed.class), value));
    }

    // 2. Field name differs from the getter name, such as the Android "m" prefix.
    //    Symptom: the JSON key changes from "mName" to "name".
    //    Fix: expose a public field named after the JSON key. Renaming the accessors to getMName and
    //    setMName does not work: the runtime's mapper uses USE_STD_BEAN_NAMING, which maps them to "MName".

    public static class PrefixedField {
        private String mName;

        public String getName() {
            return mName;
        }

        public void setName(String name) {
            this.mName = name;
        }
    }

    public static class PrefixedFieldFixed {
        public String mName;
    }

    @Test
    void prefixedField_keyChangesUnderJackson() {
        PrefixedField value = new PrefixedField();
        value.setName("x");
        assertEquals("{\"mName\":\"x\"}", toJson(gson(PrefixedField.class), value));
        assertEquals("{\"name\":\"x\"}", toJson(jackson(PrefixedField.class), value));

        assertNull(jackson(PrefixedField.class).fromJson("{\"mName\":\"x\"}").getName());
    }

    @Test
    void prefixedField_fix_publicFieldNamedAfterTheKey() {
        assertEquals("x", jackson(PrefixedFieldFixed.class).fromJson("{\"mName\":\"x\"}").mName);

        PrefixedFieldFixed value = new PrefixedFieldFixed();
        value.mName = "x";
        assertEquals("{\"mName\":\"x\"}", toJson(jackson(PrefixedFieldFixed.class), value));
    }

    // 3. Boolean field named isX with an isX() getter.
    //    Symptom: the JSON key changes from "isActive" to "active".
    //    Fix: name the accessors getIsActive and setIsActive.

    public static class BooleanIsField {
        private boolean isActive;

        public boolean isActive() {
            return isActive;
        }

        public void setActive(boolean active) {
            this.isActive = active;
        }
    }

    public static class BooleanIsFieldFixed {
        private boolean isActive;

        public boolean getIsActive() {
            return isActive;
        }

        public void setIsActive(boolean isActive) {
            this.isActive = isActive;
        }
    }

    @Test
    void booleanIsField_keyChangesUnderJackson() {
        BooleanIsField value = new BooleanIsField();
        value.setActive(true);
        assertEquals("{\"isActive\":true}", toJson(gson(BooleanIsField.class), value));
        assertEquals("{\"active\":true}", toJson(jackson(BooleanIsField.class), value));
    }

    @Test
    void booleanIsField_fix_getIsAccessors() {
        assertTrue(jackson(BooleanIsFieldFixed.class).fromJson("{\"isActive\":true}").getIsActive());

        BooleanIsFieldFixed value = new BooleanIsFieldFixed();
        value.setIsActive(true);
        assertEquals("{\"isActive\":true}", toJson(jackson(BooleanIsFieldFixed.class), value));
    }

    // 4. No no-argument constructor.
    //    Symptom: the invocation fails while deserializing the request.
    //    Fix: add a no-argument constructor and setters.

    public static class NoDefaultConstructor {
        private final String name;

        public NoDefaultConstructor(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    public static class NoDefaultConstructorFixed {
        private String name;

        public NoDefaultConstructorFixed() {
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    @Test
    void noDefaultConstructor_failsUnderJackson() {
        assertEquals("x", gson(NoDefaultConstructor.class).fromJson("{\"name\":\"x\"}").getName());
        UncheckedIOException e = assertThrows(UncheckedIOException.class,
                () -> jackson(NoDefaultConstructor.class).fromJson("{\"name\":\"x\"}"));
        assertInstanceOf(InvalidDefinitionException.class, e.getCause());
    }

    @Test
    void noDefaultConstructor_fix_addNoArgConstructor() {
        assertEquals("x", jackson(NoDefaultConstructorFixed.class).fromJson("{\"name\":\"x\"}").getName());
    }

    // 5. java.util.Date.
    //    Symptom: dates change from a formatted string to epoch milliseconds.
    //    Fix: use a String (formatted by the handler) or a long, so both sides agree on the format.

    public static class DateField {
        private Date createdAt;

        public Date getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(Date createdAt) {
            this.createdAt = createdAt;
        }
    }

    public static class DateFieldFixed {
        private long createdAt;

        public long getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(long createdAt) {
            this.createdAt = createdAt;
        }
    }

    @Test
    void dateField_formatChangesUnderJackson() {
        DateField value = new DateField();
        value.setCreatedAt(new Date(1577836800000L));

        // Only the JSON type is checked. Gson's text depends on the JVM locale and time zone, and
        // Jackson writes millis until any serializer from LambdaEventSerializers registers DateModule
        // on the shared mapper, after which it writes seconds.
        assertTrue(tree(toJson(gson(DateField.class), value)).get("createdAt").isTextual());
        assertTrue(tree(toJson(jackson(DateField.class), value)).get("createdAt").isNumber());
    }

    @Test
    void dateField_fix_useEpochMillis() {
        DateFieldFixed value = new DateFieldFixed();
        value.setCreatedAt(1577836800000L);
        assertEquals(toJson(gson(DateFieldFixed.class), value), toJson(jackson(DateFieldFixed.class), value));
    }

    // 6. Getter without a backing field.
    //    Symptom: the response gains a key for every computed getter.
    //    Fix: rename the method so it is not a getter.

    public static class ComputedGetter {
        public String name;

        public String getDisplayName() {
            return "Name: " + name;
        }
    }

    public static class ComputedGetterFixed {
        public String name;

        public String displayName() {
            return "Name: " + name;
        }
    }

    @Test
    void computedGetter_addsKeyUnderJackson() {
        ComputedGetter value = new ComputedGetter();
        value.name = "x";
        assertEquals("{\"name\":\"x\"}", toJson(gson(ComputedGetter.class), value));
        assertEquals("{\"name\":\"x\",\"displayName\":\"Name: x\"}", toJson(jackson(ComputedGetter.class), value));
    }

    @Test
    void computedGetter_fix_renameMethod() {
        ComputedGetterFixed value = new ComputedGetterFixed();
        value.name = "x";
        assertEquals("{\"name\":\"x\"}", toJson(jackson(ComputedGetterFixed.class), value));
    }

    // 7. Getter without a setter.
    //    Symptom: the request value is ignored and the field keeps its default.
    //    Fix: add a setter.

    public static class ReadOnlyProperty {
        private boolean locked;

        public boolean isLocked() {
            return locked;
        }
    }

    public static class ReadOnlyPropertyFixed {
        private boolean locked;

        public boolean isLocked() {
            return locked;
        }

        public void setLocked(boolean locked) {
            this.locked = locked;
        }
    }

    @Test
    void readOnlyProperty_inputIgnoredByJackson() {
        assertTrue(gson(ReadOnlyProperty.class).fromJson("{\"locked\":true}").isLocked());
        assertFalse(jackson(ReadOnlyProperty.class).fromJson("{\"locked\":true}").isLocked());
    }

    @Test
    void readOnlyProperty_fix_addSetter() {
        assertTrue(jackson(ReadOnlyPropertyFixed.class).fromJson("{\"locked\":true}").isLocked());
    }

    // 8. Transient field with a getter.
    //    Symptom: the response gains a key for the transient field, as it already did for non-Android callers.
    //    Fix: rename the accessors so they are not a bean property.

    public static class TransientField {
        public String name;
        private transient String cache;

        public String getCache() {
            return cache;
        }

        public void setCache(String cache) {
            this.cache = cache;
        }
    }

    public static class TransientFieldFixed {
        public String name;
        private transient String cache;

        public String cache() {
            return cache;
        }

        public void cache(String cache) {
            this.cache = cache;
        }
    }

    @Test
    void transientField_writtenByJackson() {
        TransientField value = new TransientField();
        value.name = "x";
        value.setCache("y");
        assertEquals("{\"name\":\"x\"}", toJson(gson(TransientField.class), value));
        assertEquals("{\"name\":\"x\",\"cache\":\"y\"}", toJson(jackson(TransientField.class), value));
    }

    @Test
    void transientField_fix_renameAccessors() {
        TransientFieldFixed value = new TransientFieldFixed();
        value.name = "x";
        value.cache("y");
        assertEquals("{\"name\":\"x\"}", toJson(jackson(TransientFieldFixed.class), value));
    }

    // 9. Numbers inside Object or Map<String, Object>.
    //    Symptom: Gson read every number as Double, Jackson reads Integer, Long or Double, so casts to
    //    Double throw ClassCastException and echoed values change from 1.0 to 1.
    //    Fix: declare the value type, or read values through Number.

    public static class UntypedMap {
        public Map<String, Object> attributes;
    }

    public static class UntypedMapFixed {
        public Map<String, Integer> attributes;
    }

    @Test
    void untypedMap_numberTypeChangesUnderJackson() {
        String json = "{\"attributes\":{\"count\":1}}";
        UntypedMap fromGson = gson(UntypedMap.class).fromJson(json);
        UntypedMap fromJackson = jackson(UntypedMap.class).fromJson(json);

        assertEquals(Double.class, fromGson.attributes.get("count").getClass());
        assertEquals(Integer.class, fromJackson.attributes.get("count").getClass());

        assertEquals("{\"attributes\":{\"count\":1.0}}", toJson(gson(UntypedMap.class), fromGson));
        assertEquals("{\"attributes\":{\"count\":1}}", toJson(jackson(UntypedMap.class), fromJackson));
    }

    @Test
    void untypedMap_fix_declareValueType() {
        String json = "{\"attributes\":{\"count\":1}}";
        assertEquals(Integer.valueOf(1), gson(UntypedMapFixed.class).fromJson(json).attributes.get("count"));
        assertEquals(Integer.valueOf(1), jackson(UntypedMapFixed.class).fromJson(json).attributes.get("count"));
    }

    // 10. Nested types.
    //     Symptom: patterns 1 to 9 apply to every nested object and list element, even when the
    //     top-level type has getters and setters.
    //     Fix: apply the fix to the nested types as well.

    public static class Outer {
        private PrivateFields inner;
        private List<PrivateFields> items;

        public PrivateFields getInner() {
            return inner;
        }

        public void setInner(PrivateFields inner) {
            this.inner = inner;
        }

        public List<PrivateFields> getItems() {
            return items;
        }

        public void setItems(List<PrivateFields> items) {
            this.items = items;
        }
    }

    public static class OuterFixed {
        private PrivateFieldsFixed inner;
        private List<PrivateFieldsFixed> items;

        public PrivateFieldsFixed getInner() {
            return inner;
        }

        public void setInner(PrivateFieldsFixed inner) {
            this.inner = inner;
        }

        public List<PrivateFieldsFixed> getItems() {
            return items;
        }

        public void setItems(List<PrivateFieldsFixed> items) {
            this.items = items;
        }
    }

    @Test
    void nestedTypes_droppedByJackson() {
        String json = "{\"inner\":{\"name\":\"x\"},\"items\":[{\"name\":\"y\"}]}";

        Outer fromGson = gson(Outer.class).fromJson(json);
        assertEquals("x", fromGson.getInner().name);
        assertEquals("y", fromGson.getItems().get(0).name);

        Outer fromJackson = jackson(Outer.class).fromJson(json);
        assertNull(fromJackson.getInner().name);
        assertNull(fromJackson.getItems().get(0).name);

        assertEquals(json, toJson(gson(Outer.class), fromGson));
        assertEquals("{\"inner\":{},\"items\":[{}]}", toJson(jackson(Outer.class), fromGson));
    }

    @Test
    void nestedTypes_fix_applyToNestedTypes() {
        String json = "{\"inner\":{\"name\":\"x\"},\"items\":[{\"name\":\"y\"}]}";

        OuterFixed value = jackson(OuterFixed.class).fromJson(json);
        assertEquals("x", value.getInner().getName());
        assertEquals("y", value.getItems().get(0).getName());
        assertEquals(json, toJson(jackson(OuterFixed.class), value));
    }

    // Same factory instances and Type overload as EventHandlerLoader.getSerializer in the RIC.

    @SuppressWarnings("unchecked")
    private static <T> PojoSerializer<T> gson(Class<T> type) {
        return (PojoSerializer<T>) (PojoSerializer<?>) GsonFactory.getInstance().getSerializer((Type) type);
    }

    @SuppressWarnings("unchecked")
    private static <T> PojoSerializer<T> jackson(Class<T> type) {
        return (PojoSerializer<T>) (PojoSerializer<?>) JacksonFactory.getInstance().getSerializer((Type) type);
    }

    private static <T> String toJson(PojoSerializer<T> serializer, T value) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        serializer.toJson(value, output);
        return new String(output.toByteArray(), StandardCharsets.UTF_8);
    }

    private static JsonNode tree(String json) {
        try {
            return MAPPER.readTree(json);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
