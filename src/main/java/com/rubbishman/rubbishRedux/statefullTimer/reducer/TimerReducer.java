package com.rubbishman.rubbishRedux.statefullTimer.reducer;

import com.rubbishman.rubbishRedux.createObjectCallback.action.CreateObject;
import com.rubbishman.rubbishRedux.createObjectCallback.reducer.CreateObjectReducer;
import com.rubbishman.rubbishRedux.statefullTimer.action.IncrementTimer;
import com.rubbishman.rubbishRedux.statefullTimer.helper.TimerHelper;
import com.rubbishman.rubbishRedux.statefullTimer.state.RepeatingTimer;
import com.rubbishman.rubbishRedux.statefullTimer.state.TimerState;
import redux.api.Reducer;

public class TimerReducer implements Reducer<TimerState> {
    private TimerState reduce(TimerState state, IncrementTimer action) {
        RepeatingTimer timer = state.timers.get(action.subject);

        RepeatingTimer newTimer = timer.changeRepeats(TimerHelper.calculateNewRepeats(action.nowTime, timer));
        state.timers.set(action.subject, newTimer);

        int diff = newTimer.currentRepeats - timer.currentRepeats;

        while(diff > 0) {
            state = this.reduce(state, timer.action);
            diff--;
        }

        return state;
    }

    public TimerState reduce(TimerState state, Object action) {
        if (action instanceof IncrementTimer) {
            return reduce(state, (IncrementTimer)action);
        }
        return state;
    }
}
