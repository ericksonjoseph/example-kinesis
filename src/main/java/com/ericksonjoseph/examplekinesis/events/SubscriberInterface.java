
package com.ericksonjoseph.examplekinesis.events;

import com.ericksonjoseph.examplekinesis.app.AppContext;

public interface SubscriberInterface {

    public void subscriberUpdate(String eventName, Object data, AppContext context);
}
