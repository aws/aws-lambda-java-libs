package com.amazonaws.services.lambda.runtime.events;

import java.util.List;

/**
 * Represents an Amazon scheduled event sent to Lambda Functions
 */
public class ScheduledEvent {

    private String account;

    private Object detail;

    private String detailType;

    private String id;

    private String region;

    private List<String> resources;

    private String source;

    private String time;

    /**
     * Gets the event account
     *
     */
    public String getAccount() {
        return account;
    }

    /**
     * Sets the event account
     * @param account A string containing the event account
     */
    public void setAccount(String account) {
        this.account = account;
    }

    /**
     * Gets the event detail
     *
     */
    public Object getDetail() {
        return detail;
    }

    /**
     * Sets the event detail
     * @param detail An object containing the event detail
     */
    public void setDetail(Object detail) {
        this.detail = detail;
    }

    /**
     * Gets the event detail type
     *
     */
    public String getDetailType() {
        return detailType;
    }

    /**
     * Sets the event detail type
     * @param detailType A string containing the event detail type
     */
    public void setDetailType(String detailType) {
        this.detailType = detailType;
    }

    /**
     * Gets the event ID
     *
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the event ID
     * @param id A string containing the event ID
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Gets the event region
     *
     */
    public String getRegion() {
        return region;
    }

    /**
     * Sets the event region
     * @param region A string containing the event region
     */
    public void setRegion(String region) {
        this.region = region;
    }

    /**
     * Gets the event resources
     *
     */
    public List<String> getResources() {
        return resources;
    }

    /**
     * Sets the event resources
     * @param resources A list of strings containing the event resources
     */
    public void setResources(List<String> resources) {
        this.resources = resources;
    }

    /**
     * Gets the event source
     *
     */
    public String getSource() {
        return source;
    }

    /**
     * Sets the event source
     * @param source A string containing the event source
     */
    public void setSource(String source) {
        this.source = source;
    }

    /**
     * Gets the event time
     *
     */
    public String getTime() {
        return time;
    }

    /**
     * Sets the event time
     * @param time A string containing the event time
     */
    public void setTime(String time) {
        this.time = time;
    }
}
