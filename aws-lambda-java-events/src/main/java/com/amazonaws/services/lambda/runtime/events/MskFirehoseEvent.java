/*
 * Copyright 2012-2017 Amazon.com, Inc. or its affiliates. All Rights Reserved.
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

package events;

import java.io.Serializable;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.Map;

/**
 * Created by vermshas on 6/28/24.
 */
public class MskFirehoseEvent implements Serializable, Cloneable {

    private static final long serialVersionUID = -2890373471008001695L;

    private String invocationId;

    private String deliveryStreamArn;

    private String region;

    private List<Record> records;

    public static class Record implements Serializable, Cloneable {

        private static final long serialVersionUID = -7231161900431910379L;

        /**
         * <p>
         * The data blob, which is base64-encoded when the blob is serialized. The maximum size of the data blob, before
         * base64-encoding, is 1,000 KB.
         * </p>
         */
        private ByteBuffer kafkaRecordValue;

        private String recordId;

        private Long approximateArrivalEpoch;

        private Long approximateArrivalTimestamp;

        private Map<String, String> mskRecordMetadata;

        /**
         * default constructor
         */
        public Record() {}

        /**
         * <p>
         * The data blob, which is base64-encoded when the blob is serialized. The maximum size of the data blob, before
         * base64-encoding, is 1,000 KB.
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
         * @param kafkaRecordValue
         *        The data blob, which is base64-encoded when the blob is serialized. The maximum size of the data blob,
         *        before base64-encoding, is 1,000 KB.
         */
        public void setKafkaRecordValue(ByteBuffer kafkaRecordValue) {
            this.kafkaRecordValue = kafkaRecordValue;
        }

        /**
         * <p>
         * The data blob, which is base64-encoded when the blob is serialized. The maximum size of the data blob, before
         * base64-encoding, is 1,000 KB.
         * </p>
         * <p>
         * {@code ByteBuffer}s are stateful. Calling their {@code get} methods changes their {@code position}. We recommend
         * using {@link java.nio.ByteBuffer#asReadOnlyBuffer()} to create a read-only view of the buffer with an independent
         * {@code position}, and calling {@code get} methods on this rather than directly on the returned {@code ByteBuffer}.
         * Doing so will ensure that anyone else using the {@code ByteBuffer} will not be affected by changes to the
         * {@code position}.
         * </p>
         *
         * @return The data blob, which is base64-encoded when the blob is serialized. The maximum size of the data blob,
         *         before base64-encoding, is 1,000 KB.
         */
        public ByteBuffer getKafkaRecordValue() {
            return this.kafkaRecordValue;
        }

        /**
         * @return record id
         */
        public String getRecordId() {
            return this.recordId;
        }

        /**
         * @param recordId record id
         */
        public void setRecordId(String recordId) {
            this.recordId = recordId;
        }

        /**
         * @param recordId record id
         * @return Record
         */
        public Record withRecordId(String recordId) {
            setRecordId(recordId);
            return this;
        }

        /**
         * @return approximate arrival epoch
         */
        public Long getApproximateArrivalEpoch() {
            return this.approximateArrivalEpoch;
        }

        /**
         * @param approximateArrivalEpoch Long epoch
         */
        public void setApproximateArrivalEpoch(Long approximateArrivalEpoch) {
            this.approximateArrivalEpoch = approximateArrivalEpoch;
        }

        /**
         * @param approximateArrivalEpoch Long epoch
         * @return Record
         */
        public Record withApproximateArrivalEpoch(Long approximateArrivalEpoch) {
            setApproximateArrivalEpoch(approximateArrivalEpoch);
            return this;
        }

        /**
         * @return approximate arrival timestamp
         */
        public Long getApproximateArrivalTimestamp() {
            return this.approximateArrivalTimestamp;
        }

        /**
         * @param approximateArrivalTimestamp approximate arrival timestamp
         */
        public void setApproximateArrivalTimestamp(Long approximateArrivalTimestamp) {
            this.approximateArrivalTimestamp = approximateArrivalTimestamp;
        }

