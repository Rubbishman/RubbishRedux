package com.rubbishman.rubbishRedux.statefullTimer.executor;

import com.rubbishman.rubbishRedux.createObjectCallback.action.CreateObject;
import com.rubbishman.rubbishRedux.statefullTimer.helper.TimerComparator;
import com.rubbishman.rubbishRedux.statefullTimer.helper.TimerHelper;
import com.rubbishman.rubbishRedux.statefullTimer.logic.TimerLogic;
import com.rubbishman.rubbishRedux.statefullTimer.reducer.TimerReducer;
import com.rubbishman.rubbishRedux.statefullTimer.state.RepeatingTimer;
import com.rubbishman.rubbishRedux.statefullTimer.state.Timer;
import com.rubbishman.rubbishRedux.statefullTimer.state.TimerState;
import redux.api.Store;

import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class TimerExecutor {
    private Store<TimerState> timerState;
    private ConcurrentLinkedQueue<Object> actionQueue;
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

    public TimerExecutor(Store.Creator<TimerState> creator) {
        initialize(creator);
    }

    public TimerExecutor() {
        initialize(new com.glung.redux.Store.Creator());
    }

    public TimerState getState() {
        return timerState.getState();
    }

    private void initialize(Store.Creator<TimerState> creator) {
        timerState = creator.create(new TimerReducer(), new TimerState());

        comparator = new TimerComparator(timerState);
        timerList = new PriorityQueue<>(comparator);
        actionQueue = new ConcurrentLinkedQueue<>();

        executor = Executors.newSingleThreadScheduledExecutor();
    }

    public void rubbish() {
        LinkedList<TimerLogic> toAdd = checkTimers();

        doActions();

        synchronized (timerState) {
            for(TimerLogic logicToAdd: toAdd) {
                addTimer(logicToAdd);
            }
        }
    }

    public void startTimer() {
        Runnable runner = () -> {
            rubbish();
        };

        executor.scheduleAtFixedRate(runner, 0, 15, TimeUnit.MILLISECONDS);
    }

    private LinkedList<TimerLogic> checkTimers() {
        LinkedList<TimerLogic> toAdd = new LinkedList();

        synchronized (timerState) {
            TimerState state = timerState.getState();
            Long nowTime = System.nanoTime();



            TimerLogic logic = timerList.peek();
            while(logic != null) {
                if(TimerHelper.repeatsChanged(logic.getPeriodicIncrementer(state), nowTime)) {
                    logic = timerList.poll();

                    if(logic.logic(state, nowTime)) {
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

    public RepeatingTimer createTimer(Object action, int period, int repeats) {
        CreateObject createObj = new CreateObject();

        RepeatingTimer create = new RepeatingTimer();
        create.timer = new Timer();
        create.timer.startTime = System.nanoTime();
        create.timer.period = period;
        create.action = action;
        create.repeats = repeats;

        createObj.createObject = create;

        createObj.callback = (repeatingTimer) -> {
            addTimer(new TimerLogic(((RepeatingTimer)repeatingTimer).id));
        };

        addAction(createObj);

        return create;
    }
}
