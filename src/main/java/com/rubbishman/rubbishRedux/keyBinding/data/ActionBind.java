package com.rubbishman.rubbishRedux.keyBinding.data;

import com.rubbishman.rubbishRedux.keyBinding.BindType;

public class ActionBind {
    public final Object action;
    public final BindType bindType;

    public ActionBind(Object action, BindType bindType) {
        this.action = action;
        this.bindType = bindType;
    }
}
