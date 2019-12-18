package com.rubbishman.rubbishRedux.external;

import com.rubbishman.rubbishRedux.internal.dynamicObjectStore.store.ObjectStore;
import com.rubbishman.rubbishRedux.external.multiStageActions.MultiStageActionsProcessing;
import com.rubbishman.rubbishRedux.external.statefullTimer.StatefullTimerProcessing;
import com.rubbishman.rubbishRedux.statefullTimer.logic.TimerLogic;
import redux.api.Store;

import java.util.LinkedList;
import java.util.concurrent.ConcurrentLinkedQueue;

public class RubbishContainer {
    private ConcurrentLinkedQueue<Object> actionQueue;
    private Store<ObjectStore> store;
    private StatefullTimerProcessing timer;
    private MultiStageActionsProcessing multistageActions;

    public RubbishContainer(ConcurrentLinkedQueue<Object> actionQueue, MultiStageActionsProcessing multistageActions, StatefullTimerProcessing timer, Store<ObjectStore> store) {
        this.actionQueue = actionQueue;
        this.timer = timer;
        this.store = store;
        this.multistageActions = multistageActions;
    }

    public ObjectStore getState() {
        return store.getState();
    }

    public void addAction(Object action) {
        actionQueue.add(action);
    }

    public void performAction(Object action) {
        actionQueue.add(action);
        performActions();
    }

    public void performActions() {
        Long nowTime = System.nanoTime();

        LinkedList<TimerLogic> toAdd = timer.beforeDispatchStarted(actionQueue, nowTime);

        // Take a snapshot of the queue.
        ConcurrentLinkedQueue<Object> internalQueue = snapShotQueue();

        Object action = internalQueue.poll();
        while(action != null) {
            store.dispatch(action);
            multistageActions.afterDispatch(nowTime); // If an action has triggered multistage actions they resolve before the next action.
            action = internalQueue.poll();
        }

        timer.afterDispatchFinished(toAdd);
    }

    private ConcurrentLinkedQueue<Object> snapShotQueue() {
        ConcurrentLinkedQueue<Object> internalQueue;
        synchronized (actionQueue) {
            internalQueue = actionQueue;
            actionQueue = new ConcurrentLinkedQueue<>();
        }

        return internalQueue;
    }
//    public void timerLogic(Long nowTime) {
//        timer.timerLogic(nowTime);
//    }
//
//    public void startTimer() {
//        timer.startTimer();
//    }
//
//    public void stopTimer() {
//        timer.stopTimer();
//    }
}
