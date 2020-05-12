package com.rubbishman.rubbishRedux.external;

import com.rubbishman.rubbishRedux.external.setup_extra.actionTrack.ActionTrack;
import com.rubbishman.rubbishRedux.external.setup_extra.TickSystem;
import com.rubbishman.rubbishRedux.external.setup_extra.actionTrack.stage.StageStack;
import com.rubbishman.rubbishRedux.external.operational.store.ObjectStore;
import com.rubbishman.rubbishRedux.external.setup_extra.statefullTimer.StatefullTimerProcessing;
import com.rubbishman.rubbishRedux.internal.RubbishReducer;
import redux.api.Store;
import java.util.ArrayList;

public class RubbishContainer {
    private ActionTrack actionTrack;
    private Store<ObjectStore> store;
    private ArrayList<TickSystem> registeredTickSystems;
    private StatefullTimerProcessing statefullTimer;
    protected long nowTime;
    protected long elapsedTime;

    public RubbishContainer(
            StageStack stageStack,
            Store<ObjectStore> store,
            RubbishReducer reducer,
            ArrayList<TickSystem> registeredTickSystems) {
        this.actionTrack = new ActionTrack(store, stageStack);
        this.store = store;
        this.registeredTickSystems = registeredTickSystems;
        reducer.setRubbishContainer(this);
        statefullTimer = new StatefullTimerProcessing();
        statefullTimer.setStore(store);
        registeredTickSystems.add(statefullTimer);
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

    public long getNowTime() {
        return nowTime;
    }

    public long getElapsedTime() {
        return elapsedTime;
    }

    public void performActions() {
        Long nowTime = System.nanoTime();
        elapsedTime = nowTime - this.nowTime;
        this.nowTime = nowTime;

        for(TickSystem tickSystem : registeredTickSystems) {
            tickSystem.beforeDispatchStarted(actionTrack, nowTime);
        }

        // Take a snapshot of the queue.
        ActionTrack internalQueue = actionTrack.isolate();

        while(internalQueue.hasNext()) {
            internalQueue.processNextAction();
        }

        for(TickSystem tickSystem : registeredTickSystems) {
            tickSystem.afterDispatchFinished();
        }
    }

    public void createTimer(Long nowTime, Object action, long period, long repeats) {
        statefullTimer.createTimer(actionTrack, nowTime, action, period, repeats);
    }
}
