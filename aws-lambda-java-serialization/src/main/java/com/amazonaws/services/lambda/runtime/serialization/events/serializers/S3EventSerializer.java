/* Copyright 2019 Amazon.com, Inc. or its affiliates. All Rights Reserved. */

package com.amazonaws.services.lambda.runtime.serialization.events.serializers;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UncheckedIOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import com.amazonaws.services.lambda.runtime.serialization.util.Functions;
import com.amazonaws.services.lambda.runtime.serialization.util.ReflectUtil;
import com.amazonaws.services.lambda.runtime.serialization.util.SerializeUtil;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Serializer for S3 event
 * NOTE: Because the s3 event class provided by the SDK does not play well with Jackson through a class laoder,
 * this class uses the low level org json library to serialize and deserialize the event. If new events are added
 * that do not work well with Jackson or GSON, this is the fallback method that will always work but is more verbose.
 */
public class S3EventSerializer<T> implements OrgJsonSerializer<T> {

    /**
     * As part of https://github.com/aws/aws-lambda-java-libs/issues/74 the `com.amazonaws.services.s3.event.S3EventNotification`
     *  class used by the aws-lambda-java-events library was adapted from the AWSS3JavaClient library into
     *  `com.amazonaws.services.lambda.runtime.events.models.s3.S3EventNotification`, hence the need to support both classes
     *  in the runtime
     * @see com.amazonaws.services.lambda.runtime.events.S3Event;
     * @see com.amazonaws.services.lambda.runtime.events.models.s3.S3EventNotification;
     * @see com.amazonaws.services.s3.event.S3EventNotification;
     */
    private static final String S3_EVENT_NOTIFICATION_CLASS_V3 = "com.amazonaws.services.lambda.runtime.events.models.s3.S3EventNotification";
    private static final String S3_EVENT_NOTIFICATION_CLASS_V2 = "com.amazonaws.services.s3.event.S3EventNotification";

    /**
     * S3 event class
     * @see com.amazonaws.services.lambda.runtime.events.S3Event;
     * @see com.amazonaws.services.lambda.runtime.events.models.s3.S3EventNotification;
     * @see com.amazonaws.services.s3.event.S3EventNotification;
     */
    private Class<T> eventClass;

    /**
     * ClassLoader to be used when loading S3 event classes
     */
    private ClassLoader classLoader;

    /**
     * Construct s3Event Serialize from specific s3 event class from user
     * @param eventClass s3 event class
     * @see com.amazonaws.services.lambda.runtime.events.S3Event;
     * @see com.amazonaws.services.lambda.runtime.events.models.s3.S3EventNotification;
     * @see com.amazonaws.services.s3.event.S3EventNotification;
     */
    @Override
    public S3EventSerializer<T> withClass(Class<T> eventClass) {
        this.eventClass = eventClass;
        return this;
    }

