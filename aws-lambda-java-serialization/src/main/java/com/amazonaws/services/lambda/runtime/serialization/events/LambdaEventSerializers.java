/* Copyright 2019 Amazon.com, Inc. or its affiliates. All Rights Reserved. */

package com.amazonaws.services.lambda.runtime.serialization.events;

import com.amazonaws.services.lambda.runtime.serialization.events.mixins.CloudFormationCustomResourceEventMixin;
import com.amazonaws.services.lambda.runtime.serialization.events.mixins.CloudFrontEventMixin;
import com.amazonaws.services.lambda.runtime.serialization.events.mixins.CloudWatchLogsEventMixin;
import com.amazonaws.services.lambda.runtime.serialization.events.mixins.CodeCommitEventMixin;
import com.amazonaws.services.lambda.runtime.serialization.events.mixins.ConnectEventMixin;
import com.amazonaws.services.lambda.runtime.serialization.events.mixins.DynamodbEventMixin;
import com.amazonaws.services.lambda.runtime.serialization.events.mixins.DynamodbTimeWindowEventMixin;
import com.amazonaws.services.lambda.runtime.serialization.events.mixins.KinesisEventMixin;
import com.amazonaws.services.lambda.runtime.serialization.events.mixins.KinesisTimeWindowEventMixin;
import com.amazonaws.services.lambda.runtime.serialization.events.mixins.SNSEventMixin;
import com.amazonaws.services.lambda.runtime.serialization.events.mixins.SQSEventMixin;
import com.amazonaws.services.lambda.runtime.serialization.events.mixins.ScheduledEventMixin;
import com.amazonaws.services.lambda.runtime.serialization.events.mixins.SecretsManagerRotationEventMixin;
import com.amazonaws.services.lambda.runtime.serialization.factories.JacksonFactory;
import com.amazonaws.services.lambda.runtime.serialization.PojoSerializer;
import com.amazonaws.services.lambda.runtime.serialization.util.ReflectUtil;
import com.amazonaws.services.lambda.runtime.serialization.util.SerializeUtil;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.amazonaws.services.lambda.runtime.serialization.events.modules.DateModule;
import com.amazonaws.services.lambda.runtime.serialization.events.modules.DateTimeModule;
import com.amazonaws.services.lambda.runtime.serialization.events.serializers.OrgJsonSerializer;
import com.amazonaws.services.lambda.runtime.serialization.events.serializers.S3EventSerializer;

import java.util.AbstractMap.SimpleEntry;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * This class provides serializers for Lambda supported events.
 *
 * HOW TO ADD SUPPORT FOR A NEW EVENT MODEL:
 *
 * Option 1 (Preferred):
 * 1. Add Class name to SUPPORTED_EVENTS
 * 2. Add Mixin Class to com.amazonaws.services.lambda.runtime.serialization.events.mixins package (if needed)
 * 3. Add entries to MIXIN_MAP for event class and sub classes (if needed)
 * 4. Add entries to NESTED_CLASS_MAP for event class and sub classes (if needed)
 * 5. Add entry to NAMING_STRATEGY_MAP (if needed i.e. Could be used in place of a mixin)
 *
 * Option 2 (longer - for event models that do not work with Jackson or GSON):
 * 1. Add Class name to SUPPORTED_EVENTS
 * 2. Add serializer (using org.json) to com.amazonaws.services.lambda.runtime.serialization.events.serializers
 * 3. Add class name and serializer to SERIALIZER_MAP
 */
public class LambdaEventSerializers {

    /**
     * list of supported events
     */
    private static final List<String> SUPPORTED_EVENTS = Stream.of(
            "com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent",
            "com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent",
            "com.amazonaws.services.lambda.runtime.events.CloudFormationCustomResourceEvent",
            "com.amazonaws.services.lambda.runtime.events.CloudFrontEvent",
            "com.amazonaws.services.lambda.runtime.events.CloudWatchLogsEvent",
            "com.amazonaws.services.lambda.runtime.events.CodeCommitEvent",
            "com.amazonaws.services.lambda.runtime.events.CognitoEvent",
            "com.amazonaws.services.lambda.runtime.events.ConfigEvent",
            "com.amazonaws.services.lambda.runtime.events.ConnectEvent",
            "com.amazonaws.services.lambda.runtime.events.DynamodbEvent",
            "com.amazonaws.services.lambda.runtime.events.DynamodbTimeWindowEvent",
            "com.amazonaws.services.lambda.runtime.events.IoTButtonEvent",
            "com.amazonaws.services.lambda.runtime.events.KinesisEvent",
            "com.amazonaws.services.lambda.runtime.events.KinesisTimeWindowEvent",
            "com.amazonaws.services.lambda.runtime.events.KinesisFirehoseEvent",
            "com.amazonaws.services.lambda.runtime.events.LambdaDestinationEvent",
            "com.amazonaws.services.lambda.runtime.events.LexEvent",
            "com.amazonaws.services.lambda.runtime.events.ScheduledEvent",
            "com.amazonaws.services.lambda.runtime.events.SecretsManagerRotationEvent",
            "com.amazonaws.services.s3.event.S3EventNotification",
            "com.amazonaws.services.lambda.runtime.events.models.s3.S3EventNotification",
            "com.amazonaws.services.lambda.runtime.events.S3Event",
            "com.amazonaws.services.lambda.runtime.events.SNSEvent",
            "com.amazonaws.services.lambda.runtime.events.SQSEvent")
            .collect(Collectors.toList());

