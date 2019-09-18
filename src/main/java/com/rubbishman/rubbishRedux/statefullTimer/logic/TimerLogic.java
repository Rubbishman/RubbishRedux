package com.rubbishman.rubbishRedux.statefullTimer.logic;

import com.rubbishman.rubbishRedux.statefullTimer.action.IncrementTimer;
import com.rubbishman.rubbishRedux.statefullTimer.TimerExecutor;
import com.rubbishman.rubbishRedux.statefullTimer.helper.TimerHelper;
import com.rubbishman.rubbishRedux.statefullTimer.state.RepeatingTimer;
import com.rubbishman.rubbishRedux.statefullTimer.state.TimerState;

public class TimerLogic {
    private TimerExecutor owner;
    public final int subject;

    public TimerLogic(TimerExecutor owner, int subject) {
        this.owner = owner;
        this.subject = subject;
    }

    public boolean logic(TimerState state, long nowTime) {
        if(state.timers.get(subject) == null) {
            return false;
        }

        RepeatingTimer periodicIncrementer = getRepeatingTimer(state);

        if(TimerHelper.repeatsChanged(periodicIncrementer, nowTime)) {
            owner.addAction(new IncrementTimer(nowTime, subject));
            if(periodicIncrementer.currentRepeats == periodicIncrementer.repeats - 1) {
                return false; //Destruct is needed..?
            }
            return true;
        }
        return false;
    }

    public RepeatingTimer getRepeatingTimer(TimerState state) {
        return state.timers.get(subject);
    }

}
