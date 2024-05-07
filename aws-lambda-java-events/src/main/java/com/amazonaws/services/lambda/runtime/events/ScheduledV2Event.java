/*
 * Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.
 * SPDX-License-Identifier: Apache-2.0
 */

package com.amazonaws.services.lambda.runtime.events;

import org.joda.time.DateTime;

import java.io.Serializable;
import java.util.List;

/**
 * represents a scheduled V2 event
 */
public class ScheduledV2Event implements Serializable, Cloneable {

    private static final long serialVersionUID = -463442139623175611L;

    private String version;

    private String account;

    private String region;

    private String detail;

    private String detailType;

    private String source;

    private String id;

    private DateTime time;

    private List<String> resources;

    /**
     * default constructor
     */
    public ScheduledV2Event() {
    }

    /**
     * @return the version number
     */
    public String getVersion() {
        return version;
    }

    /**
     * @param version the version number
     */
    public void setVersion(String version) {
        this.version = version;
    }

    /**
     * @param version version number
     * @return ScheduledV2Event
     */
    public ScheduledV2Event withVersion(String version) {
        setVersion(version);
        return this;
    }

    /**
     * @return the account id
     */
    public String getAccount() {
        return account;
    }

    /**
     * @param account the account id
     */
    public void setAccount(String account) {
        this.account = account;
    }

    /**
     * @param account account id
     * @return ScheduledV2Event
     */
    public ScheduledV2Event withAccount(String account) {
        setAccount(account);
        return this;
    }

    /**
     * @return the aws region
     */
    public String getRegion() {
        return region;
    }

    /**
     * @param region the aws region
     */
    public void setRegion(String region) {
        this.region = region;
    }

    /**
     * @param region aws region
     * @return ScheduledV2Event
     */
    public ScheduledV2Event withRegion(String region) {
        setRegion(region);
        return this;
    }

    /**
     * @return The details of the events (usually left blank)
     */
    public String getDetail() {
        return detail;
    }

    /**
     * @param detail The details of the events (usually left blank)
     */
    public void setDetail(String detail) {
        this.detail = detail;
    }

    /**
     * @param detail details of the events (usually left blank)
     * @return ScheduledV2Event
     */
    public ScheduledV2Event withDetail(String detail) {
        setDetail(detail);
        return this;
    }

    /**
     * @return The details type - see cloud watch events for more info
     */
    public String getDetailType() {
        return detailType;
    }

    /**
     * @param detailType The details type - see cloud watch events for more info
     */
    public void setDetailType(String detailType) {
        this.detailType = detailType;
    }

    /**
     * @param detailType The details type - see cloud watch events for more info
     * @return ScheduledV2Event
     */
    public ScheduledV2Event withDetailType(String detailType) {
        setDetailType(detailType);
        return this;
    }

    /**
     * @return the source of the event
     */
    public String getSource() {
        return source;
    }

    /**
     * @param source the source of the event
     */
    public void setSource(String source) {
        this.source = source;
    }

    /**
     * @param source source of the event
     * @return ScheduledV2Event
     */
    public ScheduledV2Event withSource(String source) {
        setSource(source);
        return this;
    }

    /**
     * @return the timestamp for when the event is scheduled
     */
    public DateTime getTime() {
        return this.time;
    }

    /**
     * @param time the timestamp for when the event is scheduled
     */
    public void setTime(DateTime time) {
        this.time = time;
    }

    /**
     * @param time the timestamp for when the event is scheduled
     * @return ScheduledV2Event
     */
    public ScheduledV2Event withTime(DateTime time) {
        setTime(time);
        return this;
    }

    /**
     * @return the id of the event
     */
    public String getId() {
        return id;
    }

    /**
     * @param id the id of the event
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @param id id of event
     * @return ScheduledV2Event
     */
    public ScheduledV2Event withId(String id) {
        setId(id);
        return this;
    }

    /**
     * @return the resources used by event
     */
    public List<String> getResources() {
        return this.resources;
    }

    /**
     * @param resources the resources used by event
     */
    public void setResources(List<String> resources) {
        this.resources = resources;
    }

