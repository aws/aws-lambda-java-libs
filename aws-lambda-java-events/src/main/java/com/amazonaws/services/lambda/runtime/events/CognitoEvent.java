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

package com.amazonaws.services.lambda.runtime.events;

import java.io.Serializable;
import java.util.Map;

/**
 * 
 * Represents an Amazon Cognito event sent to Lambda Functions
 *
 */
public class CognitoEvent implements Serializable, Cloneable {

    private static final long serialVersionUID = -3471890133562627751L;

    private String region;

    private Map<String, DatasetRecord> datasetRecords;

    private String identityPoolId;

    private String identityId;

    private String datasetName;

    private String eventType;

    private Integer version;

	/**
	 * DatasetRecord contains the information about each record in a data set.
	 *
	 */
    public static class DatasetRecord implements Serializable, Cloneable {

        private static final long serialVersionUID = -8853471047466644850L;

        private String oldValue;
        
        private String newValue;
     
        private String op;

        /**
         * default constructor
         * (Not available in v1)
         */
        public DatasetRecord() {}

        /**
         * Get the record's old value
         * @return old value
         */
        public String getOldValue() {
            return oldValue;
        }

        /**
         * Sets the record's old value
         * @param oldValue A string containing the old value
         */
        public void setOldValue(String oldValue) {
            this.oldValue = oldValue;
        }

        /**
         * @param oldValue String with old value
         * @return DatasetRecord object
         */
        public DatasetRecord withOldValue(String oldValue) {
            setOldValue(oldValue);
            return this;
        }

        /**
         * Gets the record's new value
         * @return new value
         */
        public String getNewValue() {
            return newValue;
        }

        /**
         * Sets the records new value
         * @param newValue A string containing the new value
         */
        public void setNewValue(String newValue) {
            this.newValue = newValue;
        }

        /**
         * @param newValue new value for record
         * @return DatasetRecord object
         */
        public DatasetRecord withNewValue(String newValue) {
            setNewValue(newValue);
            return this;
        }

        /**
         * Gets the operation associated with the record
         * <p>
         * <ul>
         * <li>
         * For a new record or any updates to existing record it is set to "replace".
         * </li>
         * <li>
         * For deleting a record it is set to "remove".
         * </li>
         * </ul>
         * </p>
         */
        public String getOp() {
            return op;
        }

        /**
         * Sets the operation associated with the record
         * <p>
         * <ul>
         * <li>
         * For a new record or any updates to existing record it is set to "replace".
         * </li>
         * <li>
         * For deleting a record it is set to "remove".
         * </li>
         * </ul>
         * </p>
         * @param op A string with a value of "replace" of "remove"
         */
        public void setOp(String op) {
            this.op = op;
        }

        /**
         * @param op String operation
         * @return DatasetRecord object
         */
        public DatasetRecord withOp(String op) {
            setOp(op);
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
            if (getOldValue() != null)
                sb.append("oldValue: ").append(getOldValue()).append(",");
            if (getNewValue() != null)
                sb.append("newValue: ").append(getNewValue()).append(",");
            if (getOp() != null)
                sb.append("op: ").append(getOp());
            sb.append("}");
            return sb.toString();
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;

            if (obj instanceof DatasetRecord == false)
                return false;
            DatasetRecord other = (DatasetRecord) obj;
            if (other.getOldValue() == null ^ this.getOldValue() == null)
                return false;
            if (other.getOldValue() != null && other.getOldValue().equals(this.getOldValue()) == false)
                return false;
            if (other.getNewValue() == null ^ this.getNewValue() == null)
                return false;
            if (other.getNewValue() != null && other.getNewValue().equals(this.getNewValue()) == false)
                return false;
            if (other.getOp() == null ^ this.getOp() == null)
                return false;
            if (other.getOp() != null && other.getOp().equals(this.getOp()) == false)
                return false;
            return true;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int hashCode = 1;

            hashCode = prime * hashCode + ((getOldValue() == null) ? 0 : getOldValue().hashCode());
            hashCode = prime * hashCode + ((getNewValue() == null) ? 0 : getNewValue().hashCode());
            hashCode = prime * hashCode + ((getOp() == null) ? 0 : getOp().hashCode());
            return hashCode;
        }

        @Override
        public DatasetRecord clone() {
            try {
                return (DatasetRecord) super.clone();
            } catch (CloneNotSupportedException e) {
                throw new IllegalStateException("Got a CloneNotSupportedException from Object.clone()", e);
            }
        }
    }

    /**
     * default constructor
     * (Not available in v1)
     */
    public CognitoEvent() {}

    /**
     * Gets the region in which data set resides.
     * @return aws region
     */
    public String getRegion() {
        return region;
    }

    /**
     * Sets the region in which data set resides.
     * @param region A string containing a region name
     */
    public void setRegion(String region) {
        this.region = region;
    }

    /**
     * @param region String of region name
     * @return CognitoEvent
     */
    public CognitoEvent withRegion(String region) {
        setRegion(region);
        return this;
    }

    /**
     * Gets the map of data set records for the event
     * @return map of dataset records
     */
    public Map<String, DatasetRecord> getDatasetRecords() {
        return datasetRecords;
    }

    /**
     * Sets the map of data set records for the event
     * @param datasetRecords A map of string & data set record key/value pairs
     */
    public void setDatasetRecords(Map<String, DatasetRecord> datasetRecords) {
        this.datasetRecords = datasetRecords;
    }

    /**
     * @param datasetRecords a map of string & data set record key/value pairs
     * @return CognitoEvent
     */
    public CognitoEvent withDatasetRecords(Map<String, DatasetRecord> datasetRecords) {
        setDatasetRecords(datasetRecords);
        return this;
    }

