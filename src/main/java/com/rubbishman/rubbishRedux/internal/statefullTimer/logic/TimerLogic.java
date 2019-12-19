package com.rubbishman.rubbishRedux.internal.statefullTimer.logic;

import com.rubbishman.rubbishRedux.internal.dynamicObjectStore.store.Identifier;
import com.rubbishman.rubbishRedux.internal.dynamicObjectStore.store.ObjectStore;
import com.rubbishman.rubbishRedux.internal.statefullTimer.action.IncrementTimer;
import com.rubbishman.rubbishRedux.internal.statefullTimer.helper.TimerHelper;
import com.rubbishman.rubbishRedux.internal.statefullTimer.state.RepeatingTimer;

import java.util.concurrent.ConcurrentLinkedQueue;

public class TimerLogic {
    public final Identifier subject;

    public TimerLogic(Identifier subject) {
        this.subject = subject;
    }

    public boolean logic(ConcurrentLinkedQueue<Object> actionQueue, ObjectStore state, long nowTime) {
        if(state.objectMap.get(subject) == null) {
            return false;
        }

        RepeatingTimer periodicIncrementer = getRepeatingTimer(state);

        if(TimerHelper.repeatsChanged(periodicIncrementer, nowTime)) {
            actionQueue.add(new IncrementTimer(nowTime, subject));
            if(periodicIncrementer.currentRepeats == periodicIncrementer.repeats - 1) {
                return false; //Destruct is needed..?
            }
            return true;
        }
        return false;
    }

    public RepeatingTimer getRepeatingTimer(ObjectStore state) {
        return (RepeatingTimer)state.objectMap.get(subject).object;
    }

}
