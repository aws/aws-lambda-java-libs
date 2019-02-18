package com.amazonaws.services.lambda.runtime.events;

import org.joda.time.DateTime;

import java.io.Serializable;
import java.util.List;

/**
 * References a CodeCommit event
 */
public class CodeCommitEvent implements Serializable, Cloneable {

    private static final long serialVersionUID = 2404735479795009282L;

    private List<Record> records;

    /**
     * represents a Reference object in a CodeCommit object
     */
    public static class Reference implements Serializable, Cloneable {

        private static final long serialVersionUID = 9166524005926768827L;

        private String commit;

        private String ref;

        private Boolean created;

        /**
         * default constructor
         */
        public Reference() {}

        /**
         * @return commit id
         */
        public String getCommit() {
            return this.commit;
        }

        /**
         * @param commit set commit id
         */
        public void setCommit(String commit) {
            this.commit = commit;
        }

        /**
         * @param commit commit id
         * @return Reference
         */
        public Reference withCommit(String commit) {
            setCommit(commit);
            return this;
        }

        /**
         * @return reference id
         */
        public String getRef() {
            return this.ref;
        }

        /**
         * @param ref reference id
         */
        public void setRef(String ref) {
            this.ref = ref;
        }

        /**
         * @param ref reference id
         * @return Reference object
         */
        public Reference withRef(String ref) {
            setRef(ref);
            return this;
        }

        /**
         * @return whether reference was created
         */
        public Boolean getCreated() {
            return this.created;
        }

        /**
         * @param created whether reference was created
         */
        public void setCreated(Boolean created) {
            this.created = created;
        }

        /**
         * @param created whether reference was created
         * @return Reference object
         */
        public Reference withCreated(Boolean created) {
            setCreated(created);
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
            if (getCommit() != null)
                sb.append("commit: ").append(getCommit()).append(",");
            if (getRef() != null)
                sb.append("ref: ").append(getRef()).append(",");
            if (getCreated() != null)
                sb.append("created: ").append(getCreated().toString());
            sb.append("}");
            return sb.toString();
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;

            if (obj instanceof Reference == false)
                return false;
            Reference other = (Reference) obj;
            if (other.getCommit() == null ^ this.getCommit() == null)
                return false;
            if (other.getCommit() != null && other.getCommit().equals(this.getCommit()) == false)
                return false;
            if (other.getRef() == null ^ this.getRef() == null)
                return false;
            if (other.getRef() != null && other.getRef().equals(this.getRef()) == false)
                return false;
            if (other.getCreated() == null ^ this.getCreated() == null)
                return false;
            if (other.getCreated() != null && other.getCreated().equals(this.getCreated()) == false)
                return false;
            return true;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int hashCode = 1;

            hashCode = prime * hashCode + ((getCommit() == null) ? 0 : getCommit().hashCode());
            hashCode = prime * hashCode + ((getRef() == null) ? 0 : getRef().hashCode());
            hashCode = prime * hashCode + ((getCreated() == null) ? 0 : getCreated().hashCode());
            return hashCode;
        }

        @Override
        public Reference clone() {
            try {
                return (Reference) super.clone();
            } catch (CloneNotSupportedException e) {
                throw new IllegalStateException("Got a CloneNotSupportedException from Object.clone()", e);
            }
        }
    }

    /**
     * Represents a CodeCommit object in a record
     */
    public static class CodeCommit implements Serializable, Cloneable {

        private static final long serialVersionUID = 2594306162311794147L;

        private List<Reference> references;

        /**
         * default constructor
         */
        public CodeCommit() {}

        /**
         * @return list of Reference objects in the CodeCommit event
         */
        public List<Reference> getReferences() {
            return this.references;
        }

        /**
         * @param references list of Reference objects in the CodeCommit event
         */
        public void setReferences(List<Reference> references) {
            this.references = references;
        }

        /**
         * @param references list of Reference objects in the CodeCommit event
         * @return CodeCommit
         */
        public CodeCommit withReferences(List<Reference> references) {
            setReferences(references);
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
            if (getReferences() != null)
                sb.append("references: ").append(getReferences().toString());
            sb.append("}");
            return sb.toString();
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;

            if (obj instanceof CodeCommit == false)
                return false;
            CodeCommit other = (CodeCommit) obj;
            if (other.getReferences() == null ^ this.getReferences() == null)
                return false;
            if (other.getReferences() != null && other.getReferences().equals(this.getReferences()) == false)
                return false;
            return true;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int hashCode = 1;

            hashCode = prime * hashCode + ((getReferences() == null) ? 0 : getReferences().hashCode());

            return hashCode;
        }