    /**
     * Sets the ClassLoader that will be used to load S3 event classes
     * @param classLoader - ClassLoader that S3 event classes will be loaded from
     */
    @Override
    public S3EventSerializer<T> withClassLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
        return this;
    }


    /**
     * deserialize an instance of an s3 event from an input stream
     * @param input InputStream reading from
     * @return S3Event Object
     */
    public T fromJson(InputStream input) {
        return fromJson(SerializeUtil.convertStreamToString(input));
    }

    /**
     * deserialize an instance of an s3 event from a string
     * @param input String with JSON
     * @return s3Event object
     */
    public T fromJson(String input) {
        JSONObject jsonObject = new JSONObject((input));
        return deserializeEvent(jsonObject);
    }

    /**
     * serialize an S3 event object to the output stream
     * @param value S3 event object
     * @param output OutputStream serializing to
     */
    public void toJson(T value, OutputStream output) {
        JSONObject jsonObject;
        try {
            // Try to load newer version of S3EventNotification from aws-lambda-java-events v3+
            Class eventNotificationRecordClass = SerializeUtil.loadCustomerClass(
                    S3_EVENT_NOTIFICATION_CLASS_V3 + "$S3EventNotificationRecord", classLoader);
            jsonObject = serializeEvent(eventNotificationRecordClass, value, S3_EVENT_NOTIFICATION_CLASS_V3);

        } catch (Exception ex) {
            // Fallback to aws-lambda-java-events pre-v3 (relies on aws-s3-sdk)
            Class eventNotificationRecordClass = SerializeUtil.loadCustomerClass(
                    S3_EVENT_NOTIFICATION_CLASS_V2 + "$S3EventNotificationRecord", classLoader);
            jsonObject = serializeEvent(eventNotificationRecordClass, value, S3_EVENT_NOTIFICATION_CLASS_V2);
        }

        // Writer in try block so that writer gets flushed and closed
        try (Writer writer = new OutputStreamWriter(output)) {
            writer.write(jsonObject.toString());
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    /**
     * serialize an s3 event
     * @param eventNotificationRecordClass class holding the s3 event notification record
     * @param value s3 event object
     * @param baseClassName base class name
     * @return JSONObject that contains s3 event
     */
    @SuppressWarnings({"unchecked"})
    private JSONObject serializeEvent(Class eventNotificationRecordClass, T value, String baseClassName) {
        JSONObject jsonObject = new JSONObject();
        Functions.R0<List> getRecordsMethod = ReflectUtil.bindInstanceR0(value, "getRecords", true, List.class);
        jsonObject.put("Records", serializeEventNotificationRecordList(getRecordsMethod.call(), eventNotificationRecordClass, baseClassName));
        return jsonObject;
    }

    /**
     * deserialize an s3 event
     * @param jsonObject JSONObject with s3 data
     * @return S3 Event Object
     */
    @SuppressWarnings({"unchecked"})
    private T deserializeEvent(JSONObject jsonObject) {
        Functions.R1<T, List> constructor = ReflectUtil.loadConstructor1(eventClass, true, List.class);
        JSONArray records = jsonObject.optJSONArray("Records");
        try {
            // Try to load newer version of S3EventNotification from aws-lambda-java-events v3+
            Class recordClass = SerializeUtil.loadCustomerClass(
                    S3_EVENT_NOTIFICATION_CLASS_V3 + "$S3EventNotificationRecord", classLoader);
            return (T) constructor.call(deserializeEventNotificationRecordList(records, recordClass,
                    S3_EVENT_NOTIFICATION_CLASS_V3));

        } catch (Exception ex) {
            // Fallback to aws-lambda-java-events pre-v3 (relies on aws-s3-sdk)
            Class eventNotificationRecordClass = SerializeUtil.loadCustomerClass(
                    S3_EVENT_NOTIFICATION_CLASS_V2 + "$S3EventNotificationRecord", classLoader);
            return (T) constructor.call(deserializeEventNotificationRecordList(records, eventNotificationRecordClass,
                    S3_EVENT_NOTIFICATION_CLASS_V2));
        }
    }

    /**
     * serialize an s3 event notification record list
     * @param eventNotificationRecords List of event notification records
     * @param <A> EventNotificationRecord
     * @return JSONArray with s3 event records
     */
    @SuppressWarnings({"unchecked"})
    private <A> JSONArray serializeEventNotificationRecordList(List eventNotificationRecords,
                                                               Class<A> eventNotificationRecordClass,
                                                               String baseClassName) {
        JSONArray jsonRecords = new JSONArray();
        for (Object eventNotificationRecord: eventNotificationRecords) {
            jsonRecords.put(serializeEventNotificationRecord((A) eventNotificationRecord, baseClassName));
        }
        return jsonRecords;
    }

    /**
     * deserialize an s3 event notification record
     * @param jsonRecords JSONArray of event notification records
     * @param eventNotificiationRecordClass Event notification record class
     * @param <A> Event notification record type
     * @return List of event notification records
     */
    @SuppressWarnings({"unchecked"})
    private <A> List<A> deserializeEventNotificationRecordList(JSONArray jsonRecords,
                                                               Class<A> eventNotificiationRecordClass,
                                                               String baseClassName) {
        if (jsonRecords == null) {
            jsonRecords = new JSONArray();
        }
        Class s3EntityClass = SerializeUtil.loadCustomerClass(baseClassName + "$S3Entity", classLoader);
        Class s3BucketClass = SerializeUtil.loadCustomerClass(baseClassName + "$S3BucketEntity", classLoader);
        Class s3ObjectClass = SerializeUtil.loadCustomerClass(baseClassName + "$S3ObjectEntity", classLoader);
        Class requestParametersClass = SerializeUtil.loadCustomerClass(baseClassName + "$RequestParametersEntity", classLoader);
        Class responseElementsClass = SerializeUtil.loadCustomerClass(baseClassName + "$ResponseElementsEntity", classLoader);
        Class userIdentityClass = SerializeUtil.loadCustomerClass(baseClassName + "$UserIdentityEntity", classLoader);

        List<A> records = new ArrayList<>();
        for (int i=0; i < jsonRecords.length(); i++) {
            records.add((A) deserializeEventNotificationRecord(
                    jsonRecords.getJSONObject(i), eventNotificiationRecordClass, s3EntityClass, s3BucketClass,
                    s3ObjectClass, requestParametersClass, responseElementsClass, userIdentityClass));
        }
        return records;
    }

    /**
     * serialize an s3 event notification record
     * @param eventNotificationRecord Event notification record
     * @param <A> Event notification record type
     * @return JSONObject
     */
    private <A> JSONObject serializeEventNotificationRecord(A eventNotificationRecord, String baseClassName) {
        // reflect load all the classes we need
        Class s3EntityClass = SerializeUtil.loadCustomerClass(baseClassName + "$S3Entity", classLoader);
        Class requestParametersClass = SerializeUtil.loadCustomerClass(baseClassName + "$RequestParametersEntity", classLoader);
        Class responseElementsClass = SerializeUtil.loadCustomerClass(baseClassName + "$ResponseElementsEntity", classLoader);
        Class userIdentityClass = SerializeUtil.loadCustomerClass(baseClassName + "$UserIdentityEntity", classLoader);
        // Workaround not to let maven shade plugin relocating string literals https://issues.apache.org/jira/browse/MSHADE-156
        Class dateTimeClass = SerializeUtil.loadCustomerClass("com.amazonaws.lambda.unshade.thirdparty.org.joda.time.DateTime", classLoader);
        // serialize object
        JSONObject jsonObject = new JSONObject();
        Functions.R0<String> getAwsRegionMethod =
                ReflectUtil.bindInstanceR0(eventNotificationRecord, "getAwsRegion", true, String.class);
        jsonObject.put("awsRegion", getAwsRegionMethod.call());
        Functions.R0<String> getEventNameMethod =
                ReflectUtil.bindInstanceR0(eventNotificationRecord, "getEventName", true, String.class);
        jsonObject.put("eventName", getEventNameMethod.call());
        Functions.R0<String> getEventSourceMethod =
                ReflectUtil.bindInstanceR0(eventNotificationRecord, "getEventSource", true, String.class);
        jsonObject.put("eventSource", getEventSourceMethod.call());
        Functions.R0<?> getEventTimeMethod =
                ReflectUtil.bindInstanceR0(eventNotificationRecord, "getEventTime", true, dateTimeClass);
        jsonObject.put("eventTime", SerializeUtil.serializeDateTime(getEventTimeMethod.call(), classLoader));
        Functions.R0<String> getEventVersionMethod =
                ReflectUtil.bindInstanceR0(eventNotificationRecord, "getEventVersion", true, String.class);
        jsonObject.put("eventVersion", getEventVersionMethod.call());
        Functions.R0<?> getRequestParametersMethod =
                ReflectUtil.bindInstanceR0(eventNotificationRecord, "getRequestParameters", true, requestParametersClass);
        jsonObject.put("requestParameters", serializeRequestParameters(getRequestParametersMethod.call()));
        Functions.R0<?> getResponseElementsMethod =
                ReflectUtil.bindInstanceR0(eventNotificationRecord, "getResponseElements", true, responseElementsClass);
        jsonObject.put("responseElements", serializeResponseElements(getResponseElementsMethod.call()));
        Functions.R0<?> getS3EntityMethod =
                ReflectUtil.bindInstanceR0(eventNotificationRecord, "getS3", true, s3EntityClass);
        jsonObject.put("s3", serializeS3Entity(getS3EntityMethod.call(), baseClassName));
        Functions.R0<?> getUserIdentityMethod =
                ReflectUtil.bindInstanceR0(eventNotificationRecord, "getUserIdentity", true, userIdentityClass);
        jsonObject.put("userIdentity", serializeUserIdentity(getUserIdentityMethod.call()));
        return jsonObject;
    }

    /**
     * deserialize an event notification record
     * NOTE: Yes there are a lot of generics. They are needed for the compiler to correctly associate instance types
     * with class types
     * @param jsonObject JSONObject to deserialize from
     * @param eventNotificationRecordClass event notification record class
     * @param s3EntityClass s3 entity class
     * @param s3BucketClass s3 bucket class
     * @param s3ObjectClass s3 object class
     * @param requestParametersClass request parameters class
     * @param responseElementsClass response elements class
     * @param userIdentityClass user identity class
     * @param <A> event notification record type
     * @param <B> s3 entity type
     * @param <C> s3 bucket type
     * @param <D> s3 object type
     * @param <E> request parameters type
     * @param <F> response elements type
     * @param <G> user identity class
     * @return event notification record object
     */
    private <A, B, C, D, E, F, G> A deserializeEventNotificationRecord(JSONObject jsonObject,
                                                                       Class<A> eventNotificationRecordClass,
                                                                       Class<B> s3EntityClass,
                                                                       Class<C> s3BucketClass,
                                                                       Class<D> s3ObjectClass,
                                                                       Class<E> requestParametersClass,
                                                                       Class<F> responseElementsClass,
                                                                       Class<G> userIdentityClass) {
        if (jsonObject == null) {
            jsonObject = new JSONObject();
        }
        String awsRegion = jsonObject.optString("awsRegion");
        String eventName = jsonObject.optString("eventName");
        String eventSource = jsonObject.optString("eventSource");
        String eventTime = jsonObject.optString("eventTime");
        String eventVersion = jsonObject.optString("eventVersion");
        E requestParameters = deserializeRequestParameters(jsonObject.optJSONObject("requestParameters"), requestParametersClass);
        F responseElements = deserializeResponseElements(jsonObject.optJSONObject("responseElements"), responseElementsClass);
        B s3 = deserializeS3Entity(jsonObject.optJSONObject("s3"), s3EntityClass, s3BucketClass, s3ObjectClass, userIdentityClass);
        G userIdentity = deserializeUserIdentity(jsonObject.optJSONObject("userIdentity"), userIdentityClass);
        Functions.R9<A, String, String, String, String, String, E, F, B, G> constructor =
                ReflectUtil.loadConstuctor9(eventNotificationRecordClass, true, String.class, String.class,
                        String.class, String.class, String.class, requestParametersClass, responseElementsClass,
                        s3EntityClass, userIdentityClass);
        return constructor.call(awsRegion, eventName, eventSource, eventTime, eventVersion, requestParameters,
                responseElements, s3, userIdentity);
    }

    /**
     * serialize an s3 entity
     * @param s3Entity S3 entity object
     * @param <A> S3 entity type
     * @return JSONObject with serialized s3 entity
     */
    private <A> JSONObject serializeS3Entity(A s3Entity, String baseClassName) {
        Class s3BucketClass = SerializeUtil.loadCustomerClass(baseClassName + "$S3BucketEntity", classLoader);
        Class s3ObjectClass = SerializeUtil.loadCustomerClass(baseClassName + "$S3ObjectEntity", classLoader);
        JSONObject jsonObject = new JSONObject();
        Functions.R0<String> getConfigurationIdMethod =
                ReflectUtil.bindInstanceR0(s3Entity, "getConfigurationId", true, String.class);
        jsonObject.put("configurationId", getConfigurationIdMethod.call());
        Functions.R0<?> getBucketMethod =
                ReflectUtil.bindInstanceR0(s3Entity, "getBucket", true, s3BucketClass);
        jsonObject.put("bucket", serializeS3Bucket(getBucketMethod.call(), baseClassName));
        Functions.R0<?> getObjectMethod =
                ReflectUtil.bindInstanceR0(s3Entity, "getObject", true, s3ObjectClass);
        jsonObject.put("object", serializeS3Object(getObjectMethod.call()));
        Functions.R0<String> getSchemaVersionMethod =
                ReflectUtil.bindInstanceR0(s3Entity, "getS3SchemaVersion", true, String.class);
        jsonObject.put("s3SchemaVersion", getSchemaVersionMethod.call());
        return jsonObject;
    }

    /**
     * deserialize an S3 entity object
     * @param jsonObject json object to deserialize from
     * @param s3EntityClass s3 entity class
     * @param s3BucketClass s3 bucket class
     * @param s3ObjectClass s3 object class
     * @param userIdentityClass s3 user identity class
     * @param <A> s3 entity type
     * @param <B> s3 bucket type
     * @param <C> s3 object type
     * @param <D> s3 user identity type
     * @return s3 entity object
     */
    private <A, B, C, D> A deserializeS3Entity(JSONObject jsonObject, Class<A> s3EntityClass, Class<B> s3BucketClass,
                                               Class<C> s3ObjectClass, Class<D> userIdentityClass) {
        if (jsonObject == null) {
            jsonObject = new JSONObject();
        }
        String configurationId = jsonObject.optString("configurationId");
        B bucket = deserializeS3Bucket(jsonObject.optJSONObject("bucket"), s3BucketClass, userIdentityClass);
        C object = deserializeS3Object(jsonObject.optJSONObject("object"), s3ObjectClass);
        String schemaVersion = jsonObject.optString("s3SchemaVersion");
        Functions.R4<A, String, B, C, String> constructor =
                ReflectUtil.loadConstuctor4(s3EntityClass, true, String.class, s3BucketClass, s3ObjectClass, String.class);
        return constructor.call(configurationId, bucket, object, schemaVersion);
    }

    /**
     * serialize an s3 bucket object
     * @param s3Bucket S3 bucket object
     * @param <A> S3 bucket type
     * @return JSONObject
     */
    private <A> JSONObject serializeS3Bucket(A s3Bucket, String baseClassName) {
        Class userIdentityClass = SerializeUtil.loadCustomerClass(baseClassName + "$UserIdentityEntity", classLoader);
        JSONObject jsonObject = new JSONObject();
        Functions.R0<String> getNameMethod =
                ReflectUtil.bindInstanceR0(s3Bucket, "getName", true, String.class);
        jsonObject.put("name", getNameMethod.call());
        Functions.R0<?> getOwnerIdentityMethod =
                ReflectUtil.bindInstanceR0(s3Bucket, "getOwnerIdentity", true, userIdentityClass);
        jsonObject.put("ownerIdentity", serializeUserIdentity(getOwnerIdentityMethod.call()));
        Functions.R0<String> getArnMethod = ReflectUtil.bindInstanceR0(s3Bucket, "getArn", true, String.class);
        jsonObject.put("arn", getArnMethod.call());
        return jsonObject;
    }

    /**
     * deserialize an s3 bucket object
     * @param jsonObject JSONObject to deserialize from
     * @param s3BucketClass S3Bucket class
     * @param userIdentityClass user identity class
     * @param <A> s3 bucket type
     * @param <B> user identity type
     * @return s3 bucket object
     */
    private <A, B> A deserializeS3Bucket(JSONObject jsonObject, Class<A> s3BucketClass, Class<B> userIdentityClass) {
        if (jsonObject == null) {
            jsonObject = new JSONObject();
        }
        String name = jsonObject.optString("name");
        B ownerIdentity = deserializeUserIdentity(jsonObject.optJSONObject("ownerIdentity"), userIdentityClass);
        String arn = jsonObject.optString("arn");
        Functions.R3<A, String, B, String> constructor =
                ReflectUtil.loadConstuctor3(s3BucketClass, true, String.class, userIdentityClass, String.class);
        return constructor.call(name, ownerIdentity, arn);
    }

    /**
     * serialize an s3 object
     * @param s3Object s3Object object
     * @param <A> s3 object type
     * @return s3Object object
     */
    private <A> JSONObject serializeS3Object(A s3Object) {
        JSONObject jsonObject = new JSONObject();
        Functions.R0<String> getKeyMethod =
                ReflectUtil.bindInstanceR0(s3Object, "getKey", true, String.class);
        jsonObject.put("key", getKeyMethod.call());
        Functions.R0<Long> getSizeMethod =
                ReflectUtil.bindInstanceR0(s3Object, "getSizeAsLong", true, Long.class);
        jsonObject.put("size", getSizeMethod.call().longValue());
        Functions.R0<String> getETagMethod =
                ReflectUtil.bindInstanceR0(s3Object, "geteTag", true, String.class);
        jsonObject.put("eTag", getETagMethod.call());
        Functions.R0<String> getVersionIdMethod =
                ReflectUtil.bindInstanceR0(s3Object, "getVersionId", true, String.class);
        jsonObject.put("versionId", getVersionIdMethod.call());
        // legacy s3 event models do not have these methods
        try {
            Functions.R0<String> getUrlEncodedKeyMethod =
                    ReflectUtil.bindInstanceR0(s3Object,  "getUrlDecodedKey", true, String.class);
            jsonObject.put("urlDecodedKey", getUrlEncodedKeyMethod.call());
            Functions.R0<String> getSequencerMethod =
                    ReflectUtil.bindInstanceR0(s3Object, "getSequencer", true, String.class);
            jsonObject.put("sequencer", getSequencerMethod.call());
        } catch (Exception ignored) {}
        return jsonObject;
    }

    /**
     * deserialize an s3Object
     * @param jsonObject json object to deserialize from
     * @param s3ObjectClass class of s3Object
     * @param <A> s3Object type
     * @return s3Object object
     */
    private <A> A deserializeS3Object(JSONObject jsonObject, Class<A> s3ObjectClass) {
        if (jsonObject == null) {
            jsonObject = new JSONObject();
        }
        String key = jsonObject.optString("key");
        Long size = jsonObject.optLong("size");
        String eTag = jsonObject.optString("eTag");
        String versionId = jsonObject.optString("versionId");
        String sequencer = jsonObject.optString("sequencer");
        // legacy s3 event uses constructor in catch statement
        try {
            Functions.R5<A, String, Long, String, String, String> constructor =
                    ReflectUtil.loadConstuctor5(s3ObjectClass, true, String.class, Long.class, String.class, String.class, String.class);
            return constructor.call(key, size, eTag, versionId, sequencer);
        } catch (Exception e) {
            Functions.R4<A, String, Long, String, String> constructor =
                    ReflectUtil.loadConstuctor4(s3ObjectClass, true, String.class, Long.class, String.class, String.class);
            return constructor.call(key, size, eTag, versionId);
        }
    }

    /**
     * serialize an s3 user identity
     * @param userIdentity user identity object
     * @param <A> user identity type
     * @return JSONObject with serialized user identity
     */
    private <A> JSONObject serializeUserIdentity(A userIdentity) {
        JSONObject jsonObject = new JSONObject();
        Functions.R0<String> getPrincipalIdMethod =
                ReflectUtil.bindInstanceR0(userIdentity, "getPrincipalId", true, String.class);
        jsonObject.put("principalId", getPrincipalIdMethod.call());
        return jsonObject;
    }

    /**
     * deserialize a user identity
     * @param jsonObject JSONObject to deserialize from
     * @param userIdentityClass User Identity Class
     * @param <A> User Identity Type
     * @return User Identity Object
     */
    private <A> A deserializeUserIdentity(JSONObject jsonObject, Class<A> userIdentityClass) {
        if (jsonObject == null) {
            jsonObject = new JSONObject();
        }
        String principalId = jsonObject.optString("principalId");
        Functions.R1<A, String> constructor =
                ReflectUtil.loadConstructor1(userIdentityClass, true, String.class);
        return constructor.call(principalId);
    }

    /**
     * serialize request parameters
     * @param requestParameters request parameters object
     * @param <A> request parameters type
     * @return JSONObject with serialized request parameters
     */
    private <A> JSONObject serializeRequestParameters(A requestParameters) {
        JSONObject jsonObject = new JSONObject();
        Functions.R0<String> getSourceIpMethod =
                ReflectUtil.bindInstanceR0(requestParameters, "getSourceIPAddress", true, String.class);
        jsonObject.put("sourceIPAddress", getSourceIpMethod.call());
        return jsonObject;
    }

    /**
     * deserialize request parameters
     * @param jsonObject JSONObject to deserialize from
     * @param requestParametersClass RequestParameters class
     * @param <A> RequestParameters type
     * @return RequestParameters object
     */
    private <A> A deserializeRequestParameters(JSONObject jsonObject, Class<A> requestParametersClass) {
        if (jsonObject == null) {
            jsonObject = new JSONObject();
        }
        String sourceIpAddress = jsonObject.optString("sourceIPAddress");
        Functions.R1<A, String> constructor = ReflectUtil.loadConstructor1(requestParametersClass, true, String.class);
        return constructor.call(sourceIpAddress);
    }

    /**
     * serialize response elements object
     * @param responseElements response elements object
     * @param <A> response elements type
     * @return JSONObject with serialized responseElements
     */
    private <A> JSONObject serializeResponseElements(A responseElements) {
        JSONObject jsonObject = new JSONObject();
        Functions.R0<String> getXAmzId2Method =
                ReflectUtil.bindInstanceR0(responseElements, "getxAmzId2", true, String.class);
        jsonObject.put("x-amz-id-2", getXAmzId2Method.call());
        Functions.R0<String> getXAmzRequestId =
                ReflectUtil.bindInstanceR0(responseElements, "getxAmzRequestId", true, String.class);
        jsonObject.put("x-amz-request-id", getXAmzRequestId.call());
        return jsonObject;
    }

    /**
     * deserialize response elements
     * @param jsonObject JSONObject deserializing from
     * @param responseElementsClass response elements class
     * @param <A> response elements type
     * @return Response elements object
     */
    private <A> A deserializeResponseElements(JSONObject jsonObject, Class<A> responseElementsClass) {
        if (jsonObject == null) {
            jsonObject = new JSONObject();
        }
        String xAmzId2 = jsonObject.optString("x-amz-id-2");
        String xAmzRequestId = jsonObject.optString("x-amz-request-id");
        Functions.R2<A, String, String> constructor =
                ReflectUtil.loadConstructor2(responseElementsClass, true, String.class, String.class);
        return constructor.call(xAmzId2, xAmzRequestId);
    }

}
