package com.rubbishman.rubbishRedux.internal.keyBinding.data;

public class Bind {
    public final ActionBind baseKeyAction;

    public final ActionBind shiftKeyAction;
    public final ActionBind ctrlKeyAction;
    public final ActionBind bothKeyAction;

    public Bind(ActionBind baseKeyAction) {
        this.baseKeyAction = baseKeyAction;
        this.shiftKeyAction = null;
        this.ctrlKeyAction = null;
        this.bothKeyAction = null;
    }

    public Bind(ActionBind baseKeyAction, ActionBind shiftKeyAction, ActionBind ctrlKeyAction, ActionBind bothKeyAction) {
        this.baseKeyAction = baseKeyAction;
        this.shiftKeyAction = shiftKeyAction;
        this.ctrlKeyAction = ctrlKeyAction;
        this.bothKeyAction = bothKeyAction;
    }
}
