package com.rubbishman.rubbishRedux.statefullTimer.reducer;

import com.rubbishman.rubbishRedux.createObjectCallback.action.CreateObject;
import com.rubbishman.rubbishRedux.createObjectCallback.interfaces.ICreateObjectProcessor;
import com.rubbishman.rubbishRedux.statefullTimer.state.RepeatingTimer;
import com.rubbishman.rubbishRedux.statefullTimer.state.TimerState;

public class TimerCreateProcessor implements ICreateObjectProcessor<TimerState, RepeatingTimer> {

    private RepeatingTimer createdObject;

    public RepeatingTimer getPostCreateObject() {
        return createdObject;
    }

    public TimerState run(TimerState state, CreateObject action) {
        TimerState cloned = state.clone();

        createdObject = ((RepeatingTimer) action.createObject).assignId(cloned.timers.size());

        cloned.timers.add(createdObject);

        return cloned;
    }
}
