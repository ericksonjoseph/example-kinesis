
package com.ericksonjoseph.examplekinesis.model;

import java.io.IOException;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Captures the key elements of a stock trade, such as the ticker symbol, price,
 * number of shares, the type of the trade (buy or sell), and an id uniquely identifying
 * the trade.
 */
public class Event {

    private final static ObjectMapper JSON = new ObjectMapper();
    static {
        JSON.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    private String origin;
    private String jobId;
    private String taskId;
    private long createdAt;
    private String createdBy;
    private int messageCode;
    private String data;

    public Event() {
    }

    public Event setOrigin(String origin) {
        this.origin = origin;
        return this;
    }

    public String getOrigin() {
        return this.origin;
    }

    public Event setJobId(String jobId) {
        this.jobId = jobId;
        return this;
    }

    public String getJobId() {
        return this.jobId;
    }

    public Event setTaskId(String taskId) {
        this.taskId = taskId;
        return this;
    }

    public String getTaskId() {
        return this.taskId;
    }

    public Event setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public long getCreatedAt() {
        return this.createdAt;
    }

    public Event setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public String getCreatedBy() {
        return this.createdBy;
    }

    public Event setMessageCode(int messageCode) {
        this.messageCode = messageCode;
        return this;
    }

    public int getMessageCode() {
        return this.messageCode;
    }

    public Event setData(String data) {
        this.data = data;
        return this;
    }

    public String getData() {
        return this.data;
    }

    public byte[] toJsonAsBytes() {
        try {
            return JSON.writeValueAsBytes(this);
        } catch (IOException e) {
            return null;
        }
    }

    public static Event fromJsonAsBytes(byte[] bytes) {
        try {
            return JSON.readValue(bytes, Event.class);
        } catch (IOException e) {
            return null;
        }
    }

    // This is needed to use event in string context like when pushing to a queue
    @Override
    public String toString() {
        try {
            return JSON.writeValueAsString(this);
        } catch (IOException e) {
            return null;
        }
    }

}