    /**
     * list of events incompatible with Jackson, with serializers explicitly defined
     * Classes are incompatible with Jackson for any of the following reasons:
     * 1. different constructor/setter types from getter types
     * 2. various bugs within Jackson
     */
    private static final Map<String, OrgJsonSerializer> SERIALIZER_MAP  = Stream.of(
            new SimpleEntry<>("com.amazonaws.services.s3.event.S3EventNotification", new S3EventSerializer<>()),
            new SimpleEntry<>("com.amazonaws.services.lambda.runtime.events.models.s3.S3EventNotification", new S3EventSerializer<>()),
            new SimpleEntry<>("com.amazonaws.services.lambda.runtime.events.S3Event", new S3EventSerializer<>()))
            .collect(Collectors.toMap(SimpleEntry::getKey, SimpleEntry::getValue));

    /**
     * Maps supported event classes to mixin classes with Jackson annotations.
     * Jackson annotations are not loaded through the ClassLoader so if a Java field is serialized or deserialized from a
     * json field that does not match the Jave field name, then a Mixin is required.
     */
    @SuppressWarnings("rawtypes")
    private static final Map<String, Class> MIXIN_MAP = Stream.of(
            new SimpleEntry<>("com.amazonaws.services.lambda.runtime.events.CloudFormationCustomResourceEvent",
                    CloudFormationCustomResourceEventMixin.class),
            new SimpleEntry<>("com.amazonaws.services.lambda.runtime.events.CloudFrontEvent",
                    CloudFrontEventMixin.class),
            new SimpleEntry<>("com.amazonaws.services.lambda.runtime.events.CloudWatchLogsEvent",
                    CloudWatchLogsEventMixin.class),
            new SimpleEntry<>("com.amazonaws.services.lambda.runtime.events.CodeCommitEvent",
                    CodeCommitEventMixin.class),
            new SimpleEntry<>("com.amazonaws.services.lambda.runtime.events.CodeCommitEvent$Record",
                    CodeCommitEventMixin.RecordMixin.class),
            new SimpleEntry<>("com.amazonaws.services.lambda.runtime.events.ConnectEvent",
                    ConnectEventMixin.class),
            new SimpleEntry<>("com.amazonaws.services.lambda.runtime.events.ConnectEvent$Details",
                    ConnectEventMixin.DetailsMixin.class),
            new SimpleEntry<>("com.amazonaws.services.lambda.runtime.events.ConnectEvent$ContactData",
                    ConnectEventMixin.ContactDataMixin.class),
            new SimpleEntry<>("com.amazonaws.services.lambda.runtime.events.ConnectEvent$CustomerEndpoint",
                    ConnectEventMixin.CustomerEndpointMixin.class),
            new SimpleEntry<>("com.amazonaws.services.lambda.runtime.events.ConnectEvent$SystemEndpoint",
                    ConnectEventMixin.SystemEndpointMixin.class),
            new SimpleEntry<>("com.amazonaws.services.lambda.runtime.events.DynamodbEvent",
                    DynamodbEventMixin.class),
            new SimpleEntry<>("com.amazonaws.services.lambda.runtime.events.DynamodbEvent$DynamodbStreamRecord",
                    DynamodbEventMixin.DynamodbStreamRecordMixin.class),
            new SimpleEntry<>("com.amazonaws.services.dynamodbv2.model.StreamRecord",
                    DynamodbEventMixin.StreamRecordMixin.class),
            new SimpleEntry<>("com.amazonaws.services.lambda.runtime.events.models.dynamodb.StreamRecord",
                    DynamodbEventMixin.StreamRecordMixin.class),
            new SimpleEntry<>("com.amazonaws.services.dynamodbv2.model.AttributeValue",
                    DynamodbEventMixin.AttributeValueMixin.class),
            new SimpleEntry<>("com.amazonaws.services.lambda.runtime.events.models.dynamodb.AttributeValue",
                    DynamodbEventMixin.AttributeValueMixin.class),
            new SimpleEntry<>("com.amazonaws.services.lambda.runtime.events.DynamodbTimeWindowEvent",
                DynamodbTimeWindowEventMixin.class),
            new SimpleEntry<>("com.amazonaws.services.lambda.runtime.events.KinesisEvent",
                    KinesisEventMixin.class),
            new SimpleEntry<>("com.amazonaws.services.lambda.runtime.events.KinesisEvent$Record",
                    KinesisEventMixin.RecordMixin.class),
            new SimpleEntry<>("com.amazonaws.services.lambda.runtime.events.KinesisTimeWindowEvent",
                KinesisTimeWindowEventMixin.class),
            new SimpleEntry<>("com.amazonaws.services.lambda.runtime.events.ScheduledEvent",
                    ScheduledEventMixin.class),
            new SimpleEntry<>("com.amazonaws.services.lambda.runtime.events.SecretsManagerRotationEvent",
                    SecretsManagerRotationEventMixin.class),
            new SimpleEntry<>("com.amazonaws.services.lambda.runtime.events.SNSEvent",
                    SNSEventMixin.class),
            new SimpleEntry<>("com.amazonaws.services.lambda.runtime.events.SNSEvent$SNSRecord",
                    SNSEventMixin.SNSRecordMixin.class),
            new SimpleEntry<>("com.amazonaws.services.lambda.runtime.events.SQSEvent",
                    SQSEventMixin.class),
            new SimpleEntry<>("com.amazonaws.services.lambda.runtime.events.SQSEvent$SQSMessage",
                    SQSEventMixin.SQSMessageMixin.class))
            .collect(Collectors.toMap(SimpleEntry::getKey, SimpleEntry::getValue));

