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

import org.joda.time.DateTime;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * represents a scheduled event
 */
public class ScheduledEvent implements Serializable, Cloneable {

    private static final long serialVersionUID = -5810383198587331146L;

    private String account;

    private String region;

    private Map<String, Object> detail;

    private String detailType;

    private String source;

    private String id;

    private DateTime time;

    private List<String> resources;

    /**
     * default constructor
     */
    public ScheduledEvent() {}

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
     * @return ScheduledEvent
     */
    public ScheduledEvent withAccount(String account) {
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
     * @return ScheduledEvent
     */
    public ScheduledEvent withRegion(String region) {
        setRegion(region);
        return this;
    }
    
    /**
     * @return The details of the events (usually left blank)
     */
    public Map<String, Object> getDetail() {
        return detail;
    }

    /**
     * @param detail The details of the events (usually left blank)
     */
    public void setDetail(Map<String, Object> detail) {
        this.detail = detail;
    }

    /**
     * @param detail details of the events (usually left blank)
     * @return ScheduledEvent
     */
    public ScheduledEvent withDetail(Map<String, Object> detail) {
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
     * @return ScheduledEvent
     */
    public ScheduledEvent withDetailType(String detailType) {
        setDetailType(detailType);
        return this;
    }
    
    /**
     * @return the soruce of the event
     */
    public String getSource() {
        return source;
    }

    /**
     * @param soruce the soruce of the event
     */
    public void setSource(String soruce) {
        this.source = soruce;
    }

    /**
     * @param source source of the event
     * @return ScheduledEvent
     */
    public ScheduledEvent withSource(String source) {
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
     * @return ScheduledEvent
     */
    public ScheduledEvent withTime(DateTime time) {
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
     * @return ScheduledEvent
     */
    public ScheduledEvent withId(String id) {
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
     * @return Scheduled event object
     */
    public ScheduledEvent withResources(List<String> resources) {
        setResources(resources);
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
        if (getAccount() != null)
            sb.append("account: ").append(getAccount()).append(",");
        if (getRegion() != null)
            sb.append("region: ").append(getRegion()).append(",");
        if (getDetail() != null)
            sb.append("detail: ").append(getDetail().toString()).append(",");
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

        if (obj instanceof ScheduledEvent == false)
            return false;
        ScheduledEvent other = (ScheduledEvent) obj;
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
    public ScheduledEvent clone() {
        try {
            return (ScheduledEvent) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new IllegalStateException("Got a CloneNotSupportedException from Object.clone()", e);
        }
    }
    
}
