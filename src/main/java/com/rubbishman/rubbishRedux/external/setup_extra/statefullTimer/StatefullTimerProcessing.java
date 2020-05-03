package com.rubbishman.rubbishRedux.external.setup_extra.statefullTimer;

import com.rubbishman.rubbishRedux.experimental.actionTrack.ActionTrack;
import com.rubbishman.rubbishRedux.experimental.actionTrack.TickSystem;
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

public class StatefullTimerProcessing extends TickSystem {
    private TimerComparator comparator;
    private PriorityQueue<TimerLogic> timerList;

    @Override
    public void setStore(Store<ObjectStore> store) {
        super.setStore(store);
        comparator = new TimerComparator(store);
        timerList = new PriorityQueue<>(comparator);
    }

    LinkedList<TimerLogic> toAdd;

    public void beforeDispatchStarted(ActionTrack actionTrack, Long nowTime) {
        toAdd = new LinkedList();

        synchronized (store) {
            ObjectStore state = store.getState();

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
    }

    public void afterDispatchFinished() {
        synchronized (store) {
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
