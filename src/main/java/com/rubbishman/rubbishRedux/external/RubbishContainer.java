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
    private ArrayList<TickSystem> registeredTickSystems;
    private StatefullTimerProcessing statefullTimer;
    private TimeKeeper timeKeeper;
    private ActionTrack internalQueue;
    private Boolean processingAction = false;

    public RubbishContainer(
            StageStack stageStack,
            Store<ObjectStore> store,
            RubbishReducer reducer,
            ArrayList<TickSystem> registeredTickSystems,
            TimeKeeper timeKeeper) {
        this.actionTrack = new ActionTrack(store, stageStack);
        this.store = store;
        this.registeredTickSystems = registeredTickSystems;
        reducer.setRubbishContainer(this);
        statefullTimer = new StatefullTimerProcessing();
        statefullTimer.setStore(store);
        registeredTickSystems.add(statefullTimer);

        if(timeKeeper == null) {
            this.timeKeeper = new TimeKeeper();
        } else {
            this.timeKeeper = timeKeeper;
        }
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
        internalQueue = actionTrack.isolate();

        processActions(internalQueue);
    }

    private void processActions(ActionTrack actionTrack) {
        for(TickSystem tickSystem : registeredTickSystems) {
            tickSystem.beforeDispatchStarted(actionTrack, timeKeeper.getNowTime());
        }

        boolean wasProcessingActionTrue;
        synchronized (processingAction) {
            wasProcessingActionTrue = processingAction;
        }

        while(actionTrack.hasNext()) {
            if(!wasProcessingActionTrue) {
                synchronized (processingAction) {
                    processingAction = true;
                }
                actionTrack.processNextAction();
                synchronized (processingAction) {
                    processingAction = false;
                }
            } else {
                actionTrack.processNextAction();
            }
        }

        for(TickSystem tickSystem : registeredTickSystems) {
            tickSystem.afterDispatchFinished();
        }
    }

    public void performActionThisTick(Object action) {
        synchronized (processingAction) {
            if(processingAction) {
                internalQueue.addAction(action);
            } else {
                throw new RuntimeException();
            }
        }
    }

    public void performActionImmediately(Object action) {
        synchronized (processingAction) {
            if(processingAction) {
                ActionTrack internalActionTrack = new ActionTrack(store, actionTrack.stageStack);
                internalActionTrack.addAction(action);
                processActions(internalActionTrack);
            } else {
                throw new RuntimeException();
            }
        }
    }

    public void performActionsImmediately(Object... actions) {
        synchronized (processingAction) {
            if(processingAction) {
                ActionTrack internalActionTrack = new ActionTrack(store, actionTrack.stageStack);
                for(Object action : actions) {
                    internalActionTrack.addAction(action);
                }
                processActions(internalActionTrack);
            } else {
                throw new RuntimeException();
            }
        }
    }

    public void createTimer(Long nowTime, Object action, long period, long repeats) {
        statefullTimer.createTimer(actionTrack, nowTime, action, period, repeats);
    }
}
