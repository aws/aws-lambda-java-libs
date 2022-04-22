/*
 * Copyright 2022 Amazon.com, Inc. or its affiliates. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file except in compliance with
 * the License. A copy of the License is located at
 *
 * http://aws.amazon.com/apache2.0
 *
 * or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR
 * CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions
 * and limitations under the License.
 */

package com.amazonaws.services.lambda.runtime.events.dynamodb;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Builder;
import lombok.Data;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;

/**
 * Represents an Amazon DynamoDB event
 */
@Data
@Builder(setterPrefix = "with")
@NoArgsConstructor
@AllArgsConstructor
public class DynamodbEvent implements Serializable, Cloneable {

    private static final long serialVersionUID = -2354616079899981231L;

    @JsonProperty("Records")
    private List<DynamodbStreamRecord> records;

    /**
     * The unit of data of an Amazon DynamoDB event
     */
    @Data
    @Builder(setterPrefix = "with")
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DynamodbStreamRecord implements Serializable, Cloneable {

        private static final long serialVersionUID = 3638381544604354963L;

        private String eventSourceARN;

        /**
         * <p>
         * A globally unique identifier for the event that was recorded in this stream record.
         * </p>
         */
        private String eventID;
        /**
         * <p>
         * The type of data modification that was performed on the DynamoDB table:
         * </p>
         * <ul>
         * <li>
         * <p>
         * <code>INSERT</code> - a new item was added to the table.
         * </p>
         * </li>
         * <li>
         * <p>
         * <code>MODIFY</code> - one or more of an existing item's attributes were modified.
         * </p>
         * </li>
         * <li>
         * <p>
         * <code>REMOVE</code> - the item was deleted from the table
         * </p>
         * </li>
         * </ul>
         */
        private String eventName;
        /**
         * <p>
         * The version number of the stream record format. This number is updated whenever the structure of
         * <code>Record</code> is modified.
         * </p>
         * <p>
         * Client applications must not assume that <code>eventVersion</code> will remain at a particular value, as this
         * number is subject to change at any time. In general, <code>eventVersion</code> will only increase as the
         * low-level DynamoDB Streams API evolves.
         * </p>
         */
        private String eventVersion;
        /**
         * <p>
         * The AWS service from which the stream record originated. For DynamoDB Streams, this is <code>aws:dynamodb</code>.
         * </p>
         */
        private String eventSource;
        /**
         * <p>
         * The region in which the <code>GetRecords</code> request was received.
         * </p>
         */
        private String awsRegion;
        /**
         * <p>
         * The main body of the stream record, containing all of the DynamoDB-specific fields.
         * </p>
         */
        private StreamRecord dynamodb;
        /**
         * <p>
         * Items that are deleted by the Time to Live process after expiration have the following fields:
         * </p>
         * <ul>
         * <li>
         * <p>
         * Records[].userIdentity.type
         * </p>
         * <p>
         * "Service"
         * </p>
         * </li>
         * <li>
         * <p>
         * Records[].userIdentity.principalId
         * </p>
         * <p>
         * "dynamodb.amazonaws.com"
         * </p>
         * </li>
         * </ul>
         */
        private Identity userIdentity;

        /**
         * <p>
         * The type of data modification that was performed on the DynamoDB table:
         * </p>
         * <ul>
         * <li>
         * <p>
         * <code>INSERT</code> - a new item was added to the table.
         * </p>
         * </li>
         * <li>
         * <p>
         * <code>MODIFY</code> - one or more of an existing item's attributes were modified.
         * </p>
         * </li>
         * <li>
         * <p>
         * <code>REMOVE</code> - the item was deleted from the table
         * </p>
         * </li>
         * </ul>
         *
         * @param eventName
         *        The type of data modification that was performed on the DynamoDB table:</p>
         *        <ul>
         *        <li>
         *        <p>
         *        <code>INSERT</code> - a new item was added to the table.
         *        </p>
         *        </li>
         *        <li>
         *        <p>
         *        <code>MODIFY</code> - one or more of an existing item's attributes were modified.
         *        </p>
         *        </li>
         *        <li>
         *        <p>
         *        <code>REMOVE</code> - the item was deleted from the table
         *        </p>
         *        </li>
         * @see OperationType
         */
        public void setEventName(String eventName) {
            this.eventName = eventName;
        }

        /**
         * <p>
         * The type of data modification that was performed on the DynamoDB table:
         * </p>
         * <ul>
         * <li>
         * <p>
         * <code>INSERT</code> - a new item was added to the table.
         * </p>
         * </li>
         * <li>
         * <p>
         * <code>MODIFY</code> - one or more of an existing item's attributes were modified.
         * </p>
         * </li>
         * <li>
         * <p>
         * <code>REMOVE</code> - the item was deleted from the table
         * </p>
         * </li>
         * </ul>
         *
         * @param eventName
         *        The type of data modification that was performed on the DynamoDB table:</p>
         *        <ul>
         *        <li>
         *        <p>
         *        <code>INSERT</code> - a new item was added to the table.
         *        </p>
         *        </li>
         *        <li>
         *        <p>
         *        <code>MODIFY</code> - one or more of an existing item's attributes were modified.
         *        </p>
         *        </li>
         *        <li>
         *        <p>
         *        <code>REMOVE</code> - the item was deleted from the table
         *        </p>
         *        </li>
         * @see OperationType
         */
        public void setEventName(OperationType eventName) {
            this.eventName = eventName.toString();
        }

        /**
         * <p>
         * Represents the data for an attribute.
         * </p>
         * <p>
         * Each attribute value is described as a name-value pair. The name is the data type, and the value is the data itself.
         * </p>
         * <p>
         * For more information, see <a href=
         * "https://docs.aws.amazon.com/amazondynamodb/latest/developerguide/HowItWorks.NamingRulesDataTypes.html#HowItWorks.DataTypes"
         * >Data Types</a> in the <i>Amazon DynamoDB Developer Guide</i>.
         * </p>
         *
         * @see <a href="http://docs.aws.amazon.com/goto/WebAPI/dynamodb-2012-08-10/AttributeValue" target="_top">AWS API
         *      Documentation</a>
         */
        @NoArgsConstructor
        @ToString
        @EqualsAndHashCode
        public static class AttributeValue implements Serializable, Cloneable {

            /**
             * <p>
             * An attribute of type String. For example:
             * </p>
             * <p>
             * <code>"S": "Hello"</code>
             * </p>
             */
            @JsonProperty("S")
            private String s;
            /**
             * <p>
             * An attribute of type Number. For example:
             * </p>
             * <p>
             * <code>"N": "123.45"</code>
             * </p>
             * <p>
             * Numbers are sent across the network to DynamoDB as strings, to maximize compatibility across languages and
             * libraries. However, DynamoDB treats them as number type attributes for mathematical operations.
             * </p>
             */
            @JsonProperty("N")
            private String n;
            /**
             * <p>
             * An attribute of type Binary. For example:
             * </p>
             * <p>
             * <code>"B": "dGhpcyB0ZXh0IGlzIGJhc2U2NC1lbmNvZGVk"</code>
             * </p>
             */
            @JsonProperty("B")
            private java.nio.ByteBuffer b;
            /**
             * <p>
             * An attribute of type String Set. For example:
             * </p>
             * <p>
             * <code>"SS": ["Giraffe", "Hippo" ,"Zebra"]</code>
             * </p>
             */
            @JsonProperty("SS")
            private java.util.List<String> sS;
            /**
             * <p>
             * An attribute of type Number Set. For example:
             * </p>
             * <p>
             * <code>"NS": ["42.2", "-19", "7.5", "3.14"]</code>
             * </p>
             * <p>
             * Numbers are sent across the network to DynamoDB as strings, to maximize compatibility across languages and
             * libraries. However, DynamoDB treats them as number type attributes for mathematical operations.
             * </p>
             */
            @JsonProperty("NS")
            private java.util.List<String> nS;
            /**
             * <p>
             * An attribute of type Binary Set. For example:
             * </p>
             * <p>
             * <code>"BS": ["U3Vubnk=", "UmFpbnk=", "U25vd3k="]</code>
             * </p>
             */
            @JsonProperty("BS")
            private java.util.List<java.nio.ByteBuffer> bS;
            /**
             * <p>
             * An attribute of type Map. For example:
             * </p>
             * <p>
             * <code>"M": {"Name": {"S": "Joe"}, "Age": {"N": "35"}}</code>
             * </p>
             */
            @JsonProperty("M")
            private java.util.Map<String, AttributeValue> m;
            /**
             * <p>
             * An attribute of type List. For example:
             * </p>
             * <p>
             * <code>"L": [ {"S": "Cookies"} , {"S": "Coffee"}, {"N", "3.14159"}]</code>
             * </p>
             */
            @JsonProperty("L")
            private java.util.List<AttributeValue> l;
            /**
             * <p>
             * An attribute of type Null. For example:
             * </p>
             * <p>
             * <code>"NULL": true</code>
             * </p>
             */
            @JsonProperty("NULL")
            private Boolean nULLValue;
            /**
             * <p>
             * An attribute of type Boolean. For example:
             * </p>
             * <p>
             * <code>"BOOL": true</code>
             * </p>
             */
            @JsonProperty("BOOL")
            private Boolean bOOL;

            /**
             * Constructs a new DynamodbAttributeValue object. Callers should use the setter or fluent setter (with...) methods to
             * initialize any additional object members.
             *
             * @param s
             *        An attribute of type String. For example:</p>
             *        <p>
             *        <code>"S": "Hello"</code>
             */
            public AttributeValue(String s) {
                setS(s);
            }

            /**
             * Constructs a new DynamodbAttributeValue object. Callers should use the setter or fluent setter (with...) methods to
             * initialize any additional object members.
             *
             * @param sS
             *        An attribute of type String Set. For example:</p>
             *        <p>
             *        <code>"SS": ["Giraffe", "Hippo" ,"Zebra"]</code>
             */
            public AttributeValue(java.util.List<String> sS) {
                setSS(sS);
            }

            /**
             * <p>
             * An attribute of type String. For example:
             * </p>
             * <p>
             * <code>"S": "Hello"</code>
             * </p>
             *
             * @param s
             *        An attribute of type String. For example:</p>
             *        <p>
             *        <code>"S": "Hello"</code>
             */

            public void setS(String s) {
                this.s = s;
            }

            /**
             * <p>
             * An attribute of type String. For example:
             * </p>
             * <p>
             * <code>"S": "Hello"</code>
             * </p>
             *
             * @return An attribute of type String. For example:</p>
             *         <p>
             *         <code>"S": "Hello"</code>
             */

            public String getS() {
                return this.s;
            }

            /**
             * <p>
             * An attribute of type String. For example:
             * </p>
             * <p>
             * <code>"S": "Hello"</code>
             * </p>
             *
             * @param s
             *        An attribute of type String. For example:</p>
             *        <p>
             *        <code>"S": "Hello"</code>
             * @return Returns a reference to this object so that method calls can be chained together.
             */

            public AttributeValue withS(String s) {
                setS(s);
                return this;
            }

            /**
             * <p>
             * An attribute of type Number. For example:
             * </p>
             * <p>
             * <code>"N": "123.45"</code>
             * </p>
             * <p>
             * Numbers are sent across the network to DynamoDB as strings, to maximize compatibility across languages and
             * libraries. However, DynamoDB treats them as number type attributes for mathematical operations.
             * </p>
             *
             * @param n
             *        An attribute of type Number. For example:</p>
             *        <p>
             *        <code>"N": "123.45"</code>
             *        </p>
             *        <p>
             *        Numbers are sent across the network to DynamoDB as strings, to maximize compatibility across languages and
             *        libraries. However, DynamoDB treats them as number type attributes for mathematical operations.
             */

            public void setN(String n) {
                this.n = n;
            }

            /**
             * <p>
             * An attribute of type Number. For example:
             * </p>
             * <p>
             * <code>"N": "123.45"</code>
             * </p>
             * <p>
             * Numbers are sent across the network to DynamoDB as strings, to maximize compatibility across languages and
             * libraries. However, DynamoDB treats them as number type attributes for mathematical operations.
             * </p>
             *
             * @return An attribute of type Number. For example:</p>
             *         <p>
             *         <code>"N": "123.45"</code>
             *         </p>
             *         <p>
             *         Numbers are sent across the network to DynamoDB as strings, to maximize compatibility across languages
             *         and libraries. However, DynamoDB treats them as number type attributes for mathematical operations.
             */

            public String getN() {
                return this.n;
            }

            /**
             * <p>
             * An attribute of type Number. For example:
             * </p>
             * <p>
             * <code>"N": "123.45"</code>
             * </p>
             * <p>
             * Numbers are sent across the network to DynamoDB as strings, to maximize compatibility across languages and
             * libraries. However, DynamoDB treats them as number type attributes for mathematical operations.
             * </p>
             *
             * @param n
             *        An attribute of type Number. For example:</p>
             *        <p>
             *        <code>"N": "123.45"</code>
             *        </p>
             *        <p>
             *        Numbers are sent across the network to DynamoDB as strings, to maximize compatibility across languages and
             *        libraries. However, DynamoDB treats them as number type attributes for mathematical operations.
             * @return Returns a reference to this object so that method calls can be chained together.
             */

            public AttributeValue withN(String n) {
                setN(n);
                return this;
            }

            /**
             * <p>
             * An attribute of type Binary. For example:
             * </p>
             * <p>
             * <code>"B": "dGhpcyB0ZXh0IGlzIGJhc2U2NC1lbmNvZGVk"</code>
             * </p>
             * <p>
             * The AWS SDK for Java performs a Base64 encoding on this field before sending this request to the AWS service.
             * Users of the SDK should not perform Base64 encoding on this field.
             * </p>
             * <p>
             * Warning: ByteBuffers returned by the SDK are mutable. Changes to the content or position of the byte buffer will
             * be seen by all objects that have a reference to this object. It is recommended to call ByteBuffer.duplicate() or
             * ByteBuffer.asReadOnlyBuffer() before using or reading from the buffer. This behavior will be changed in a future
             * major version of the SDK.
             * </p>
             *
             * @param b
             *        An attribute of type Binary. For example:</p>
             *        <p>
             *        <code>"B": "dGhpcyB0ZXh0IGlzIGJhc2U2NC1lbmNvZGVk"</code>
             */

            public void setB(java.nio.ByteBuffer b) {
                this.b = b;
            }

            /**
             * <p>
             * An attribute of type Binary. For example:
             * </p>
             * <p>
             * <code>"B": "dGhpcyB0ZXh0IGlzIGJhc2U2NC1lbmNvZGVk"</code>
             * </p>
             * <p>
             * {@code ByteBuffer}s are stateful. Calling their {@code get} methods changes their {@code position}. We recommend
             * using {@link java.nio.ByteBuffer#asReadOnlyBuffer()} to create a read-only view of the buffer with an independent
             * {@code position}, and calling {@code get} methods on this rather than directly on the returned {@code ByteBuffer}.
             * Doing so will ensure that anyone else using the {@code ByteBuffer} will not be affected by changes to the
             * {@code position}.
             * </p>
             *
             * @return An attribute of type Binary. For example:</p>
             *         <p>
             *         <code>"B": "dGhpcyB0ZXh0IGlzIGJhc2U2NC1lbmNvZGVk"</code>
             */

            public java.nio.ByteBuffer getB() {
                return this.b;
            }

            /**
             * <p>
             * An attribute of type Binary. For example:
             * </p>
             * <p>
             * <code>"B": "dGhpcyB0ZXh0IGlzIGJhc2U2NC1lbmNvZGVk"</code>
             * </p>
             * <p>
             * The AWS SDK for Java performs a Base64 encoding on this field before sending this request to the AWS service.
             * Users of the SDK should not perform Base64 encoding on this field.
             * </p>
             * <p>
             * Warning: ByteBuffers returned by the SDK are mutable. Changes to the content or position of the byte buffer will
             * be seen by all objects that have a reference to this object. It is recommended to call ByteBuffer.duplicate() or
             * ByteBuffer.asReadOnlyBuffer() before using or reading from the buffer. This behavior will be changed in a future
             * major version of the SDK.
             * </p>
             *
             * @param b
             *        An attribute of type Binary. For example:</p>
             *        <p>
             *        <code>"B": "dGhpcyB0ZXh0IGlzIGJhc2U2NC1lbmNvZGVk"</code>
             * @return Returns a reference to this object so that method calls can be chained together.
             */

            public AttributeValue withB(java.nio.ByteBuffer b) {
                setB(b);
                return this;
            }

            /**
             * <p>
             * An attribute of type String Set. For example:
             * </p>
             * <p>
             * <code>"SS": ["Giraffe", "Hippo" ,"Zebra"]</code>
             * </p>
             *
             * @return An attribute of type String Set. For example:</p>
             *         <p>
             *         <code>"SS": ["Giraffe", "Hippo" ,"Zebra"]</code>
             */

            public java.util.List<String> getSS() {
                return sS;
            }

            /**
             * <p>
             * An attribute of type String Set. For example:
             * </p>
             * <p>
             * <code>"SS": ["Giraffe", "Hippo" ,"Zebra"]</code>
             * </p>
             *
             * @param sS
             *        An attribute of type String Set. For example:</p>
             *        <p>
             *        <code>"SS": ["Giraffe", "Hippo" ,"Zebra"]</code>
             */

            public void setSS(java.util.Collection<String> sS) {
                if (sS == null) {
                    this.sS = null;
                    return;
                }

                this.sS = new java.util.ArrayList<String>(sS);
            }

            /**
             * <p>
             * An attribute of type String Set. For example:
             * </p>
             * <p>
             * <code>"SS": ["Giraffe", "Hippo" ,"Zebra"]</code>
             * </p>
             * <p>
             * <b>NOTE:</b> This method appends the values to the existing list (if any). Use
             * {@link #setSS(java.util.Collection)} or {@link #withSS(java.util.Collection)} if you want to override the
             * existing values.
             * </p>
             *
             * @param sS
             *        An attribute of type String Set. For example:</p>
             *        <p>
             *        <code>"SS": ["Giraffe", "Hippo" ,"Zebra"]</code>
             * @return Returns a reference to this object so that method calls can be chained together.
             */

            public AttributeValue withSS(String... sS) {
                if (this.sS == null) {
                    setSS(new java.util.ArrayList<String>(sS.length));
                }
                for (String ele : sS) {
                    this.sS.add(ele);
                }
                return this;
            }

            /**
             * <p>
             * An attribute of type String Set. For example:
             * </p>
             * <p>
             * <code>"SS": ["Giraffe", "Hippo" ,"Zebra"]</code>
             * </p>
             *
             * @param sS
             *        An attribute of type String Set. For example:</p>
             *        <p>
             *        <code>"SS": ["Giraffe", "Hippo" ,"Zebra"]</code>
             * @return Returns a reference to this object so that method calls can be chained together.
             */

            public AttributeValue withSS(java.util.Collection<String> sS) {
                setSS(sS);
                return this;
            }

            /**
             * <p>
             * An attribute of type Number Set. For example:
             * </p>
             * <p>
             * <code>"NS": ["42.2", "-19", "7.5", "3.14"]</code>
             * </p>
             * <p>
             * Numbers are sent across the network to DynamoDB as strings, to maximize compatibility across languages and
             * libraries. However, DynamoDB treats them as number type attributes for mathematical operations.
             * </p>
             *
             * @return An attribute of type Number Set. For example:</p>
             *         <p>
             *         <code>"NS": ["42.2", "-19", "7.5", "3.14"]</code>
             *         </p>
             *         <p>
             *         Numbers are sent across the network to DynamoDB as strings, to maximize compatibility across languages
             *         and libraries. However, DynamoDB treats them as number type attributes for mathematical operations.
             */

            public java.util.List<String> getNS() {
                return nS;
            }

            /**
             * <p>
             * An attribute of type Number Set. For example:
             * </p>
             * <p>
             * <code>"NS": ["42.2", "-19", "7.5", "3.14"]</code>
             * </p>
             * <p>
             * Numbers are sent across the network to DynamoDB as strings, to maximize compatibility across languages and
             * libraries. However, DynamoDB treats them as number type attributes for mathematical operations.
             * </p>
             *
             * @param nS
             *        An attribute of type Number Set. For example:</p>
             *        <p>
             *        <code>"NS": ["42.2", "-19", "7.5", "3.14"]</code>
             *        </p>
             *        <p>
             *        Numbers are sent across the network to DynamoDB as strings, to maximize compatibility across languages and
             *        libraries. However, DynamoDB treats them as number type attributes for mathematical operations.
             */

            public void setNS(java.util.Collection<String> nS) {
                if (nS == null) {
                    this.nS = null;
                    return;
                }

                this.nS = new java.util.ArrayList<String>(nS);
            }

            /**
             * <p>
             * An attribute of type Number Set. For example:
             * </p>
             * <p>
             * <code>"NS": ["42.2", "-19", "7.5", "3.14"]</code>
             * </p>
             * <p>
             * Numbers are sent across the network to DynamoDB as strings, to maximize compatibility across languages and
             * libraries. However, DynamoDB treats them as number type attributes for mathematical operations.
             * </p>
             * <p>
             * <b>NOTE:</b> This method appends the values to the existing list (if any). Use
             * {@link #setNS(java.util.Collection)} or {@link #withNS(java.util.Collection)} if you want to override the
             * existing values.
             * </p>
             *
             * @param nS
             *        An attribute of type Number Set. For example:</p>
             *        <p>
             *        <code>"NS": ["42.2", "-19", "7.5", "3.14"]</code>
             *        </p>
             *        <p>
             *        Numbers are sent across the network to DynamoDB as strings, to maximize compatibility across languages and
             *        libraries. However, DynamoDB treats them as number type attributes for mathematical operations.
             * @return Returns a reference to this object so that method calls can be chained together.
             */

            public AttributeValue withNS(String... nS) {
                if (this.nS == null) {
                    setNS(new java.util.ArrayList<String>(nS.length));
                }
                for (String ele : nS) {
                    this.nS.add(ele);
                }
                return this;
            }

            /**
             * <p>
             * An attribute of type Number Set. For example:
             * </p>
             * <p>
             * <code>"NS": ["42.2", "-19", "7.5", "3.14"]</code>
             * </p>
             * <p>
             * Numbers are sent across the network to DynamoDB as strings, to maximize compatibility across languages and
             * libraries. However, DynamoDB treats them as number type attributes for mathematical operations.
             * </p>
             *
             * @param nS
             *        An attribute of type Number Set. For example:</p>
             *        <p>
             *        <code>"NS": ["42.2", "-19", "7.5", "3.14"]</code>
             *        </p>
             *        <p>
             *        Numbers are sent across the network to DynamoDB as strings, to maximize compatibility across languages and
             *        libraries. However, DynamoDB treats them as number type attributes for mathematical operations.
             * @return Returns a reference to this object so that method calls can be chained together.
             */

            public AttributeValue withNS(java.util.Collection<String> nS) {
                setNS(nS);
                return this;
            }

            /**
             * <p>
             * An attribute of type Binary Set. For example:
             * </p>
             * <p>
             * <code>"BS": ["U3Vubnk=", "UmFpbnk=", "U25vd3k="]</code>
             * </p>
             *
             * @return An attribute of type Binary Set. For example:</p>
             *         <p>
             *         <code>"BS": ["U3Vubnk=", "UmFpbnk=", "U25vd3k="]</code>
             */

            public java.util.List<java.nio.ByteBuffer> getBS() {
                return bS;
            }

            /**
             * <p>
             * An attribute of type Binary Set. For example:
             * </p>
             * <p>
             * <code>"BS": ["U3Vubnk=", "UmFpbnk=", "U25vd3k="]</code>
             * </p>
             *
             * @param bS
             *        An attribute of type Binary Set. For example:</p>
             *        <p>
             *        <code>"BS": ["U3Vubnk=", "UmFpbnk=", "U25vd3k="]</code>
             */

            public void setBS(java.util.Collection<java.nio.ByteBuffer> bS) {
                if (bS == null) {
                    this.bS = null;
                    return;
                }

                this.bS = new java.util.ArrayList<java.nio.ByteBuffer>(bS);
            }

            /**
             * <p>
             * An attribute of type Binary Set. For example:
             * </p>
             * <p>
             * <code>"BS": ["U3Vubnk=", "UmFpbnk=", "U25vd3k="]</code>
             * </p>
             * <p>
             * <b>NOTE:</b> This method appends the values to the existing list (if any). Use
             * {@link #setBS(java.util.Collection)} or {@link #withBS(java.util.Collection)} if you want to override the
             * existing values.
             * </p>
             *
             * @param bS
             *        An attribute of type Binary Set. For example:</p>
             *        <p>
             *        <code>"BS": ["U3Vubnk=", "UmFpbnk=", "U25vd3k="]</code>
             * @return Returns a reference to this object so that method calls can be chained together.
             */

            public AttributeValue withBS(java.nio.ByteBuffer... bS) {
                if (this.bS == null) {
                    setBS(new java.util.ArrayList<java.nio.ByteBuffer>(bS.length));
                }
                for (java.nio.ByteBuffer ele : bS) {
                    this.bS.add(ele);
                }
                return this;
            }

            /**
             * <p>
             * An attribute of type Binary Set. For example:
             * </p>
             * <p>
             * <code>"BS": ["U3Vubnk=", "UmFpbnk=", "U25vd3k="]</code>
             * </p>
             *
             * @param bS
             *        An attribute of type Binary Set. For example:</p>
             *        <p>
             *        <code>"BS": ["U3Vubnk=", "UmFpbnk=", "U25vd3k="]</code>
             * @return Returns a reference to this object so that method calls can be chained together.
             */

            public AttributeValue withBS(java.util.Collection<java.nio.ByteBuffer> bS) {
                setBS(bS);
                return this;
            }

            /**
             * <p>
             * An attribute of type Map. For example:
             * </p>
             * <p>
             * <code>"M": {"Name": {"S": "Joe"}, "Age": {"N": "35"}}</code>
             * </p>
             *
             * @return An attribute of type Map. For example:</p>
             *         <p>
             *         <code>"M": {"Name": {"S": "Joe"}, "Age": {"N": "35"}}</code>
             */

            public java.util.Map<String, AttributeValue> getM() {
                return m;
            }

            /**
             * <p>
             * An attribute of type Map. For example:
             * </p>
             * <p>
             * <code>"M": {"Name": {"S": "Joe"}, "Age": {"N": "35"}}</code>
             * </p>
             *
             * @param m
             *        An attribute of type Map. For example:</p>
             *        <p>
             *        <code>"M": {"Name": {"S": "Joe"}, "Age": {"N": "35"}}</code>
             */

            public void setM(java.util.Map<String, AttributeValue> m) {
                this.m = m;
            }

            /**
             * <p>
             * An attribute of type Map. For example:
             * </p>
             * <p>
             * <code>"M": {"Name": {"S": "Joe"}, "Age": {"N": "35"}}</code>
             * </p>
             *
             * @param m
             *        An attribute of type Map. For example:</p>
             *        <p>
             *        <code>"M": {"Name": {"S": "Joe"}, "Age": {"N": "35"}}</code>
             * @return Returns a reference to this object so that method calls can be chained together.
             */

            public AttributeValue withM(java.util.Map<String, AttributeValue> m) {
                setM(m);
                return this;
            }

            public AttributeValue addMEntry(String key, AttributeValue value) {
                if (null == this.m) {
                    this.m = new java.util.HashMap<String, AttributeValue>();
                }
                if (this.m.containsKey(key))
                    throw new IllegalArgumentException("Duplicated keys (" + key.toString() + ") are provided.");
                this.m.put(key, value);
                return this;
            }

            /**
             * Removes all the entries added into M.
             *
             * @return Returns a reference to this object so that method calls can be chained together.
             */

            public AttributeValue clearMEntries() {
                this.m = null;
                return this;
            }

            /**
             * <p>
             * An attribute of type List. For example:
             * </p>
             * <p>
             * <code>"L": [ {"S": "Cookies"} , {"S": "Coffee"}, {"N", "3.14159"}]</code>
             * </p>
             *
             * @return An attribute of type List. For example:</p>
             *         <p>
             *         <code>"L": [ {"S": "Cookies"} , {"S": "Coffee"}, {"N", "3.14159"}]</code>
             */

            public java.util.List<AttributeValue> getL() {
                return l;
            }

            /**
             * <p>
             * An attribute of type List. For example:
             * </p>
             * <p>
             * <code>"L": [ {"S": "Cookies"} , {"S": "Coffee"}, {"N", "3.14159"}]</code>
             * </p>
             *
             * @param l
             *        An attribute of type List. For example:</p>
             *        <p>
             *        <code>"L": [ {"S": "Cookies"} , {"S": "Coffee"}, {"N", "3.14159"}]</code>
             */

            public void setL(java.util.Collection<AttributeValue> l) {
                if (l == null) {
                    this.l = null;
                    return;
                }

                this.l = new java.util.ArrayList<AttributeValue>(l);
            }

            /**
             * <p>
             * An attribute of type List. For example:
             * </p>
             * <p>
             * <code>"L": [ {"S": "Cookies"} , {"S": "Coffee"}, {"N", "3.14159"}]</code>
             * </p>
             * <p>
             * <b>NOTE:</b> This method appends the values to the existing list (if any). Use
             * {@link #setL(java.util.Collection)} or {@link #withL(java.util.Collection)} if you want to override the existing
             * values.
             * </p>
             *
             * @param l
             *        An attribute of type List. For example:</p>
             *        <p>
             *        <code>"L": [ {"S": "Cookies"} , {"S": "Coffee"}, {"N", "3.14159"}]</code>
             * @return Returns a reference to this object so that method calls can be chained together.
             */

            public AttributeValue withL(AttributeValue... l) {
                if (this.l == null) {
                    setL(new java.util.ArrayList<AttributeValue>(l.length));
                }
                for (AttributeValue ele : l) {
                    this.l.add(ele);
                }
                return this;
            }

            /**
             * <p>
             * An attribute of type List. For example:
             * </p>
             * <p>
             * <code>"L": [ {"S": "Cookies"} , {"S": "Coffee"}, {"N", "3.14159"}]</code>
             * </p>
             *
             * @param l
             *        An attribute of type List. For example:</p>
             *        <p>
             *        <code>"L": [ {"S": "Cookies"} , {"S": "Coffee"}, {"N", "3.14159"}]</code>
             * @return Returns a reference to this object so that method calls can be chained together.
             */

            public AttributeValue withL(java.util.Collection<AttributeValue> l) {
                setL(l);
                return this;
            }

            /**
             * <p>
             * An attribute of type Null. For example:
             * </p>
             * <p>
             * <code>"NULL": true</code>
             * </p>
             *
             * @param nULLValue
             *        An attribute of type Null. For example:</p>
             *        <p>
             *        <code>"NULL": true</code>
             */

            public void setNULL(Boolean nULLValue) {
                this.nULLValue = nULLValue;
            }

            /**
             * <p>
             * An attribute of type Null. For example:
             * </p>
             * <p>
             * <code>"NULL": true</code>
             * </p>
             *
             * @return An attribute of type Null. For example:</p>
             *         <p>
             *         <code>"NULL": true</code>
             */

            public Boolean getNULL() {
                return this.nULLValue;
            }

            /**
             * <p>
             * An attribute of type Null. For example:
             * </p>
             * <p>
             * <code>"NULL": true</code>
             * </p>
             *
             * @param nULLValue
             *        An attribute of type Null. For example:</p>
             *        <p>
             *        <code>"NULL": true</code>
             * @return Returns a reference to this object so that method calls can be chained together.
             */

            public AttributeValue withNULL(Boolean nULLValue) {
                setNULL(nULLValue);
                return this;
            }

            /**
             * <p>
             * An attribute of type Null. For example:
             * </p>
             * <p>
             * <code>"NULL": true</code>
             * </p>
             *
             * @return An attribute of type Null. For example:</p>
             *         <p>
             *         <code>"NULL": true</code>
             */

            public Boolean isNULL() {
                return this.nULLValue;
            }

            /**
             * <p>
             * An attribute of type Boolean. For example:
             * </p>
             * <p>
             * <code>"BOOL": true</code>
             * </p>
             *
             * @param bOOL
             *        An attribute of type Boolean. For example:</p>
             *        <p>
             *        <code>"BOOL": true</code>
             */

            public void setBOOL(Boolean bOOL) {
                this.bOOL = bOOL;
            }

            /**
             * <p>
             * An attribute of type Boolean. For example:
             * </p>
             * <p>
             * <code>"BOOL": true</code>
             * </p>
             *
             * @return An attribute of type Boolean. For example:</p>
             *         <p>
             *         <code>"BOOL": true</code>
             */

            public Boolean getBOOL() {
                return this.bOOL;
            }

            /**
             * <p>
             * An attribute of type Boolean. For example:
             * </p>
             * <p>
             * <code>"BOOL": true</code>
             * </p>
             *
             * @param bOOL
             *        An attribute of type Boolean. For example:</p>
             *        <p>
             *        <code>"BOOL": true</code>
             * @return Returns a reference to this object so that method calls can be chained together.
             */

            public AttributeValue withBOOL(Boolean bOOL) {
                setBOOL(bOOL);
                return this;
            }

            /**
             * <p>
             * An attribute of type Boolean. For example:
             * </p>
             * <p>
             * <code>"BOOL": true</code>
             * </p>
             *
             * @return An attribute of type Boolean. For example:</p>
             *         <p>
             *         <code>"BOOL": true</code>
             */

            public Boolean isBOOL() {
                return this.bOOL;
            }

            @Override
            public AttributeValue clone() {
                try {
                    return (AttributeValue) super.clone();
                } catch (CloneNotSupportedException e) {
                    throw new IllegalStateException("Got a CloneNotSupportedException from Object.clone() even though we're Cloneable!", e);
                }
            }

        }

        /**
         * <p>
         * Contains details about the type of identity that made the request.
         * </p>
         *
         * @see <a href="http://docs.aws.amazon.com/goto/WebAPI/streams-dynamodb-2012-08-10/Identity" target="_top">AWS API
         *      Documentation</a>
         */

        @Data
        @Builder(setterPrefix = "with")
        @NoArgsConstructor
        @AllArgsConstructor
        public static class Identity implements Serializable, Cloneable {

            /**
             * <p>
             * A unique identifier for the entity that made the call. For Time To Live, the principalId is
             * "dynamodb.amazonaws.com".
             * </p>
             */
            private String principalId;

            /**
             * <p>
             * The type of the identity. For Time To Live, the type is "Service".
             * </p>
             */
            private String type;

            @Override
            public Identity clone() {
                try {
                    return (Identity) super.clone();
                } catch (CloneNotSupportedException e) {
                    throw new IllegalStateException("Got a CloneNotSupportedException from Object.clone() " + "even though we're Cloneable!", e);
                }
            }

        }

        public enum OperationType {

            INSERT("INSERT"),
            MODIFY("MODIFY"),
            REMOVE("REMOVE");

            private String value;

            private OperationType(String value) {
                this.value = value;
            }

            @Override
            public String toString() {
                return this.value;
            }

            /**
             * Use this in place of valueOf.
             *
             * @param value
             *        real value
             * @return OperationType corresponding to the value
             *
             * @throws IllegalArgumentException
             *         If the specified value does not map to one of the known values in this enum.
             */
            public static OperationType fromValue(String value) {
                if (value == null || "".equals(value)) {
                    throw new IllegalArgumentException("Value cannot be null or empty!");
                }

                for (OperationType enumEntry : OperationType.values()) {
                    if (enumEntry.toString().equals(value)) {
                        return enumEntry;
                    }
                }
                throw new IllegalArgumentException("Cannot create enum from " + value + " value!");
            }
        }

        /**
         * <p>
         * A description of a single data modification that was performed on an item in a DynamoDB table.
         * </p>
         *
         * @see <a href="http://docs.aws.amazon.com/goto/WebAPI/streams-dynamodb-2012-08-10/StreamRecord" target="_top">AWS API
         *      Documentation</a>
         */

        @Data
        @Builder(setterPrefix = "with")
        @NoArgsConstructor
        @AllArgsConstructor
        public static class StreamRecord implements Serializable, Cloneable {

            /**
             * <p>
             * The approximate date and time when the stream record was created, in <a
             * href="http://www.epochconverter.com/">UNIX epoch time</a> format.
             * </p>
             */
            @JsonProperty("ApproximateCreationDateTime")
            private java.time.Instant approximateCreationDateTime;
            /**
             * <p>
             * The primary key attribute(s) for the DynamoDB item that was modified.
             * </p>
             */
            @JsonProperty("Keys")
            private java.util.Map<String, AttributeValue> keys;
            /**
             * <p>
             * The item in the DynamoDB table as it appeared after it was modified.
             * </p>
             */
            @JsonProperty("NewImage")
            private java.util.Map<String, AttributeValue> newImage;
            /**
             * <p>
             * The item in the DynamoDB table as it appeared before it was modified.
             * </p>
             */
            @JsonProperty("OldImage")
            private java.util.Map<String, AttributeValue> oldImage;
            /**
             * <p>
             * The sequence number of the stream record.
             * </p>
             */
            @JsonProperty("SequenceNumber")
            private String sequenceNumber;
            /**
             * <p>
             * The size of the stream record, in bytes.
             * </p>
             */
            @JsonProperty("SizeBytes")
            private Long sizeBytes;
            /**
             * <p>
             * The type of data from the modified DynamoDB item that was captured in this stream record:
             * </p>
             * <ul>
             * <li>
             * <p>
             * <code>KEYS_ONLY</code> - only the key attributes of the modified item.
             * </p>
             * </li>
             * <li>
             * <p>
             * <code>NEW_IMAGE</code> - the entire item, as it appeared after it was modified.
             * </p>
             * </li>
             * <li>
             * <p>
             * <code>OLD_IMAGE</code> - the entire item, as it appeared before it was modified.
             * </p>
             * </li>
             * <li>
             * <p>
             * <code>NEW_AND_OLD_IMAGES</code> - both the new and the old item images of the item.
             * </p>
             * </li>
             * </ul>
             */
            @JsonProperty("StreamViewType")
            private StreamViewType streamViewType;

            public StreamRecord addKeysEntry(String key, AttributeValue value) {
                if (null == this.keys) {
                    this.keys = new java.util.HashMap<String, AttributeValue>();
                }
                if (this.keys.containsKey(key))
                    throw new IllegalArgumentException("Duplicated keys (" + key.toString() + ") are provided.");
                this.keys.put(key, value);
                return this;
            }

            /**
             * Removes all the entries added into Keys.
             *
             * @return Returns a reference to this object so that method calls can be chained together.
             */
            public StreamRecord clearKeysEntries() {
                this.keys = null;
                return this;
            }

            public StreamRecord addNewImageEntry(String key, AttributeValue value) {
                if (null == this.newImage) {
                    this.newImage = new java.util.HashMap<String, AttributeValue>();
                }
                if (this.newImage.containsKey(key))
                    throw new IllegalArgumentException("Duplicated keys (" + key.toString() + ") are provided.");
                this.newImage.put(key, value);
                return this;
            }

            /**
             * Removes all the entries added into NewImage.
             *
             * @return Returns a reference to this object so that method calls can be chained together.
             */
            public StreamRecord clearNewImageEntries() {
                this.newImage = null;
                return this;
            }

            public StreamRecord addOldImageEntry(String key, AttributeValue value) {
                if (null == this.oldImage) {
                    this.oldImage = new java.util.HashMap<String, AttributeValue>();
                }
                if (this.oldImage.containsKey(key))
                    throw new IllegalArgumentException("Duplicated keys (" + key.toString() + ") are provided.");
                this.oldImage.put(key, value);
                return this;
            }

            /**
             * Removes all the entries added into OldImage.
             *
             * @return Returns a reference to this object so that method calls can be chained together.
             */
            public StreamRecord clearOldImageEntries() {
                this.oldImage = null;
                return this;
            }

            @Override
            public StreamRecord clone() {
                try {
                    return (StreamRecord) super.clone();
                } catch (CloneNotSupportedException e) {
                    throw new IllegalStateException("Got a CloneNotSupportedException from Object.clone() " + "even though we're Cloneable!", e);
                }
            }

        }

        public enum StreamViewType {

            NEW_IMAGE("NEW_IMAGE"),
            OLD_IMAGE("OLD_IMAGE"),
            NEW_AND_OLD_IMAGES("NEW_AND_OLD_IMAGES"),
            KEYS_ONLY("KEYS_ONLY");

            private String value;

            StreamViewType(String value) {
                this.value = value;
            }

            public String getValue() {
                return value;
            }

            @Override
            public String toString() {
                return this.value;
            }

            /**
             * Use this in place of valueOf.
             *
             * @param value real value
             * @return StreamViewType corresponding to the value
             *
             * @throws IllegalArgumentException
             *         If the specified value does not map to one of the known values in this enum.
             */
            public static StreamViewType fromValue(String value) {
                if (value == null || "".equals(value)) {
                    throw new IllegalArgumentException("Value cannot be null or empty!");
                }

                for (StreamViewType enumEntry : StreamViewType.values()) {
                    if (enumEntry.toString().equals(value)) {
                        return enumEntry;
                    }
                }

                throw new IllegalArgumentException("Cannot create enum from " + value + " value!");
            }
        }

        @Override
        public DynamodbStreamRecord clone() {
            try {
                return (DynamodbStreamRecord) super.clone();
            } catch (CloneNotSupportedException e) {
                throw new IllegalStateException("Got a CloneNotSupportedException from Object.clone() " + "even though we're Cloneable!", e);
            }
        }
    }

    @Override
    public DynamodbEvent clone() {
        try {
            return (DynamodbEvent) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new IllegalStateException("Got a CloneNotSupportedException from Object.clone()", e);
        }
    }
    
}