        @Override
        public CodeCommit clone() {
            try {
                return (CodeCommit) super.clone();
            } catch (CloneNotSupportedException e) {
                throw new IllegalStateException("Got a CloneNotSupportedException from Object.clone()", e);
            }
        }
    }

    /**
     * represents a CodeCommit record
     */
    public static class Record implements Serializable, Cloneable {

        private static final long serialVersionUID = 1116409777237432728L;

        private String eventId;

        private String eventVersion;

        private DateTime eventTime;

        private String eventTriggerName;

        private Integer eventPartNumber;

        private CodeCommit codeCommit;

        private String eventName;

        private String eventTriggerConfigId;

        private String eventSourceArn;

        private String userIdentityArn;

        private String eventSource;

        private String awsRegion;

        private String customData;

        private Integer eventTotalParts;

        /**
         * default constructor
         */
        public Record() {}

        /**
         * @return event id
         */
        public String getEventId() {
            return this.eventId;
        }

        /**
         * @param eventId event id
         */
        public void setEventId(String eventId) {
            this.eventId = eventId;
        }

        /**
         * @param eventId event id
         * @return Record
         */
        public Record withEventId(String eventId) {
            setEventId(eventId);
            return this;
        }

        /**
         * @return event version
         */
        public String getEventVersion() {
            return this.eventVersion;
        }

        /**
         * @param eventVersion event version
         */
        public void setEventVersion(String eventVersion) {
            this.eventVersion = eventVersion;
        }

        /**
         * @param eventVersion event version
         * @return Record
         */
        public Record withEventVersion(String eventVersion) {
            setEventVersion(eventVersion);
            return this;
        }

        /**
         * @return event timestamp
         */
        public DateTime getEventTime() {
            return this.eventTime;
        }

        /**
         * @param eventTime event timestamp
         */
        public void setEventTime(DateTime eventTime) {
            this.eventTime = eventTime;
        }

        /**
         * @param eventTime event timestamp
         * @return Record
         */
        public Record withEventTime(DateTime eventTime) {
            setEventTime(eventTime);
            return this;
        }

        /**
         * @return event trigger name
         */
        public String getEventTriggerName() {
            return this.eventTriggerName;
        }

        /**
         * @param eventTriggerName event trigger name
         */
        public void setEventTriggerName(String eventTriggerName) {
            this.eventTriggerName = eventTriggerName;
        }

        /**
         * @param eventTriggerName
         * @return Record
         */
        public Record withEventTriggerName(String eventTriggerName) {
            setEventTriggerName(eventTriggerName);
            return this;
        }

        /**
         * @return event part number
         */
        public Integer getEventPartNumber() {
            return this.eventPartNumber;
        }

        /**
         * @param eventPartNumber event part number
         */
        public void setEventPartNumber(Integer eventPartNumber) {
            this.eventPartNumber = eventPartNumber;
        }

        /**
         * @param eventPartNumber event part number
         * @return Record
         */
        public Record withEventPartNumber(Integer eventPartNumber) {
            setEventPartNumber(eventPartNumber);
            return this;
        }

        /**
         * @return code commit
         */
        public CodeCommit getCodeCommit() {
            return this.codeCommit;
        }

        /**
         * @param codeCommit code commit
         */
        public void setCodeCommit(CodeCommit codeCommit) {
            this.codeCommit = codeCommit;
        }

        /**
         * @param codeCommit code commit
         * @return Record
         */
        public Record withCodeCommit(CodeCommit codeCommit) {
            setCodeCommit(codeCommit);
            return this;
        }

        /**
         * @return event name
         */
        public String getEventName() {
            return this.eventName;
        }

        /**
         * @param eventName event name
         */
        public void setEventName(String eventName) {
            this.eventName = eventName;
        }

        /**
         * @param eventName event name
         * @return Record
         */
        public Record withEventName(String eventName) {
            setEventName(eventName);
            return this;
        }

        /**
         * @return event trigger config id
         */
        public String getEventTriggerConfigId() {
            return this.eventTriggerConfigId;
        }

        /**
         * @param eventTriggerConfigId event trigger config id
         */
        public void setEventTriggerConfigId(String eventTriggerConfigId) {
            this.eventTriggerConfigId = eventTriggerConfigId;
        }

        /**
         * @param eventTriggerConfigId event trigger config id
         * @return Record
         */
        public Record withEventTriggerConfigId(String eventTriggerConfigId) {
            setEventTriggerConfigId(eventTriggerConfigId);
            return this;
        }

        /**
         * @return event source arn
         */
        public String getEventSourceArn() {
            return this.eventSourceArn;
        }

        /**
         * @param eventSourceArn event source arn
         */
        public void setEventSourceArn(String eventSourceArn) {
            this.eventSourceArn = eventSourceArn;
        }