        /**
         * @param approximateArrivalTimestamp approximate arrival timestamp
         * @return Record
         */
        public Record withApproximateArrivalTimestamp(Long approximateArrivalTimestamp) {
            setApproximateArrivalTimestamp(approximateArrivalTimestamp);
            return this;
        }

        /**
         * @return Msk record meta data
         */
        public Map<String, String> getMskRecordMetadata() {
            return this.mskRecordMetadata;
        }

        /**
         * @param mskRecordMetadata Msk record metadata
         */
        public void setMskRecordMetadata(Map<String, String> mskRecordMetadata) {
            this.mskRecordMetadata = mskRecordMetadata;
        }

        /**
         * @param mskRecordMetadata Msk record metadata
         * @return Record
         */
        public Record withMskRecordMetadata(Map<String, String> mskRecordMetadata) {
            setMskRecordMetadata(mskRecordMetadata);
            return this;
        }

        /**
         * Returns a string representation of this object; useful for testing and debugging.
         *
         * @return A string representation of this object.
         *
         * @see Object#toString()
         */
        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("{");
            if (getKafkaRecordValue() != null)
                sb.append("data: ").append(getKafkaRecordValue().toString()).append(",");
            if (getRecordId() != null)
                sb.append("recordId: ").append(getRecordId()).append(",");
            if (getApproximateArrivalEpoch() != null)
                sb.append("approximateArrivalEpoch: ").append(getApproximateArrivalEpoch().toString()).append(",");
            if (getApproximateArrivalTimestamp() != null)
                sb.append("approximateArrivalTimestamp: ").append(getApproximateArrivalTimestamp().toString()).append(",");
            if (getMskRecordMetadata() != null)
                sb.append("mskRecordMetadata: ").append(getMskRecordMetadata().toString());
            sb.append("}");
            return sb.toString();
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;

            if (obj instanceof Record == false)
                return false;
            Record other = (Record) obj;
            if (other.getKafkaRecordValue() == null ^ this.getKafkaRecordValue() == null)
                return false;
            if (other.getKafkaRecordValue() != null && other.getKafkaRecordValue().equals(this.getKafkaRecordValue()) == false)
                return false;
            if (other.getRecordId() == null ^ this.getRecordId() == null)
                return false;
            if (other.getRecordId() != null && other.getRecordId().equals(this.getRecordId()) == false)
                return false;
            if (other.getApproximateArrivalEpoch() == null ^ this.getApproximateArrivalEpoch() == null)
                return false;
            if (other.getApproximateArrivalEpoch() != null && other.getApproximateArrivalEpoch().equals(this.getApproximateArrivalEpoch()) == false)
                return false;
            if (other.getApproximateArrivalTimestamp() == null ^ this.getApproximateArrivalTimestamp() == null)
                return false;
            if (other.getApproximateArrivalTimestamp() != null && other.getApproximateArrivalTimestamp().equals(this.getApproximateArrivalTimestamp()) == false)
                return false;
            if (other.getMskRecordMetadata() == null ^ this.getMskRecordMetadata() == null)
                return false;
            if (other.getMskRecordMetadata() != null && other.getMskRecordMetadata().equals(this.getMskRecordMetadata()) == false)
                return false;
            return true;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int hashCode = 1;

            hashCode = prime * hashCode + ((getKafkaRecordValue() == null) ? 0 : getKafkaRecordValue().hashCode());
            hashCode = prime * hashCode + ((getRecordId() == null) ? 0 : getRecordId().hashCode());
            hashCode = prime * hashCode + ((getApproximateArrivalEpoch() == null) ? 0 : getApproximateArrivalEpoch().hashCode());
            hashCode = prime * hashCode + ((getApproximateArrivalTimestamp() == null) ? 0 : getApproximateArrivalTimestamp().hashCode());
            hashCode = prime * hashCode + ((getMskRecordMetadata() == null) ? 0 : getMskRecordMetadata().hashCode());

            return hashCode;
        }

