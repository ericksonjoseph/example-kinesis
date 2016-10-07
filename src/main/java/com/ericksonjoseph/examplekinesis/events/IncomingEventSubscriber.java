
package com.ericksonjoseph.examplekinesis.events;

import com.ericksonjoseph.examplekinesis.app.AppContext;
import com.ericksonjoseph.examplekinesis.model.Event;
import com.ericksonjoseph.examplekinesis.model.JobDocument;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Map;

public class IncomingEventSubscriber extends Subscriber implements SubscriberInterface{

    public void subscriberUpdate(String eventName, Object data, AppContext context) {

        if (!(data instanceof Event)) {
            context.logger.error("This subscriber " + this.getClass().getName() + " can only handle an Event object as data");
            return;
        }

        Event event = (Event) data;

        String jid = event.getJobId();

        // Fetch Document
        Map<String, Object> jobDocument = null;
        jobDocument = context.db.getRecord("jobs", jid);

        // If Document does not exist
        if (jobDocument == null) {

            // Create New Job Record
            JobDocument job = createNewJobFromEvent(event);

            if (!context.db.saveRecord("jobs", job.getJobId(), job.toHashMap())) {
                context.logger.error("Failed to save record");
            }
        }

        context.events.publish("messageForJob", (Event) data);

    }

    public static JobDocument createNewJobFromEvent(Event event) {

        JobDocument job = new JobDocument();

        job.setJobId(event.getJobId());
        job.setCreationEvent(event);
        job.step = "one";

        return job;
    }
}
