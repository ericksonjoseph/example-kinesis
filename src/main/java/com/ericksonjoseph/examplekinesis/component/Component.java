
package com.ericksonjoseph.examplekinesis.component;

import com.ericksonjoseph.examplekinesis.app.AppContext;
import com.ericksonjoseph.examplekinesis.queue.QueueInterface;
import com.ericksonjoseph.examplekinesis.database.Arango;
import com.ericksonjoseph.examplekinesis.model.Event;

public class Component {

    public static void main(String[] args) throws InterruptedException {

        System.out.println("Starting component");

        AppContext app = new AppContext();
        app.queue = getQueueObject();

        while (true) {
            Event event = app.queue.pop(args[0]);

            if (event != null) {

                System.out.println("WORKING ON JOB: " + event.getJobId());
                event.setData("{component: \"comp\", data: \"Data from the component\"}");
                event.setMessageCode(1);

                Thread.sleep(4000);

                System.out.println("DONE WITH JOB: " + event.getJobId());

                app.queue.push("kinesis", event);
            }
            Thread.sleep(1000);
        }
    }

    private static QueueInterface getQueueObject() {

        QueueInterface arango = new Arango();

        return arango;
    }
}
