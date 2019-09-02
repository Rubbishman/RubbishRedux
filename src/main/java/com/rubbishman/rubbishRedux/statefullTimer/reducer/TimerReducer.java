package com.rubbishman.rubbishRedux.statefullTimer.reducer;

import com.rubbishman.rubbishRedux.createObjectCallback.action.CreateObject;
import com.rubbishman.rubbishRedux.createObjectCallback.reducer.CreateObjectReducer;
import com.rubbishman.rubbishRedux.statefullTimer.state.TimerState;
import redux.api.Reducer;

public class TimerReducer implements Reducer<TimerState> {
    CreateObjectReducer<TimerState> createReducer;

    public TimerReducer() {
        createReducer = new CreateObjectReducer<>();

        createReducer.addProcessor(new TimerCreateProcessor());
    }

    public TimerState reduce(TimerState state, Object action) {
        if(action instanceof CreateObject) {
            return createReducer.reduce(state, (CreateObject)action);
        }
        return state;
    }
}
