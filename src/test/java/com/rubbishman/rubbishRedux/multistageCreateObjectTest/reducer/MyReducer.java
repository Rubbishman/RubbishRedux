package com.rubbishman.rubbishRedux.multistageCreateObjectTest.reducer;

import com.rubbishman.rubbishRedux.dynamicObjectStore.store.ObjectStore;
import com.rubbishman.rubbishRedux.multistageActionTest.action.IncrementCounter;
import com.rubbishman.rubbishRedux.multistageActionTest.action.IncrementCounterResolved;
import com.rubbishman.rubbishRedux.multistageActionTest.state.Counter;
import com.rubbishman.rubbishRedux.multistageActions.MultistageActions;
import com.rubbishman.rubbishRedux.multistageCreateObjectTest.action.CreateCounter;
import com.rubbishman.rubbishRedux.multistageCreateObjectTest.action.MultistageCreateObject;
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
        } else if(action instanceof MultistageCreateObject
                && ((MultistageCreateObject) action).createObject instanceof CreateCounter) {
            multistageAction.addMultistageAction(action);
        } else if(action instanceof IncrementCounter) {
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