package com.rubbishman.rubbishRedux.external.operational.action.createObject;

public abstract class IMultistageCreateObject<T> {
    public final T createObject;
    public transient final ICreateObjectCallback callback;

    protected IMultistageCreateObject(T createObject, ICreateObjectCallback callback) {
        this.createObject = createObject;
        this.callback = callback;
    }
}
