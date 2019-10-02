package com.rubbishman.rubbishRedux.multistageActionTest.reducer;

import com.rubbishman.rubbishRedux.multistageActionTest.action.IncrementCounter;
import com.rubbishman.rubbishRedux.multistageActionTest.action.IncrementCounterResolved;
import com.rubbishman.rubbishRedux.multistageActionTest.state.MultistageActionState;
import redux.api.Reducer;

import java.io.PrintStream;

public class MyReducer implements Reducer<MultistageActionState> {
    private PrintStream printStream;

    public MyReducer(PrintStream printStream) {
        this.printStream = printStream;
    }

    public MultistageActionState reduce(MultistageActionState state, Object action) {
        MultistageActionState cloned = state.clone();

        if(action == redux.api.Store.INIT) {
            printStream.println("Initial state INIT");
        }  else if(action instanceof IncrementCounter) {

        } else if(action instanceof IncrementCounterResolved) {

        }

        return cloned;
    }
}
