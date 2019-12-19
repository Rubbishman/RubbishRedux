package com.rubbishman.rubbishRedux.statefullTimer;

import com.rubbishman.rubbishRedux.internal.createObjectCallback.enhancer.CreateObjectEnhancer;
import com.rubbishman.rubbishRedux.internal.dynamicObjectStore.reducer.CreateObjectReducer;
import com.rubbishman.rubbishRedux.internal.dynamicObjectStore.store.ObjectStore;
import com.rubbishman.rubbishRedux.external.statefullTimer.StatefullTimerProcessing;
import com.rubbishman.rubbishRedux.statefullTimer.helper.TimerComparator;
import com.rubbishman.rubbishRedux.statefullTimer.logic.TimerLogic;
import com.rubbishman.rubbishRedux.statefullTimer.reducer.TimerReducer;
import redux.api.Store;

import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class TimerExecutor {
    private Store<ObjectStore> timerState;
    private ConcurrentLinkedQueue<Object> actionQueue = new ConcurrentLinkedQueue<>();
    private ScheduledExecutorService executor;
    private StatefullTimerProcessing timerProcessing;

    public TimerExecutor(Store.Creator<ObjectStore> creator) {
        CreateObjectEnhancer enhancer = new CreateObjectEnhancer();
        creator = enhancer.enhance(creator);

        CreateObjectReducer reducer = new CreateObjectReducer();
        reducer.setWrappedReducer(new TimerReducer());
        timerState = creator.create(reducer, new ObjectStore());

        initialize(timerState);
    }

    public ObjectStore getState() {
        return timerState.getState();
    }

    private void initialize(Store<ObjectStore> timerState) {
        this.timerState = timerState;
        timerProcessing = new StatefullTimerProcessing(timerState);

        executor = Executors.newSingleThreadScheduledExecutor();
    }

    public void timerLogic(Long nowTime) {
        LinkedList<TimerLogic> toAdd = timerProcessing.beforeDispatchStarted(actionQueue, nowTime);

        Object action = actionQueue.poll();
        while(action != null) {
            timerState.dispatch(action);
            action = actionQueue.poll();
        }

        timerProcessing.afterDispatchFinished(toAdd);
    }

    public void startTimer() {
        Runnable runner = () -> {
            Long nowTime = System.nanoTime();
            timerLogic(nowTime);
        };

        executor.scheduleAtFixedRate(runner, 0, 15, TimeUnit.MILLISECONDS);
    }

    public void createTimer(Long nowTime, Object action, int period, int repeats) {
        timerProcessing.createTimer(actionQueue, nowTime, action, period, repeats);
    }
}