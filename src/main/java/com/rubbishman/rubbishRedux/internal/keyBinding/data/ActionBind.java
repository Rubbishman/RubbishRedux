package com.rubbishman.rubbishRedux.internal.keyBinding.data;

import com.rubbishman.rubbishRedux.internal.keyBinding.BindType;

public class ActionBind {
    public final Object action;
    public final BindType bindType;

    public ActionBind(Object action, BindType bindType) {
        this.action = action;
        this.bindType = bindType;
    }
}
