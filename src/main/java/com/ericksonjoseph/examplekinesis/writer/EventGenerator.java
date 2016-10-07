
package com.ericksonjoseph.examplekinesis.writer;

import java.util.Date;
import java.util.UUID;

import com.ericksonjoseph.examplekinesis.model.Event;

/**
 * Generates random stock trades by picking randomly from a collection of stocks, assigning a
 * random price based on the mean, and picking a random quantity for the shares.
 *
 */

public class EventGenerator {

    /**
     * Return a random stock trade with a unique id every time.
     *
     */
    public Event getRandomTrade() {

        String jid = UUID.randomUUID().toString();
        String tid = UUID.randomUUID().toString();

        Date date= new Date();
        long timestamp = System.currentTimeMillis() / 1000L;

        Event s = new Event();
        s.setOrigin("content-provider");
        s.setJobId(jid);
        s.setTaskId(tid);
        s.setCreatedAt(timestamp);
        s.setCreatedBy("EventGenerator");
        s.setMessageCode(0);
        s.setData("{jobName: \"Ingest Data\", data : { status: \"active\" }}");

        return s;
    }

}
