package com.anos.covid19.eventbus.event;

public abstract class DataEventAction<T> extends EventAction {

    T data;

    public DataEventAction(T data) {
        this.data = data;
    }

    public T getData() {
        return data;
    }
}
