package com.rubbishman.rubbishRedux.statefullTimer;

import com.rubbishman.rubbishRedux.createObjectCallback.action.CreateObject;
import com.rubbishman.rubbishRedux.createObjectCallback.enhancer.CreateObjectEnhancer;
import com.rubbishman.rubbishRedux.dynamicObjectStore.store.IdObject;
import com.rubbishman.rubbishRedux.dynamicObjectStore.store.ObjectStore;
import com.rubbishman.rubbishRedux.statefullTimer.helper.TimerComparator;
import com.rubbishman.rubbishRedux.statefullTimer.helper.TimerHelper;
import com.rubbishman.rubbishRedux.statefullTimer.logic.TimerLogic;
import com.rubbishman.rubbishRedux.statefullTimer.reducer.TimerReducer;
import com.rubbishman.rubbishRedux.statefullTimer.state.RepeatingTimer;
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
    private TimerComparator comparator;
    private PriorityQueue<TimerLogic> timerList;
    ScheduledExecutorService executor;

    public void addTimer(TimerLogic logic) {
        synchronized (timerList) {
            timerList.add(logic);
        }
    }

    public void addAction(Object action) {
        actionQueue.add(action);
    }

    public TimerExecutor(Store.Creator<ObjectStore> creator) {
        timerState = creator.create(new TimerReducer(), new ObjectStore());

        initialize(timerState);
    }

    public TimerExecutor(ConcurrentLinkedQueue<Object> actionQueue, Store<ObjectStore> timerState) {
        this.actionQueue = actionQueue;
        initialize(timerState);
    }

    public ObjectStore getState() {
        return timerState.getState();
    }

    private void initialize(Store<ObjectStore> timerState) {
        this.timerState = timerState;
        comparator = new TimerComparator(timerState);
        timerList = new PriorityQueue<>(comparator);

        executor = Executors.newSingleThreadScheduledExecutor();
    }

    public LinkedList<TimerLogic> beforeDispatchStarted(ConcurrentLinkedQueue<Object> actionQueue, Long nowTime) {
        return checkTimers(actionQueue, nowTime);
    }

    public void afterDispatchFinished(LinkedList<TimerLogic> toAdd) {
        synchronized (timerState) {
            for(TimerLogic logicToAdd: toAdd) {
                addTimer(logicToAdd);
            }
        }
    }

    public void timerLogic(ConcurrentLinkedQueue<Object> actionQueue, Long nowTime) {
        LinkedList<TimerLogic> toAdd = beforeDispatchStarted(actionQueue, nowTime);

        doActions();

        afterDispatchFinished(toAdd);
    }

    public void startTimer() {
        Runnable runner = () -> {
            Long nowTime = System.nanoTime();
            timerLogic(actionQueue, nowTime);
        };

        executor.scheduleAtFixedRate(runner, 0, 15, TimeUnit.MILLISECONDS);
    }

    private LinkedList<TimerLogic> checkTimers(ConcurrentLinkedQueue<Object> actionQueue, Long nowTime) {
        LinkedList<TimerLogic> toAdd = new LinkedList();

        synchronized (timerState) {
            ObjectStore state = timerState.getState();

            TimerLogic logic = timerList.peek();
            while(logic != null) {
                if(TimerHelper.repeatsChanged(logic.getRepeatingTimer(state), nowTime)) {
                    logic = timerList.poll();

                    if(logic.logic(actionQueue, state, nowTime)) {
                        toAdd.add(logic);
                    }
                    logic = timerList.peek();
                } else {
                    logic = null;
                }
            }
        }

        return toAdd;
    }

    private void doActions() {
        Object action = actionQueue.poll();
        while(action != null) {
            timerState.dispatch(action);
            action = actionQueue.poll();
        }
    }

    public RepeatingTimer createTimer(Long nowTime, Object action, int period, int repeats) {
        CreateObject<RepeatingTimer> createObj = new CreateObject(
                new RepeatingTimer(nowTime, period, repeats, 0 , action),
                (repeatingTimer) -> {
                    addTimer(new TimerLogic(((IdObject)repeatingTimer).id));
                }
        );

        addAction(createObj);

        return createObj.createObject;
    }
}