        @Override
        public Record clone() {
            try {
                return (Record) super.clone();
            } catch (CloneNotSupportedException e) {
                throw new IllegalStateException("Got a CloneNotSupportedException from Object.clone()", e);
            }
        }

    }

    /**
     * default constructor
     */
    public MskFirehoseEvent() {}

    /**
     * @return invocation id
     */
    public String getInvocationId() {
        return this.invocationId;
    }

    /**
     * @param invocationId invocation id
     */
    public void setInvocationId(String invocationId) {
        this.invocationId = invocationId;
    }

    /**
     * @param invocationId invocation id
     * @return MskFirehoseEvent
     */
    public MskFirehoseEvent withInvocationId(String invocationId) {
        setInvocationId(invocationId);
        return this;
    }

    /**
     * @return delivery stream arn
     */
    public String getDeliveryStreamArn() {
        return this.deliveryStreamArn;
    }

    /**
     * @param deliveryStreamArn delivery stream arn
     */
    public void setDeliveryStreamArn(String deliveryStreamArn) {
        this.deliveryStreamArn = deliveryStreamArn;
    }

    /**]
     * @param deliveryStreamArn delivery stream arn
     * @return MskFirehoseEvent
     */
    public MskFirehoseEvent withDeliveryStreamArn(String deliveryStreamArn) {
        setDeliveryStreamArn(deliveryStreamArn);
        return this;
    }

    /**
     * @return region
     */
    public String getRegion() {
        return this.region;
    }

    /**
     * @param region aws region
     */
    public void setRegion(String region) {
        this.region = region;
    }

    /**
     * @param region aws region
     * @return MskFirehoseEvent
     */
    public MskFirehoseEvent withRegion(String region) {
        setRegion(region);
        return this;
    }

    /**
     * Gets the list of MSK event records
     *
     */
    public List<Record> getRecords() {
        return records;
    }

    /**
     * Sets the list of MSK event records
     * @param records a list of MSK event records
     */
    public void setRecords(List<Record> records) {
        this.records = records;
    }

    /**
     * @param records a list of MSK event records
     * @return MskFirehoseEvent
     */
    public MskFirehoseEvent withRecords(List<Record> records) {
        setRecords(records);
        return this;
    }

    /**
     * Returns a string representation of this object; useful for testing and debugging.
     *
     * @return A string representation of this object.
     *
     * @see Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        if (getInvocationId() != null)
            sb.append("invocationId: ").append(getInvocationId()).append(",");
        if (getDeliveryStreamArn() != null)
            sb.append("deliveryStreamArn: ").append(getDeliveryStreamArn()).append(",");
        if (getRegion() != null)
            sb.append("region: ").append(getRegion()).append(",");
        if (getRecords() != null)
            sb.append("records: ").append(getRecords().toString());
        sb.append("}");
        return sb.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;

        if (obj instanceof MskFirehoseEvent == false)
            return false;
        MskFirehoseEvent other = (MskFirehoseEvent) obj;
        if (other.getInvocationId() == null ^ this.getInvocationId() == null)
            return false;
        if (other.getInvocationId() != null && other.getInvocationId().equals(this.getInvocationId()) == false)
            return false;
        if (other.getDeliveryStreamArn() == null ^ this.getDeliveryStreamArn() == null)
            return false;
        if (other.getDeliveryStreamArn() != null && other.getDeliveryStreamArn().equals(this.getDeliveryStreamArn()) == false)
            return false;
        if (other.getRegion() == null ^ this.getRegion() == null)
            return false;
        if (other.getRegion() != null && other.getRegion().equals(this.getRegion()) == false)
            return false;
        if (other.getRecords() == null ^ this.getRecords() == null)
            return false;
        if (other.getRecords() != null && other.getRecords().equals(this.getRecords()) == false)
            return false;
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int hashCode = 1;

        hashCode = prime * hashCode + ((getInvocationId() == null) ? 0 : getInvocationId().hashCode());
        hashCode = prime * hashCode + ((getDeliveryStreamArn() == null) ? 0 : getDeliveryStreamArn().hashCode());
        hashCode = prime * hashCode + ((getRegion() == null) ? 0 : getRegion().hashCode());
        hashCode = prime * hashCode + ((getRecords() == null) ? 0 : getRecords().hashCode());
        return hashCode;
    }


    @Override
    public MskFirehoseEvent clone() {
        try {
            return (MskFirehoseEvent) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new IllegalStateException("Got a CloneNotSupportedException from Object.clone()", e);
        }
    }
}
