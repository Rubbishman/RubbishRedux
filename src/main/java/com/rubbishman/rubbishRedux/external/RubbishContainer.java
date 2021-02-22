package com.rubbishman.rubbishRedux.external;

import com.rubbishman.rubbishRedux.experimental.steppedActionTrack.SteppedActionTrack;
import com.rubbishman.rubbishRedux.external.operational.action.multistageAction.Stage.Stage;
import com.rubbishman.rubbishRedux.external.setup_extra.actionTrack.ActionTrack;
import com.rubbishman.rubbishRedux.external.setup_extra.TickSystem;
import com.rubbishman.rubbishRedux.external.setup_extra.actionTrack.ActionTrackListener;
import com.rubbishman.rubbishRedux.external.setup_extra.actionTrack.IActionTrack;
import com.rubbishman.rubbishRedux.external.setup_extra.actionTrack.stage.StageStack;
import com.rubbishman.rubbishRedux.external.operational.store.ObjectStore;
import com.rubbishman.rubbishRedux.external.setup_extra.statefullTimer.StatefullTimerProcessing;
import com.rubbishman.rubbishRedux.internal.RubbishReducer;
import com.rubbishman.rubbishRedux.internal.timekeeper.TimeKeeper;
import redux.api.Store;

import java.sql.Time;
import java.util.ArrayList;
import java.util.HashMap;

public class RubbishContainer {
    private IActionTrack actionTrack;
    private Store<ObjectStore> store;
    private StatefullTimerProcessing statefullTimer;
    private TimeKeeper timeKeeper;

    public RubbishContainer(
            StageStack stageStack,
            Store<ObjectStore> store,
            RubbishReducer reducer,
            ArrayList<TickSystem> registeredTickSystems,
            TimeKeeper timeKeeper,
            HashMap<Stage, ArrayList<ActionTrackListener>> listeners,
            boolean isSteppedActionTracker
    ) {
        if(timeKeeper == null) {
            this.timeKeeper = new TimeKeeper();
        } else {
            this.timeKeeper = timeKeeper;
        }

        if(isSteppedActionTracker) {
            this.actionTrack = new SteppedActionTrack(
                    this.timeKeeper,
                    registeredTickSystems,
                    store,
                    reducer,
                    stageStack,
                    listeners
            );
        } else {
            this.actionTrack = new ActionTrack(
                    this.timeKeeper,
                    registeredTickSystems,
                    store,
                    reducer,
                    stageStack,
                    listeners
            );
        }

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
        IActionTrack internalQueue = actionTrack.isolate();

        internalQueue.processActions();
    }

    public void createTimer(Long nowTime, Object action, long period, long repeats) {
        statefullTimer.createTimer(actionTrack, nowTime, action, period, repeats);
    }
}
