package com.rubbishman.rubbishRedux.external;

import com.rubbishman.rubbishRedux.external.setup_extra.actionTrack.ActionTrack;
import com.rubbishman.rubbishRedux.external.setup_extra.TickSystem;
import com.rubbishman.rubbishRedux.external.setup_extra.actionTrack.stage.StageStack;
import com.rubbishman.rubbishRedux.external.operational.store.ObjectStore;
import com.rubbishman.rubbishRedux.external.setup_extra.statefullTimer.StatefullTimerProcessing;
import com.rubbishman.rubbishRedux.internal.RubbishReducer;
import com.rubbishman.rubbishRedux.internal.timekeeper.TimeKeeper;
import redux.api.Store;

import java.sql.Time;
import java.util.ArrayList;

public class RubbishContainer {
    private ActionTrack actionTrack;
    private Store<ObjectStore> store;
    private StatefullTimerProcessing statefullTimer;
    private TimeKeeper timeKeeper;

    public RubbishContainer(
            StageStack stageStack,
            Store<ObjectStore> store,
            RubbishReducer reducer,
            ArrayList<TickSystem> registeredTickSystems,
            TimeKeeper timeKeeper) {
        if(timeKeeper == null) {
            this.timeKeeper = new TimeKeeper();
        } else {
            this.timeKeeper = timeKeeper;
        }

        this.actionTrack = new ActionTrack(this.timeKeeper, registeredTickSystems, store, reducer, stageStack);
        this.store = store;

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
        return timeKeeper.getNowTime();
    }

    public long getElapsedTime() {
        return timeKeeper.getElapsedTime();
    }

    public void performActions() {
        timeKeeper.progressTime();

        // Take a snapshot of the queue.
        ActionTrack internalQueue = actionTrack.isolate();

        internalQueue.processActions();
    }

    public void createTimer(Long nowTime, Object action, long period, long repeats) {
        statefullTimer.createTimer(actionTrack, nowTime, action, period, repeats);
    }
}
