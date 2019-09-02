package com.rubbishman.rubbishRedux.createObjectCallback.action;

import com.rubbishman.rubbishRedux.createObjectCallback.interfaces.ICreateObjectCallback;

public class CreateObject {
    public Object createObject;
    public ICreateObjectCallback callback;

    public String toString() {
        return "[CREATE:" + createObject + "]";
    }
}
