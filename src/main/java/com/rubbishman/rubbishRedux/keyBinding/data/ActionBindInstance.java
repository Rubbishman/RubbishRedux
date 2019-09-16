package com.rubbishman.rubbishRedux.keyBinding.data;

public class ActionBindInstance {
    public final boolean pressed;
    public final ActionBind actionBind;

    public ActionBindInstance(boolean pressed, ActionBind actionBind) {
        this.pressed = pressed;
        this.actionBind = actionBind;
    }
}