        /**
         * @param eventSourceArn event source arn
         * @return Record
         */
        public Record withEventSourceArn(String eventSourceArn) {
            setEventSourceArn(eventSourceArn);
            return this;
        }

        /**
         * @return user identity arn
         */
        public String getUserIdentityArn() {
            return this.userIdentityArn;
        }

        /**
         * @param userIdentityArn user identity arn
         */
        public void setUserIdentityArn(String userIdentityArn) {
            this.userIdentityArn = userIdentityArn;
        }

        /**
         * @param userIdentityArn user identity arn
         * @return Record
         */
        public Record withUserIdentityArn(String userIdentityArn) {
            setUserIdentityArn(userIdentityArn);
            return this;
        }

        /**
         * @return event source
         */
        public String getEventSource() {
            return this.eventSource;
        }

        /**
         * @param eventSource event source
         */
        public void setEventSource(String eventSource) {
            this.eventSource = eventSource;
        }

        /**
         * @param eventSource event source
         * @return Record
         */
        public Record withEventSource(String eventSource) {
            setEventSource(eventSource);
            return this;
        }

        /**
         * @return aws region
         */
        public String getAwsRegion() {
            return this.awsRegion;
        }

        /**
         * @param awsRegion aws region
         */
        public void setAwsRegion(String awsRegion) {
            this.awsRegion = awsRegion;
        }

        /**
         * @param awsRegion aws region
         * @return Record
         */
        public Record withAwsRegion(String awsRegion) {
            setAwsRegion(awsRegion);
            return this;
        }

        /**
         * @return event total parts
         */
        public Integer getEventTotalParts() {
            return this.eventTotalParts;
        }

        /**
         * @param eventTotalParts event total parts
         */
        public void setEventTotalParts(Integer eventTotalParts) {
            this.eventTotalParts = eventTotalParts;
        }

        /**
         * @param eventTotalParts event total parts
         * @return Record
         */
        public Record withEventTotalParts(Integer eventTotalParts) {
            setEventTotalParts(eventTotalParts);
            return this;
        }

        /**
         *
         * @return custom data
         */
        public String getCustomData(){ return this.customData;}

        /**
         *
         * @param customData event custom data
         */
        public void setCustomData(String customData) { this.customData = customData;}

