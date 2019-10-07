package com.rubbishman.rubbishRedux.multistageActionTest.reducer;

import com.rubbishman.rubbishRedux.multistageActionTest.action.IncrementCounter;
import com.rubbishman.rubbishRedux.multistageActionTest.action.IncrementCounterResolved;
import com.rubbishman.rubbishRedux.multistageActionTest.state.Counter;
import com.rubbishman.rubbishRedux.multistageActionTest.state.MultistageActionState;
import com.rubbishman.rubbishRedux.multistageActions.MultistageActions;
import redux.api.Reducer;

import java.io.PrintStream;

public class MyReducer implements Reducer<MultistageActionState> {
    private PrintStream printStream;
    private MultistageActions<MultistageActionState> multistageAction;

    public MyReducer(PrintStream printStream) {
        this.printStream = printStream;
    }

    public void setMultistageAction(MultistageActions<MultistageActionState> multistageAction) {
        this.multistageAction = multistageAction;
    }

    public MultistageActionState reduce(MultistageActionState state, Object action) {
        MultistageActionState cloned = state.clone();

        if(action == redux.api.Store.INIT) {
            printStream.println("Initial state INIT");
        }  else if(action instanceof IncrementCounter) {
            multistageAction.addMultistageAction(action);
        } else if(action instanceof IncrementCounterResolved) {
            IncrementCounterResolved resolved = (IncrementCounterResolved) action;
            Counter counter = cloned.counterObjects.get(resolved.targetCounterId);
            cloned.counterObjects.set(resolved.targetCounterId, counter.increment(resolved.incrementAmount));
        }

        return cloned;
    }
}
