package com.rubbishman.rubbishRedux.external;

import com.rubbishman.rubbishRedux.experimental.actionTrack.ActionTrack;
import com.rubbishman.rubbishRedux.experimental.actionTrack.stage.StageStack;
import com.rubbishman.rubbishRedux.external.operational.store.ObjectStore;
import com.rubbishman.rubbishRedux.external.setup_extra.RubbishReducer;
import com.rubbishman.rubbishRedux.external.setup_extra.statefullTimer.StatefullTimerProcessing;
import com.rubbishman.rubbishRedux.internal.statefullTimer.logic.TimerLogic;
import redux.api.Store;

import java.util.LinkedList;

public class RubbishContainer {
    private ActionTrack actionTrack;
    private Store<ObjectStore> store;
    private StatefullTimerProcessing timer;

    public RubbishContainer(StageStack stageStack, StatefullTimerProcessing timer, Store<ObjectStore> store, RubbishReducer reducer) {
        this.actionTrack = new ActionTrack(store, stageStack);
        this.timer = timer;
        this.store = store;
        reducer.setRubbishContainer(this);
    }

    public ObjectStore getState() {
        return store.getState();
    }

    public void addAction(Object action) {
        actionTrack.addAction(action);
    }

    public void performAction(Object action) {
        addAction(action);
        performActions();
    }

    public void performActions() {
        Long nowTime = System.nanoTime();

        LinkedList<TimerLogic> toAdd = timer.beforeDispatchStarted(actionTrack, nowTime);

        // Take a snapshot of the queue.
        ActionTrack internalQueue = actionTrack.isolate();

        while(internalQueue.hasNext()) {
            internalQueue.processNextAction();
        }

        timer.afterDispatchFinished(toAdd);
    }
}