    /**
     * If mixins are required for inner classes of an event, then those nested classes must be specified here.
     */
    @SuppressWarnings("rawtypes")
    private static final Map<String, List<NestedClass>> NESTED_CLASS_MAP = Stream.of(
            new SimpleEntry<>("com.amazonaws.services.lambda.runtime.events.CodeCommitEvent",
                    Arrays.asList(
                            new NestedClass("com.amazonaws.services.lambda.runtime.events.CodeCommitEvent$Record"))),
            new SimpleEntry<>("com.amazonaws.services.lambda.runtime.events.CognitoEvent",
                    Arrays.asList(
                            new NestedClass("com.amazonaws.services.lambda.runtime.events.CognitoEvent$DatasetRecord"))),
            new SimpleEntry<>("com.amazonaws.services.lambda.runtime.events.ConnectEvent",
                    Arrays.asList(
                            new NestedClass("com.amazonaws.services.lambda.runtime.events.ConnectEvent$Details"),
                            new NestedClass("com.amazonaws.services.lambda.runtime.events.ConnectEvent$ContactData"),
                            new NestedClass("com.amazonaws.services.lambda.runtime.events.ConnectEvent$CustomerEndpoint"),
                            new NestedClass("com.amazonaws.services.lambda.runtime.events.ConnectEvent$SystemEndpoint"))),
            new SimpleEntry<>("com.amazonaws.services.lambda.runtime.events.DynamodbEvent",
                    Arrays.asList(
                            new AlternateNestedClass(
                                    "com.amazonaws.services.lambda.runtime.events.models.dynamodb.AttributeValue",
                                    "com.amazonaws.services.dynamodbv2.model.AttributeValue"),
                            new AlternateNestedClass(
                                    "com.amazonaws.services.lambda.runtime.events.models.dynamodb.StreamRecord",
                                    "com.amazonaws.services.dynamodbv2.model.StreamRecord"),
                            new NestedClass("com.amazonaws.services.lambda.runtime.events.DynamodbEvent$DynamodbStreamRecord"))),
            new SimpleEntry<>("com.amazonaws.services.lambda.runtime.events.DynamodbTimeWindowEvent",
                Arrays.asList(
                    new AlternateNestedClass(
                        "com.amazonaws.services.lambda.runtime.events.models.dynamodb.AttributeValue",
                        "com.amazonaws.services.dynamodbv2.model.AttributeValue"),
                    new AlternateNestedClass(
                        "com.amazonaws.services.lambda.runtime.events.models.dynamodb.StreamRecord",
                        "com.amazonaws.services.dynamodbv2.model.StreamRecord"),
                    new NestedClass("com.amazonaws.services.lambda.runtime.events.DynamodbEvent$DynamodbStreamRecord"))),
            new SimpleEntry<>("com.amazonaws.services.lambda.runtime.events.KinesisEvent",
                    Arrays.asList(
                            new NestedClass("com.amazonaws.services.lambda.runtime.events.KinesisEvent$Record"))),
            new SimpleEntry<>("com.amazonaws.services.lambda.runtime.events.SNSEvent",
                    Arrays.asList(
                            new NestedClass("com.amazonaws.services.lambda.runtime.events.SNSEvent$SNSRecord"))),
            new SimpleEntry<>("com.amazonaws.services.lambda.runtime.events.SQSEvent",
                    Arrays.asList(
                            new NestedClass("com.amazonaws.services.lambda.runtime.events.SQSEvent$SQSMessage"))))
            .collect(Collectors.toMap(SimpleEntry::getKey, SimpleEntry::getValue));

