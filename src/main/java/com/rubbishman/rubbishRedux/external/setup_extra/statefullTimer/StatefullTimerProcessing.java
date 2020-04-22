package com.rubbishman.rubbishRedux.external.setup_extra.statefullTimer;

import com.rubbishman.rubbishRedux.experimental.actionTrack.ActionTrack;
import com.rubbishman.rubbishRedux.external.operational.action.createObject.CreateObject;
import com.rubbishman.rubbishRedux.external.operational.action.createObject.ICreateObjectCallback;
import com.rubbishman.rubbishRedux.external.operational.store.IdObject;
import com.rubbishman.rubbishRedux.external.operational.store.ObjectStore;
import com.rubbishman.rubbishRedux.internal.statefullTimer.helper.TimerComparator;
import com.rubbishman.rubbishRedux.internal.statefullTimer.helper.TimerHelper;
import com.rubbishman.rubbishRedux.internal.statefullTimer.logic.TimerLogic;
import com.rubbishman.rubbishRedux.internal.statefullTimer.state.RepeatingTimer;
import redux.api.Store;

import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class StatefullTimerProcessing {
    private TimerComparator comparator;
    private PriorityQueue<TimerLogic> timerList;
    Store<ObjectStore> timerState;

    public StatefullTimerProcessing(Store<ObjectStore> timerState) {
        this.timerState = timerState;
        comparator = new TimerComparator(timerState);
        timerList = new PriorityQueue<>(comparator);
    }

    public LinkedList<TimerLogic> beforeDispatchStarted(ActionTrack actionTrack, Long nowTime) {
        LinkedList<TimerLogic> toAdd = new LinkedList();

        synchronized (timerState) {
            ObjectStore state = timerState.getState();

            TimerLogic logic = timerList.peek();
            while(logic != null) {
                if(TimerHelper.repeatsChanged(logic.getRepeatingTimer(state), nowTime)) {
                    logic = timerList.poll();

                    if(logic.logic(actionTrack, state, nowTime)) {
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

    public void afterDispatchFinished(LinkedList<TimerLogic> toAdd) {
        synchronized (timerState) {
            for(TimerLogic logicToAdd: toAdd) {
                addTimer(logicToAdd);
            }
        }
    }

    public void addTimer(TimerLogic logic) {
        synchronized (timerList) {
            timerList.add(logic);
        }
    }

    //TODO, this is simple, we also want one where we allow a wrapper on the callback.
    public CreateObject createTimer(ActionTrack actionTrack, Long nowTime, Object action, int period, int repeats) {
        CreateObject<RepeatingTimer> createObj = new CreateObject(
                new RepeatingTimer(nowTime, period, repeats, 0 , action),
                new ICreateObjectCallback() {
                    @Override
                    public void postCreateState(Object repeatingTimer) {
                        StatefullTimerProcessing.this.addTimer(new TimerLogic(((IdObject) repeatingTimer).id));
                    }
                }
        );

        actionTrack.addAction(createObj);

        return createObj;
    }
}
