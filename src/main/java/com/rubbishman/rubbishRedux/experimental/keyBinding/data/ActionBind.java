package com.rubbishman.rubbishRedux.experimental.keyBinding.data;

import com.rubbishman.rubbishRedux.experimental.keyBinding.BindType;

public class ActionBind {
    public final Object action;
    public final BindType bindType;

    public ActionBind(Object action, BindType bindType) {
        this.action = action;
        this.bindType = bindType;
    }
}
