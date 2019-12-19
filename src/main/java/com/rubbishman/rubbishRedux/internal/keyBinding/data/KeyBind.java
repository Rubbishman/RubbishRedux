package com.rubbishman.rubbishRedux.internal.keyBinding.data;

public class KeyBind {
    public final String keyCode;
    public final Bind bind;

    public KeyBind(String keyCode, Bind bind) {
        this.keyCode = keyCode;
        this.bind = bind;
    }
}