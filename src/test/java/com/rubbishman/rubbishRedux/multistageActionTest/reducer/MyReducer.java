package com.rubbishman.rubbishRedux.multistageActionTest.reducer;

import com.rubbishman.rubbishRedux.dynamicObjectStore.store.IdObject;
import com.rubbishman.rubbishRedux.dynamicObjectStore.store.ObjectStore;
import com.rubbishman.rubbishRedux.multistageActionTest.action.IncrementCounter;
import com.rubbishman.rubbishRedux.multistageActionTest.action.IncrementCounterResolved;
import com.rubbishman.rubbishRedux.multistageActionTest.state.Counter;
import com.rubbishman.rubbishRedux.multistageActions.MultistageActions;
import redux.api.Reducer;

import java.io.PrintStream;

public class MyReducer implements Reducer<ObjectStore> {
    private PrintStream printStream;
    private MultistageActions multistageAction;

    public MyReducer(PrintStream printStream) {
        this.printStream = printStream;
    }

    public void setMultistageAction(MultistageActions multistageAction) {
        this.multistageAction = multistageAction;
    }

    public ObjectStore reduce(ObjectStore state, Object action) {
        if(action == redux.api.Store.INIT) {
            printStream.println("Initial state INIT");
        }  else if(action instanceof IncrementCounter) {
            multistageAction.addMultistageAction(action);
        } else if(action instanceof IncrementCounterResolved) {
            IncrementCounterResolved resolved = (IncrementCounterResolved) action;

            Counter counter = (Counter)state.objectMap.get(resolved.targetCounterId).object;

            return new ObjectStore(
                    state.objectMap.assoc(resolved.targetCounterId, new IdObject(resolved.targetCounterId, counter.increment(resolved.incrementAmount))),
                    state.idGenerator
            );
        }

        return state;
    }
}
