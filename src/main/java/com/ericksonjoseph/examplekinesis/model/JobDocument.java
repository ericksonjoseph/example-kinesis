
package com.ericksonjoseph.examplekinesis.model;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.ericksonjoseph.examplekinesis.model.Event;
import com.ericksonjoseph.examplekinesis.model.DBDocument;
import java.util.Map;
import java.util.HashMap;
import java.lang.reflect.Field;

public class JobDocument extends DBDocument {

    private static final Log LOG = LogFactory.getLog(JobDocument.class);

    public String jobId;
    public Event creationEvent;
    public String step;

    private static HashMap<String, String> schema;

    public static void JobDocument() {
        System.out.println("Constructing Job");
    }

    public HashMap<String, String> getSchema() {

        if (null == this.schema) {
            schema = new HashMap<String, String>();

            schema.put("jobId", "String");
            schema.put("creationEvent", "Event");
            schema.put("step", "String");

            this.schema = schema;
        }

        return this.schema;
    }

    public JobDocument setCreationEvent(Event creationEvent) {
        this.creationEvent = creationEvent;
        return this;
    }

    public Event getCreationEvent() {
        return this.creationEvent;
    }

    public JobDocument setJobId(String jobId) {
        this.jobId = jobId;
        return this;
    }

    public String getJobId() {
        return this.jobId;
    }
}
