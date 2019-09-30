package com.rubbishman.rubbishRedux.multistageActions.stage;

import com.rubbishman.rubbishRedux.multistageActions.action.MultistageAction;

import java.util.concurrent.ConcurrentLinkedQueue;

public class StageActions {
    public final Stage stage;
    private ConcurrentLinkedQueue<MultistageAction> actions;

    public StageActions(Stage stage) {
        this.stage = stage;
        actions = new ConcurrentLinkedQueue<MultistageAction>();
    }

    public MultistageAction poll() {
        return actions.poll();
    }

    public void addAction(MultistageAction action) {
        actions.add(action);
    }
}
