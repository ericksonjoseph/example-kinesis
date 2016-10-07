
package com.ericksonjoseph.examplekinesis.events;

import com.ericksonjoseph.examplekinesis.app.AppContext;
import com.ericksonjoseph.examplekinesis.model.Event;
import java.util.Map;
import java.util.HashMap;

public class JobUpdateSubscriber extends Subscriber implements SubscriberInterface{

    public void subscriberUpdate(String eventName, Object data, AppContext app) {

        if (!(data instanceof Event)) {
            app.logger.error("This subscriber " + this.getClass().getName() + " can only handle an Event object as data");
            return;
        }

        Event evt = (Event) data;

        // Get Type of message
        int messageCode = evt.getMessageCode();

        switch (messageCode) {

            case 1:
                System.out.println("Job event Handle task completion");
                break;

            default:

                System.out.println("Job event Handle default");
                String jid = evt.getJobId();

                // Fetch Document
                Map<String, Object> jobDocument = null;
                jobDocument = app.db.getRecord("jobs", jid);
                Object step = jobDocument.get("step");

                if (step.equals("one")) {

                    System.out.println("Pushing job " + jid + " to step 2");

                    app.queue.push("verify", evt);

                    HashMap updates = new HashMap();
                    updates.put("step", "two");            

                    app.db.updateDocument("jobs", jobDocument.get("jobId").toString(), updates);            
                }
        }

        // Query DB for job
        // Query for jobs workflow
        // Look at jobs current step
        // Look at jobs tasks

    }
}
