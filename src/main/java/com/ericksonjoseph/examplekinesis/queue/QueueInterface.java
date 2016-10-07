
package com.ericksonjoseph.examplekinesis.queue;

import com.ericksonjoseph.examplekinesis.model.Event;

public interface QueueInterface {

    public int push(String queue, Event data);

    public Event pop(String queue);

}
