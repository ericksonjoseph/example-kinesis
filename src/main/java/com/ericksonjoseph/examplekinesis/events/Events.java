
package com.ericksonjoseph.examplekinesis.events;

import com.ericksonjoseph.examplekinesis.app.AppContext;

import java.util.ArrayList;
import java.util.HashMap;

public class Events {

    private HashMap<String, ArrayList<SubscriberInterface>> subscribers;

    private AppContext app;

    public Events(AppContext app) {
        this.subscribers = new HashMap<String, ArrayList<SubscriberInterface>>();
        this.app = app;
    }

    public void registerSubscriber(String queue, SubscriberInterface subscriber) {

        ArrayList<SubscriberInterface> list = this.subscribers.get(queue);

        if (list == null) {
            list = new ArrayList<SubscriberInterface>();
            this.subscribers.put(queue, list);
        }

        list.add(subscriber);
    }

    public void publish(String queue, Object data) {

        ArrayList<SubscriberInterface> subscribers = this.subscribers.get(queue);

        for (SubscriberInterface subscriber : subscribers) {
            subscriber.subscriberUpdate(queue, data, this.app);
        }
    }
}
