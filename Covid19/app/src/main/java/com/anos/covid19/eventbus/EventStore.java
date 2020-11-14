package com.anos.covid19.eventbus;

import com.anos.covid19.eventbus.event.DataEventAction;
import com.anos.covid19.eventbus.event.EventAction;

import org.greenrobot.eventbus.EventBus;

public class EventStore {

    private EventBus eventBus;
    private static EventStore instance;

    public static EventStore getInstance() {
        if (instance == null) {
            instance = new EventStore();
        }
        return instance;
    }

    private EventStore() {
        eventBus = EventBus.getDefault();
    }

    public void postEventAction(final String key) {
        eventBus.post(new EventAction() {
            @Override
            public String getKey() {
                return key;
            }
        });
    }

    public void postEventAction(final String key, final Object data) {
        eventBus.post(new DataEventAction<Object>(data) {
            @Override
            public String getKey() {
                return key;
            }
        });
    }
}
