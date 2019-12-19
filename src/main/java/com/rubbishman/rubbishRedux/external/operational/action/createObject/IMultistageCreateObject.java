package com.rubbishman.rubbishRedux.external.operational.action.createObject;

import com.rubbishman.rubbishRedux.external.operational.action.createObject.ICreateObjectCallback;
import com.rubbishman.rubbishRedux.external.operational.action.multistageAction.IMultistageAction;

public class IMultistageCreateObject<T> implements IMultistageAction {
    public final T createObject;
    public transient final ICreateObjectCallback callback;

    public IMultistageCreateObject(T createObject, ICreateObjectCallback callback) {
        this.createObject = createObject;
        this.callback = callback;
    }
}
