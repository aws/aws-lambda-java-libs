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

/**
 * Class representing CloudWatchLogs event (callback when cloud watch logs something)
 */
public class CloudWatchLogsEvent implements Serializable, Cloneable {

    private static final long serialVersionUID = -1617470828168156271L;

    private AWSLogs awsLogs;

    /**
     * Represents AWSLogs object in CloudWatch Evenet
     */
    public static class AWSLogs implements Serializable, Cloneable {

        private static final long serialVersionUID = -7793438350437169987L;

        private String data;

        /**
         * default constructor
         */
        public AWSLogs() {}

        /**
         * @return String with data
         */
        public String getData() {
            return this.data;
        }

        /**
         * @param data String with log data
         */
        public void setData(String data) {
            this.data = data;
        }

        /**
         * @param data String with log data
         * @return
         */
        public AWSLogs withData(String data) {
            setData(data);
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
            if (getData() != null)
                sb.append("data: ").append(getData());
            sb.append("}");
            return sb.toString();
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;

            if (obj instanceof AWSLogs == false)
                return false;
            AWSLogs other = (AWSLogs) obj;
            if (other.getData() == null ^ this.getData() == null)
                return false;
            if (other.getData() != null && other.getData().equals(this.getData()) == false)
                return false;
            return true;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int hashCode = 1;

            hashCode = prime * hashCode + ((getData() == null) ? 0 : getData().hashCode());
            return hashCode;
        }

        @Override
        public AWSLogs clone() {
            try {
                return (AWSLogs) super.clone();
            } catch (CloneNotSupportedException e) {
                throw new IllegalStateException("Got a CloneNotSupportedException from Object.clone()", e);
            }
        }

    }

    /**
     * default constructor
     */
    public CloudWatchLogsEvent() {}

    /**
     * @return AWSLogs object
     */
    public AWSLogs getAwsLogs() {
        return this.awsLogs;
    }

    /**
     * @param awsLogs AWSLogs object
     */
    public void setAwsLogs(AWSLogs awsLogs) {
        this.awsLogs = awsLogs;
    }

    /**
     * @param awsLogs AWSLogs object
     * @return
     */
    public CloudWatchLogsEvent withAwsLogs(AWSLogs awsLogs) {
        setAwsLogs(awsLogs);
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
        if (getAwsLogs() != null)
            sb.append("awslogs: ").append(getAwsLogs().toString());
        sb.append("}");
        return sb.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;

        if (obj instanceof CloudWatchLogsEvent == false)
            return false;
        CloudWatchLogsEvent other = (CloudWatchLogsEvent) obj;
        if (other.getAwsLogs() == null ^ this.getAwsLogs() == null)
            return false;
        if (other.getAwsLogs() != null && other.getAwsLogs().equals(this.getAwsLogs()) == false)
            return false;
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int hashCode = 1;

        hashCode = prime * hashCode + ((getAwsLogs() == null) ? 0 : getAwsLogs().hashCode());
        return hashCode;
    }

    @Override
    public CloudWatchLogsEvent clone() {
        try {
            return (CloudWatchLogsEvent) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new IllegalStateException("Got a CloneNotSupportedException from Object.clone()", e);
        }
    }
}