    /**
     * If event requires a naming strategy. For example, when someone names the getter method getSNS and the setter
     * method setSns, for some magical reasons, using both mixins and a naming strategy works
     */
    private static final Map<String, PropertyNamingStrategy> NAMING_STRATEGY_MAP = Stream.of(
            new SimpleEntry<>("com.amazonaws.services.lambda.runtime.events.SNSEvent",
                    new PropertyNamingStrategy.PascalCaseStrategy()))
            .collect(Collectors.toMap(SimpleEntry::getKey, SimpleEntry::getValue));

    /**
     * Returns whether the class name is a Lambda supported event model.
     * @param className class name as string
     * @return whether the event model is supported
     */
    public static boolean isLambdaSupportedEvent(String className) {
        return SUPPORTED_EVENTS.contains(className);
    }

    /**
     * Return a serializer for the event class
     * @return a specific PojoSerializer or modified JacksonFactory instance with mixins and modules added in
     */
    @SuppressWarnings({"unchecked"})
    public static <T> PojoSerializer<T> serializerFor(Class<T> eventClass, ClassLoader classLoader) {
        // if serializer specifically defined for event then use that
        if (SERIALIZER_MAP.containsKey(eventClass.getName())) {
            return SERIALIZER_MAP.get(eventClass.getName()).withClass(eventClass).withClassLoader(classLoader);
        }
        // else use a Jackson ObjectMapper instance
        JacksonFactory factory = JacksonFactory.getInstance();
        // if mixins required for class, then apply
        if (MIXIN_MAP.containsKey(eventClass.getName())) {
            factory = factory.withMixin(eventClass, MIXIN_MAP.get(eventClass.getName()));
        }
        // if event model has nested classes then load those classes and check if mixins apply
        if (NESTED_CLASS_MAP.containsKey(eventClass.getName())) {
            List<NestedClass> nestedClasses = NESTED_CLASS_MAP.get(eventClass.getName());
            for (NestedClass nestedClass: nestedClasses) {
                // if mixin exists for nested class then apply
                if (MIXIN_MAP.containsKey(nestedClass.className)) {
                    factory = tryLoadingNestedClass(classLoader, factory, nestedClass);
                }
            }
        }
        // load DateModules
        factory.getMapper().registerModules(new DateModule(), new DateTimeModule(classLoader));
        // load naming strategy if needed
        if (NAMING_STRATEGY_MAP.containsKey(eventClass.getName())) {
            factory = factory.withNamingStrategy(NAMING_STRATEGY_MAP.get(eventClass.getName()));
        }
        return factory.getSerializer(eventClass);
    }

    /**
     * Tries to load a nested class with its defined mixin from {@link #MIXIN_MAP} into the {@link JacksonFactory} object.
     * Will allow initial failure for {@link AlternateNestedClass} objects and try again with their alternate class name
     * @return a modified JacksonFactory instance with mixins added in
     */
    private static JacksonFactory tryLoadingNestedClass(ClassLoader classLoader, JacksonFactory factory, NestedClass nestedClass) {
        Class<?> eventClazz;
        Class<?> mixinClazz;
        try {
            eventClazz = SerializeUtil.loadCustomerClass(nestedClass.getClassName(), classLoader);
            mixinClazz = MIXIN_MAP.get(nestedClass.getClassName());
        } catch (ReflectUtil.ReflectException e) {
            if (nestedClass instanceof AlternateNestedClass) {
                AlternateNestedClass alternateNestedClass = (AlternateNestedClass) nestedClass;
                eventClazz = SerializeUtil.loadCustomerClass(alternateNestedClass.getAlternateClassName(), classLoader);
                mixinClazz = MIXIN_MAP.get(alternateNestedClass.getAlternateClassName());
            } else {
                throw e;
            }
        }

        return factory.withMixin(eventClazz, mixinClazz);
    }

    private static class NestedClass {
        private final String className;

        protected NestedClass(String className) {
            this.className = className;
        }

        protected String getClassName() {
            return className;
        }
    }

    private static class AlternateNestedClass extends NestedClass {
        private final String alternateClassName;

        private AlternateNestedClass(String className, String alternateClassName) {
            super(className);
            this.alternateClassName = alternateClassName;
        }

        private String getAlternateClassName() {
            return alternateClassName;
        }
    }
}