        /**
         * @param customData event
         * @return Record
         */
        public Record withCustomData(String customData) {
            setCustomData(customData);
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
            if (getEventId() != null)
                sb.append("eventId: ").append(getEventId()).append(",");
            if (getEventVersion() != null)
                sb.append("eventVersion: ").append(getEventVersion()).append(",");
            if (getEventTime() != null)
                sb.append("eventTime: ").append(getEventTime().toString()).append(",");
            if (getEventTriggerName() != null)
                sb.append("eventTriggerName: ").append(getEventTriggerName()).append(",");
            if (getEventPartNumber() != null)
                sb.append("eventPartNumber: ").append(getEventPartNumber().toString()).append(",");
            if (getCodeCommit() != null)
                sb.append("codeCommit: ").append(getCodeCommit().toString()).append(",");
            if (getEventName() != null)
                sb.append("eventName: ").append(getEventName()).append(",");
            if (getEventTriggerConfigId() != null)
                sb.append("eventTriggerConfigId: ").append(getEventTriggerConfigId()).append(",");
            if (getEventSourceArn() != null)
                sb.append("eventSourceArn: ").append(getEventSourceArn()).append(",");
            if (getUserIdentityArn() != null)
                sb.append("userIdentityArn: ").append(getUserIdentityArn()).append(",");
            if (getEventSource() != null)
                sb.append("eventSource: ").append(getEventSource()).append(",");
            if (getAwsRegion() != null)
                sb.append("awsRegion: ").append(getAwsRegion()).append(",");
            if (getCustomData() != null)
                sb.append("customData: ").append(getCustomData()).append(",");
            if (getEventTotalParts() != null)
                sb.append("eventTotalParts: ").append(getEventTotalParts());
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
            if (other.getEventId() == null ^ this.getEventId() == null)
                return false;
            if (other.getEventId() != null && other.getEventId().equals(this.getEventId()) == false)
                return false;
            if (other.getEventVersion() == null ^ this.getEventVersion() == null)
                return false;
            if (other.getEventVersion() != null && other.getEventVersion().equals(this.getEventVersion()) == false)
                return false;
            if (other.getEventTime() == null ^ this.getEventTime() == null)
                return false;
            if (other.getEventTime() != null && other.getEventTime().equals(this.getEventTime()) == false)
                return false;
            if (other.getEventTriggerName() == null ^ this.getEventTriggerName() == null)
                return false;
            if (other.getEventTriggerName() != null && other.getEventTriggerName().equals(this.getEventTriggerName()) == false)
                return false;
            if (other.getEventPartNumber() == null ^ this.getEventPartNumber() == null)
                return false;
            if (other.getEventPartNumber() != null && other.getEventPartNumber().equals(this.getEventPartNumber()) == false)
                return false;
            if (other.getCodeCommit() == null ^ this.getCodeCommit() == null)
                return false;
            if (other.getCodeCommit() != null && other.getCodeCommit().equals(this.getCodeCommit()) == false)
                return false;
            if (other.getEventName() == null ^ this.getEventName() == null)
                return false;
            if (other.getEventName() != null && other.getEventName().equals(this.getEventName()) == false)
                return false;
            if (other.getEventTriggerConfigId() == null ^ this.getEventTriggerConfigId() == null)
                return false;
            if (other.getEventTriggerConfigId() != null && other.getEventTriggerConfigId().equals(this.getEventTriggerConfigId()) == false)
                return false;
            if (other.getEventSourceArn() == null ^ this.getEventSourceArn() == null)
                return false;
            if (other.getEventSourceArn() != null && other.getEventSourceArn().equals(this.getEventSourceArn()) == false)
                return false;
            if (other.getUserIdentityArn() == null ^ this.getUserIdentityArn() == null)
                return false;
            if (other.getUserIdentityArn() != null && other.getUserIdentityArn().equals(this.getUserIdentityArn()) == false)
                return false;
            if (other.getEventSource() == null ^ this.getEventSource() == null)
                return false;
            if (other.getEventSource() != null && other.getEventSource().equals(this.getEventSource()) == false)
                return false;
            if (other.getAwsRegion() == null ^ this.getAwsRegion() == null)
                return false;
            if (other.getAwsRegion() != null && other.getAwsRegion().equals(this.getAwsRegion()) == false)
                return false;
            if (other.getEventTotalParts() == null ^ this.getEventTotalParts() == null)
                return false;
            if (other.getEventTotalParts() != null && other.getEventTotalParts().equals(this.getEventTotalParts()) == false)
                return false;
            if (other.getCustomData() == null ^ this.getCustomData() == null)
                return false;
            if (other.getCustomData() != null && other.getCustomData().equals(this.getCustomData()) == false)
                return false;
            return true;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int hashCode = 1;

            hashCode = prime * hashCode + ((getEventId() == null) ? 0 : getEventId().hashCode());
            hashCode = prime * hashCode + ((getEventVersion() == null) ? 0 : getEventVersion().hashCode());
            hashCode = prime * hashCode + ((getEventTime() == null) ? 0 : getEventTime().hashCode());
            hashCode = prime * hashCode + ((getEventTriggerName() == null) ? 0 : getEventTriggerName().hashCode());
            hashCode = prime * hashCode + ((getEventPartNumber() == null) ? 0 : getEventPartNumber().hashCode());
            hashCode = prime * hashCode + ((getCodeCommit() == null) ? 0 : getCodeCommit().hashCode());
            hashCode = prime * hashCode + ((getEventName() == null) ? 0 : getEventName().hashCode());
            hashCode = prime * hashCode + ((getEventTriggerConfigId() == null) ? 0 : getEventTriggerConfigId().hashCode());
            hashCode = prime * hashCode + ((getEventSourceArn() == null) ? 0 : getEventSourceArn().hashCode());
            hashCode = prime * hashCode + ((getUserIdentityArn() == null) ? 0 : getUserIdentityArn().hashCode());
            hashCode = prime * hashCode + ((getEventSource() == null) ? 0 : getEventSource().hashCode());
            hashCode = prime * hashCode + ((getAwsRegion() == null) ? 0 : getAwsRegion().hashCode());
            hashCode = prime * hashCode + ((getEventTotalParts() == null) ? 0 : getEventTotalParts().hashCode());
            hashCode = prime * hashCode + ((getCustomData() == null) ? 0 : getCustomData().hashCode());
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
    public CodeCommitEvent() {}

    /**
     * @return records
     */
    public List<Record> getRecords() {
        return this.records;
    }

    /**
     * @param records records
     */
    public void setRecords(List<Record> records) {
        this.records = records;
    }

    /**
     * @param records records
     * @return CodeCommitEvent
     */
    public CodeCommitEvent withRecords(List<Record> records) {
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

        if (obj instanceof CodeCommitEvent == false)
            return false;
        CodeCommitEvent other = (CodeCommitEvent) obj;
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

        hashCode = prime * hashCode + ((getRecords() == null) ? 0 : getRecords().hashCode());
        return hashCode;
    }

    @Override
    public CodeCommitEvent clone() {
        try {
            return (CodeCommitEvent) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new IllegalStateException("Got a CloneNotSupportedException from Object.clone()", e);
        }
    }
}
