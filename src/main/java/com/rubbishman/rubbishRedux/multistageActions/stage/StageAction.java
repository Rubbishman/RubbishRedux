package com.rubbishman.rubbishRedux.multistageActions.stage;

import com.rubbishman.rubbishRedux.multistageActions.action.MultistageAction;

import java.util.concurrent.ConcurrentLinkedQueue;

public class StageAction {
    public final Stage stage;
    public final Object action;

    public StageAction(Stage stage, Object action) {
        this.stage = stage;
        this.action = action;
    }
}
