/* Copyright 2015 Amazon.com, Inc. or its affiliates. All Rights Reserved. */

package com.amazonaws.services.lambda.runtime.events;

import java.util.Map;

/**
 * 
 * Represents an Amazon Congnito event sent to Lambda Functions
 *
 */
public class CognitoEvent {

	/**
	 * DatasetRecord contains the information about each record in a data set.
	 *
	 */
    public static class DatasetRecord {
        
        private String oldValue;
        
        private String newValue;
     
        private String op;

        /**
         * Get the record's old value
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
         * Gets the record's new value
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
    }

    private String region;

    private Map<String, DatasetRecord> datasetRecords;

    private String identityPoolId;

    private String identityId;

    private String datasetName;

    private String eventType;

    private Integer version;

    /**
     * Gets the region in which data set resides.
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
     * Gets the map of data set records for the event
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
     * Gets the identity pool ID associated with the data set
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
     * Gets the identity pool ID associated with the data set
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
     * Gets the data set name of the event
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
     * Gets the event type
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
     * Gets the event version
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
}
