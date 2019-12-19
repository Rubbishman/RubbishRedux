package com.rubbishman.rubbishRedux.external.operational.action.createObject;

public class CreateObject<T> {
    public final T createObject;
    public transient final ICreateObjectCallback callback;

    public CreateObject(T createObject, ICreateObjectCallback callback) {
        this.createObject = createObject;
        this.callback = callback;
    }
}
