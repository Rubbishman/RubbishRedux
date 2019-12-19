package com.rubbishman.rubbishRedux.internal.multistageActionTest.reducer;

import com.rubbishman.rubbishRedux.internal.dynamicObjectStore.store.ObjectStore;
import com.rubbishman.rubbishRedux.external.RubbishReducer;
import com.rubbishman.rubbishRedux.internal.multistageActionTest.action.IncrementCounter;
import com.rubbishman.rubbishRedux.internal.multistageActionTest.action.IncrementCounterResolved;
import com.rubbishman.rubbishRedux.internal.multistageActionTest.state.Counter;

import java.io.PrintStream;

public class MyReducer extends RubbishReducer {
    private PrintStream printStream;

    public MyReducer(PrintStream printStream) {
        this.printStream = printStream;
    }

    public ObjectStore reduce(ObjectStore state, Object action) {
        if(action == redux.api.Store.INIT) {
            printStream.println("Initial state INIT");
        }  else if(action instanceof IncrementCounter) {
            multistageAction.addMultistageAction(action);
        } else if(action instanceof IncrementCounterResolved) {
            IncrementCounterResolved resolved = (IncrementCounterResolved) action;

            Counter counter = (Counter)state.objectMap.get(resolved.targetCounterId).object;

            return state.setObject(
                resolved.targetCounterId,
                counter.increment(resolved.incrementAmount)
            );
        }

        return state;
    }
}