    /**
     * Gets the identity pool ID associated with the data set
     * @return identity pool id
     */
    public String getIdentityPoolId() {
        return identityPoolId;
    }

    /**
     * Sets the identity pool ID associated with the data set
     * @param identityPoolId A string containing the identity pool ID.
     */
    public void setIdentityPoolId(String identityPoolId) {
        this.identityPoolId = identityPoolId;
    }

    /**
     * @param identityPoolId a string containing the identity pool ID
     * @return CognitoEvent
     */
    public CognitoEvent withIdentityPoolId(String identityPoolId) {
        setIdentityPoolId(identityPoolId);
        return this;
    }

    /**
     * Gets the identity pool ID associated with the data set
     * @return identity id
     */
    public String getIdentityId() {
        return identityId;
    }

    /**
     * Sets the identity pool ID associated with the data set
     * @param identityId A string containing the identity pool ID
     */
    public void setIdentityId(String identityId) {
        this.identityId = identityId;
    }

    /**
     * @param identityId a string containing identity id
     * @return CognitoEvent
     */
    public CognitoEvent withIdentityId(String identityId) {
        setIdentityId(identityId);
        return this;
    }

    /**
     * Gets the data set name of the event
     * @return dataset name
     */
    public String getDatasetName() {
        return datasetName;
    }

    /**
     * Sets the data set name for the event
     * @param datasetName A string containing the data set name
     */
    public void setDatasetName(String datasetName) {
        this.datasetName = datasetName;
    }

    /**
     * @param datasetName String with data set name
     * @return CognitoEvent
     */
    public CognitoEvent withDatasetName(String datasetName) {
        setDatasetName(datasetName);
        return this;
    }

    /**
     * Gets the event type
     * @return event type
     */
    public String getEventType() {
        return eventType;
    }

    /**
     * Sets the event type
     * @param eventType A string containing the event type
     */
    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    /**
     * @param eventType String with event type
     * @return CognitoEvent
     */
    public CognitoEvent withEventType(String eventType) {
        setEventType(eventType);
        return this;
    }

    /**
     * Gets the event version
     * @return version as integer
     */
    public Integer getVersion() {
        return version;
    }

    /**
     * Sets the event version
     * @param version An integer representing the event version
     */
    public void setVersion(Integer version) {
        this.version = version;
    }

    /**
     * @param version Integer with version
     * @return CognitoEvent
     */
    public CognitoEvent withVersion(Integer version) {
        setVersion(version);
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
        if (getRegion() != null)
            sb.append("region: ").append(getRegion()).append(",");
        if (getDatasetRecords() != null)
            sb.append("datasetRecords: ").append(getDatasetRecords().toString()).append(",");
        if (getIdentityPoolId() != null)
            sb.append("identityPoolId: ").append(getIdentityPoolId()).append(",");
        if (getIdentityId() != null)
            sb.append("identityId: ").append(getIdentityId()).append(",");
        if (getDatasetName() != null)
            sb.append("datasetName: ").append(getDatasetName()).append(",");
        if (getEventType() != null)
            sb.append("eventType: ").append(getEventType()).append(",");
        if (getVersion() != null)
            sb.append("version: ").append(getVersion().toString());
        sb.append("}");
        return sb.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;

        if (obj instanceof CognitoEvent == false)
            return false;
        CognitoEvent other = (CognitoEvent) obj;
        if (other.getRegion() == null ^ this.getRegion() == null)
            return false;
        if (other.getRegion() != null && other.getRegion().equals(this.getRegion()) == false)
            return false;
        if (other.getDatasetRecords() == null ^ this.getDatasetRecords() == null)
            return false;
        if (other.getDatasetRecords() != null && other.getDatasetRecords().equals(this.getDatasetRecords()) == false)
            return false;
        if (other.getIdentityPoolId() == null ^ this.getIdentityPoolId() == null)
            return false;
        if (other.getIdentityPoolId() != null && other.getIdentityPoolId().equals(this.getIdentityPoolId()) == false)
            return false;
        if (other.getIdentityId() == null ^ this.getIdentityId() == null)
            return false;
        if (other.getIdentityId() != null && other.getIdentityId().equals(this.getIdentityId()) == false)
            return false;
        if (other.getDatasetName() == null ^ this.getDatasetName() == null)
            return false;
        if (other.getDatasetName() != null && other.getDatasetName().equals(this.getDatasetName()) == false)
            return false;
        if (other.getEventType() == null ^ this.getEventType() == null)
            return false;
        if (other.getEventType() != null && other.getEventType().equals(this.getEventType()) == false)
            return false;
        if (other.getVersion() == null ^ this.getVersion() == null)
            return false;
        if (other.getVersion() != null && other.getVersion().equals(this.getVersion()) == false)
            return false;
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int hashCode = 1;
        hashCode = prime * hashCode + ((getRegion() == null) ? 0 : getRegion().hashCode());
        hashCode = prime * hashCode + ((getDatasetRecords() == null) ? 0 : getDatasetRecords().hashCode());
        hashCode = prime * hashCode + ((getIdentityPoolId() == null) ? 0 : getIdentityPoolId().hashCode());
        hashCode = prime * hashCode + ((getIdentityId() == null) ? 0 : getIdentityId().hashCode());
        hashCode = prime * hashCode + ((getDatasetName() == null) ? 0 : getDatasetName().hashCode());
        hashCode = prime * hashCode + ((getEventType() == null) ? 0 : getEventType().hashCode());
        hashCode = prime * hashCode + ((getVersion() == null) ? 0 : getVersion().hashCode());
        return hashCode;
    }

    @Override
    public CognitoEvent clone() {
        try {
            return (CognitoEvent) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new IllegalStateException("Got a CloneNotSupportedException from Object.clone()", e);
        }
    }

}