    /**
     * @param resources list of resource names
     * @return Scheduled V2 event object
     */
    public ScheduledV2Event withResources(List<String> resources) {
        setResources(resources);
        return this;
    }

    /**
     * Returns a string representation of this object; useful for testing and debugging.
     *
     * @return A string representation of this object.
     * @see Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        if (getVersion() != null)
            sb.append("version: ").append(getVersion()).append(",");
        if (getAccount() != null)
            sb.append("account: ").append(getAccount()).append(",");
        if (getRegion() != null)
            sb.append("region: ").append(getRegion()).append(",");
        if (getDetail() != null)
            sb.append("detail: ").append(getDetail()).append(",");
        if (getDetailType() != null)
            sb.append("detailType: ").append(getDetailType()).append(",");
        if (getSource() != null)
            sb.append("source: ").append(getSource()).append(",");
        if (getId() != null)
            sb.append("id: ").append(getId()).append(",");
        if (getTime() != null)
            sb.append("time: ").append(getTime().toString()).append(",");
        if (getResources() != null)
            sb.append("resources: ").append(getResources());
        sb.append("}");
        return sb.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;

        if (obj instanceof ScheduledV2Event == false)
            return false;
        ScheduledV2Event other = (ScheduledV2Event) obj;
        if (other.getVersion() == null ^ this.getVersion() == null)
            return false;
        if (other.getVersion() != null && other.getVersion().equals(this.getVersion()) == false)
            return false;
        if (other.getAccount() == null ^ this.getAccount() == null)
            return false;
        if (other.getAccount() != null && other.getAccount().equals(this.getAccount()) == false)
            return false;
        if (other.getRegion() == null ^ this.getRegion() == null)
            return false;
        if (other.getRegion() != null && other.getRegion().equals(this.getRegion()) == false)
            return false;
        if (other.getDetail() == null ^ this.getDetail() == null)
            return false;
        if (other.getDetail() != null && other.getDetail().equals(this.getDetail()) == false)
            return false;
        if (other.getDetailType() == null ^ this.getDetailType() == null)
            return false;
        if (other.getDetailType() != null && other.getDetailType().equals(this.getDetailType()) == false)
            return false;
        if (other.getSource() == null ^ this.getSource() == null)
            return false;
        if (other.getSource() != null && other.getSource().equals(this.getSource()) == false)
            return false;
        if (other.getId() == null ^ this.getId() == null)
            return false;
        if (other.getId() != null && other.getId().equals(this.getId()) == false)
            return false;
        if (other.getTime() == null ^ this.getTime() == null)
            return false;
        if (other.getTime() != null && other.getTime().equals(this.getTime()) == false)
            return false;
        if (other.getResources() == null ^ this.getResources() == null)
            return false;
        if (other.getResources() != null && other.getResources().equals(this.getResources()) == false)
            return false;
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int hashCode = 1;

        hashCode = prime * hashCode + ((getVersion() == null) ? 0 : getVersion().hashCode());
        hashCode = prime * hashCode + ((getAccount() == null) ? 0 : getAccount().hashCode());
        hashCode = prime * hashCode + ((getRegion() == null) ? 0 : getRegion().hashCode());
        hashCode = prime * hashCode + ((getDetail() == null) ? 0 : getDetail().hashCode());
        hashCode = prime * hashCode + ((getDetailType() == null) ? 0 : getDetailType().hashCode());
        hashCode = prime * hashCode + ((getSource() == null) ? 0 : getSource().hashCode());
        hashCode = prime * hashCode + ((getId() == null) ? 0 : getId().hashCode());
        hashCode = prime * hashCode + ((getTime() == null) ? 0 : getTime().hashCode());
        hashCode = prime * hashCode + ((getResources() == null) ? 0 : getResources().hashCode());
        return hashCode;
    }

    @Override
    public ScheduledV2Event clone() {
        try {
            return (ScheduledV2Event) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new IllegalStateException("Got a CloneNotSupportedException from Object.clone()", e);
        }
    }

}
