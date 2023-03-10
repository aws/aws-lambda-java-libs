/* Copyright 2019 Amazon.com, Inc. or its affiliates. All Rights Reserved. */

package com.amazonaws.services.lambda.runtime.serialization.util;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Scanner;

/**
 * Class with Utilities for serializing and deserializing customer classes
 */
public class SerializeUtil {

    /**
     * cached of classes being loaded for faster reflect loading
     */
    private static final HashMap<String, Class> cachedClasses = new HashMap<>();

    /**
     * converts an input stream to a string
     * @param inputStream InputStream object
     * @return String with stream contents
     */
    public static String convertStreamToString(InputStream inputStream) {
        Scanner s = new Scanner(inputStream).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }

    /**
     * load a customer class
     * @param className name of class to load
     * @return Class object
     */
    public static Class loadCustomerClass(String className, ClassLoader customerClassLoader) {
        Class cachedClass = cachedClasses.get(className);
        if (cachedClass == null) {
            cachedClass = ReflectUtil.loadClass(customerClassLoader, className);
            cachedClasses.put(className, cachedClass);
        }
        return cachedClass;
    }

    /**
     * deserialize a joda datetime object
     * Underneath the reflection, this method does the following:
     *
     * DateTime.parse(jsonParser.getValueAsString());
     *
     * @param dateTimeClass DateTime class
     * @param dateTimeString string to deserialize from
     * @param <T> DateTime type
     * @return DateTime instance
     */
    public static <T> T deserializeDateTime(Class<T> dateTimeClass, String dateTimeString) {
        Functions.R1<T, String> parseMethod =
                ReflectUtil.loadStaticR1(dateTimeClass, "parse", true, dateTimeClass, String.class);
        return parseMethod.call(dateTimeString);
    }

    /**
     * serialize a DateTime object
     * Underneath the reflection, this method does the following:
     *
     * DateTimeFormatter formatter = ISODateTimeFormat.dateTime();
     * jsonGenerator.writeString(formatter.print(customerDateTime)
     *
     * @param dateTime DateTime object to serialize
     * @param <T> DateTime type
     * @param classLoader ClassLoader used to load DateTime classes
     * @return timestamp as formatted string
     */
    @SuppressWarnings({"unchecked"})
    public static <T> String serializeDateTime(T dateTime, ClassLoader classLoader) {
        // Workaround not to let maven shade plugin relocating string literals https://issues.apache.org/jira/browse/MSHADE-156
        Class dateTimeFormatterClass = loadCustomerClass("com.amazonaws.lambda.unshade.thirdparty.org.joda.time.format.DateTimeFormatter", classLoader);
        Class dateTimeFormatClass = loadCustomerClass("com.amazonaws.lambda.unshade.thirdparty.org.joda.time.format.ISODateTimeFormat", classLoader);
        Class readableInstantInterface = loadCustomerClass("com.amazonaws.lambda.unshade.thirdparty.org.joda.time.ReadableInstant", classLoader);
        return serializeDateTimeHelper(dateTime, dateTimeFormatterClass, dateTimeFormatClass, readableInstantInterface);
    }

    /**
     * Helper method to serialize DateTime objects (We need some way to define generics to get code to compile)
     * @param dateTime DAteTime object
     * @param dateTimeFormatterClass DAteTime formatter class
     * @param dateTimeFormatClass DateTime ISO format class
     * @param readableInstantInterface DateTime readable instant interface (Needed because reflection is type specific)
     * @param <S> DAteTime type
     * @param <T> DateTimeFormatter type
     * @param <U> DateTimeFormat type
     * @param <V> ReadableInstant type
     * @return String with serialized date time
     */
    private static <S extends V, T, U, V> String serializeDateTimeHelper(S dateTime, Class<T> dateTimeFormatterClass,
                                                                         Class<U> dateTimeFormatClass,
                                                                         Class<V> readableInstantInterface) {
        Functions.R0<T> dateTimeFormatterConstructor =
                ReflectUtil.loadStaticR0(dateTimeFormatClass, "dateTime", true, dateTimeFormatterClass);
        T dateTimeFormatter = dateTimeFormatterConstructor.call();
        Functions.R1<String, S> printMethod =
                ReflectUtil.bindInstanceR1(dateTimeFormatter, "print", true, String.class, readableInstantInterface);
        return printMethod.call(dateTime);
    }

}
