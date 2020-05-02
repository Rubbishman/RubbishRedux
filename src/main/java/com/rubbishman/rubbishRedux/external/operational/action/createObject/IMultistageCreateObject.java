package com.rubbishman.rubbishRedux.external.operational.action.createObject;

public class IMultistageCreateObject<T> {
    public final T createObject;
    public transient final ICreateObjectCallback callback;

    public IMultistageCreateObject(T createObject, ICreateObjectCallback callback) {
        this.createObject = createObject;
        this.callback = callback;
    }
}
