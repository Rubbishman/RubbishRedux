package com.rubbishman.rubbishRedux.statefullTimer.logic;

import com.rubbishman.rubbishRedux.statefullTimer.helper.TimerHelper;
import com.rubbishman.rubbishRedux.statefullTimer.state.RepeatingTimer;
import com.rubbishman.rubbishRedux.statefullTimer.state.TimerState;

public class TimerLogic {
    public int subject;

    public TimerLogic(int subject) {
        this.subject = subject;
    }

    public boolean logic(TimerState state, long nowTime) {
        if(state.timers.get(subject) == null) {
            return false;
        }

        RepeatingTimer periodicIncrementer = getPeriodicIncrementer(state);

        if(TimerHelper.repeatsChanged(periodicIncrementer, nowTime)) {
//            InitApp.actionQueue.add(new ActionTargeted(subject, Action.PERIODIC_INC_EXEC));
            if(periodicIncrementer.currentRepeats == periodicIncrementer.repeats - 1) {
                return false; //Destruct is needed..?
            }
            return true;
        }
        return false;
    }

    public RepeatingTimer getPeriodicIncrementer(TimerState state) {
        return state.timers.get(subject);
    }

}
