# Jackson 2.18 – `joda.time.DateTime` Deserialization Regression

## Symptom

After upgrading Jackson from **2.15.4 → 2.18.6**, deserializing `joda.time.DateTime` throws:

```
com.amazonaws.lambda.thirdparty.com.fasterxml.jackson.databind.exc.MismatchedInputException:
Cannot construct instance of `org.joda.time.DateTime` (although at least one Creator exists):
no String-argument constructor/factory method to deserialize from String value ('2016-01-01T23:59:59.000+0000')
```

Reproduced by: `EventLoaderTest.testLoadCodeCommitEvent` — `CodeCommitEvent.Record.eventTime` is of type `org.joda.time.DateTime`.

## Root Cause

### Background: how `DateTime` deserialization is supposed to work

`LambdaEventSerializers.serializerFor()` registers a `DateTimeModule` (which extends `JodaModule`) on the mapper before returning the serializer:

```java
// LambdaEventSerializers.java line 262
factory.getMapper().registerModules(new DateModule(), new DateTimeModule(classLoader));
```

`DateTimeModule` registers a custom deserializer for `org.joda.time.DateTime` (the customer's unshaded class, via the pom reverse-transform workaround). This deserializer simply calls `DateTime.parse(jsonParser.getValueAsString())` and handles any String input correctly.

### What broke in Jackson 2.18

`JacksonFactory` disables creator auto-detection:

```java
// JacksonFactory.java line 68
.disable(MapperFeature.AUTO_DETECT_CREATORS)
```

Jackson 2.18 shipped a complete rewrite of `POJOPropertiesCollector` ([databind #4515](https://github.com/FasterXML/jackson-databind/issues/4515)), which moved creator detection logic from `BasicDeserializerFactory` into `POJOPropertiesCollector` and changed when it runs.

With `AUTO_DETECT_CREATORS` disabled, no creators are found for `DateTime` during bean property collection. In Jackson 2.18's rewritten code path, when no creators are detected for a type, `_findCustomDeserializer()` — the lookup that would find `DateTimeModule`'s registered deserializer — is bypassed. Jackson falls straight through to building a `BeanDeserializer` for `DateTime`.

The `BeanDeserializer` finds `DateTime()`'s no-arg constructor (which is not gated by `AUTO_DETECT_CREATORS`), so it reports "at least one Creator exists" — but that creator doesn't accept a String, hence the error.

### Why it worked in 2.15.4

In Jackson 2.15.4, `_findCustomDeserializer()` was called **unconditionally**, regardless of whether creator detection found anything. So `DateTimeModule`'s registered deserializer was always found and used. That unconditional call was the fallback that 2.18 removed as part of the `#4515` rewrite.

## Fix Options

### Option 1 — Remove `.disable(MapperFeature.AUTO_DETECT_CREATORS)` from `JacksonFactory`

Simplest fix. Jackson's default is enabled, so this aligns with standard behaviour. Risk: could enable constructor-based creator detection for customer POJOs, potentially changing deserialization semantics for handler inputs that have multi-arg constructors. The original intent behind disabling it is undocumented (present since the initial commit), so the blast radius is unknown.

To confirm this is the cause: remove the line and run `testLoadCodeCommitEvent`.

### Option 2 — Use `BeanDeserializerModifier` in `DateTimeModule` ✅ Recommended

More targeted. Since Jackson 2.18 IS reaching `BeanDeserializer` for `DateTime` (confirmed by the error), `BeanDeserializerModifier.modifyDeserializer()` is called at that point. We can intercept it there and swap in the custom deserializer — without touching `AUTO_DETECT_CREATORS` at all.

Add the following to the `DateTimeModule` constructor, after the existing `addDeserializer` call:

```java
this.setDeserializerModifier(new BeanDeserializerModifier() {
    @Override
    public JsonDeserializer<?> modifyDeserializer(DeserializationConfig config,
            BeanDescription beanDesc, JsonDeserializer<?> deserializer) {
        if (beanDesc.getBeanClass() == dateTimeClass) {
            return getDeserializer(dateTimeClass);
        }
        return deserializer;
    }
});
```

This is scoped only to `DateTime`, leaves `AUTO_DETECT_CREATORS` untouched, and fixes exactly the broken code path without risk of side effects elsewhere.

## Related Issues

| Issue | Description |
|-------|-------------|
| [databind #4515](https://github.com/FasterXML/jackson-databind/issues/4515) | Rewrite of `POJOPropertiesCollector` — root cause of the behaviour change |
| [databind #4920](https://github.com/FasterXML/jackson-databind/issues/4920) | Regression introduced by #4515: custom module deserializers bypassed when creator detection finds nothing |
| [databind #4982](https://github.com/FasterXML/jackson-databind/pull/4982) | Partial fix in 2.18.3, does not cover the `AUTO_DETECT_CREATORS = false` case |
