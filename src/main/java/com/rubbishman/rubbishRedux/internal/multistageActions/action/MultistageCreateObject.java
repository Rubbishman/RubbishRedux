package com.rubbishman.rubbishRedux.internal.multistageActions.action;

import com.rubbishman.rubbishRedux.internal.createObjectCallback.interfaces.ICreateObjectCallback;

public class MultistageCreateObject<T> extends MultistageAction {
    public final T createObject;
    public transient final ICreateObjectCallback callback;

    public MultistageCreateObject(T createObject, ICreateObjectCallback callback) {
        this.createObject = createObject;
        this.callback = callback;
    }
}
