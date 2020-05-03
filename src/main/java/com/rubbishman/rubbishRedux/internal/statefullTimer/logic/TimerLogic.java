package com.rubbishman.rubbishRedux.internal.statefullTimer.logic;

import com.rubbishman.rubbishRedux.external.setup_extra.actionTrack.ActionTrack;
import com.rubbishman.rubbishRedux.external.operational.store.Identifier;
import com.rubbishman.rubbishRedux.external.operational.store.ObjectStore;
import com.rubbishman.rubbishRedux.internal.statefullTimer.action.IncrementTimer;
import com.rubbishman.rubbishRedux.internal.statefullTimer.helper.TimerHelper;
import com.rubbishman.rubbishRedux.internal.statefullTimer.state.RepeatingTimer;

public class TimerLogic {
    public final Identifier subject;

    public TimerLogic(Identifier subject) {
        this.subject = subject;
    }

    public boolean logic(ActionTrack actionTrack, ObjectStore state, long nowTime) {
        if(state.getObject(subject) == null) {
            return false;
        }

        RepeatingTimer periodicIncrementer = getRepeatingTimer(state);

        if(TimerHelper.repeatsChanged(periodicIncrementer, nowTime)) {
            actionTrack.addAction(new IncrementTimer(nowTime, subject));
            if(periodicIncrementer.currentRepeats == periodicIncrementer.repeats - 1) {
                return false; //Destruct is needed..?
            }
            return true;
        }
        return false;
    }

    public RepeatingTimer getRepeatingTimer(ObjectStore state) {
        return (RepeatingTimer)state.getObject(subject);
    }

}
