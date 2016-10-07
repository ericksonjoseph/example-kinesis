
package com.ericksonjoseph.examplekinesis.app;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.ericksonjoseph.examplekinesis.events.Events;
import com.ericksonjoseph.examplekinesis.database.DatabaseInterface;
import com.ericksonjoseph.examplekinesis.queue.QueueInterface;

public class AppContext {

    /**
     * Pub/Sub system
     */
    public Events events;

    public DatabaseInterface db;

    public QueueInterface queue;

    public static final Log logger = LogFactory.getLog(AppContext.class);
}
