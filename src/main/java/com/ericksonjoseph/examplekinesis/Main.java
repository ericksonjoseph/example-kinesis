
package com.ericksonjoseph.examplekinesis;

import java.util.Date;
import java.util.UUID;

import com.ericksonjoseph.examplekinesis.events.Events;
import com.ericksonjoseph.examplekinesis.events.JobUpdateSubscriber;
import com.ericksonjoseph.examplekinesis.events.IncomingEventSubscriber;
import com.ericksonjoseph.examplekinesis.model.Event;
import com.ericksonjoseph.examplekinesis.app.AppContext;

import com.ericksonjoseph.examplekinesis.database.DatabaseInterface;
import com.ericksonjoseph.examplekinesis.queue.QueueInterface;
import com.ericksonjoseph.examplekinesis.database.Arango;

public class Main {

    public static void main(String[] args) throws InterruptedException {

        AppContext app = new AppContext();
        app.events = new Events(app);
        app.db = getDbObject();
        app.queue = getQueueObject();

        app.events.registerSubscriber("incoming", new IncomingEventSubscriber());
        app.events.registerSubscriber("messageForJob", new JobUpdateSubscriber());

        while (true) {
            //app.events.publish("incoming", generateMessage());
            Event event = app.queue.pop("kinesis");
            if (event != null) {
                app.events.publish("incoming", event);
            }
            Thread.sleep(1000);
        }
    }

    private static Event generateMessage() {

        String jid = "2727bee0-bb98-4971-8e36-624de67d6d41"; //UUID.randomUUID().toString();
        String tid = UUID.randomUUID().toString();

        long timestamp = System.currentTimeMillis() / 1000L;

        Event event = new Event();
        event.setOrigin("content-provider");
        event.setJobId(jid);
        event.setTaskId(tid);
        event.setCreatedAt(timestamp);
        event.setCreatedBy("EventGenerator");
        event.setMessageCode(0);
        event.setData("{jobName: \"Ingest Data\", data : { status: \"active\" }}");

        return event;
    }

    private static DatabaseInterface getDbObject() {

        DatabaseInterface arango = new Arango();

        return arango;
    }

    private static QueueInterface getQueueObject() {

        QueueInterface arango = new Arango();

        return arango;
    }
}
