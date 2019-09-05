package com.rubbishman.rubbishRedux.statefullTimer.reducer;

import com.rubbishman.rubbishRedux.createObjectCallback.action.CreateObject;
import com.rubbishman.rubbishRedux.createObjectCallback.interfaces.ICreateObjectProcessor;
import com.rubbishman.rubbishRedux.statefullTimer.state.RepeatingTimer;
import com.rubbishman.rubbishRedux.statefullTimer.state.TimerState;

import java.util.ArrayList;

public class TimerCreateProcessor implements ICreateObjectProcessor<TimerState, RepeatingTimer> {

    private RepeatingTimer createdObject;

    public RepeatingTimer getPostCreateObject() {
        return createdObject;
    }

    public TimerState run(TimerState state, CreateObject action) {
        state.timers = (ArrayList<RepeatingTimer>)state.timers.clone();

        createdObject = ((RepeatingTimer) action.createObject).assignId(state.timers.size());

        state.timers.add(createdObject);

        return state;
    }
}
