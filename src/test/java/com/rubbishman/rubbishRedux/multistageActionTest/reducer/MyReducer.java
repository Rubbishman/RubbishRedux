package com.rubbishman.rubbishRedux.multistageActionTest.reducer;

import com.rubbishman.rubbishRedux.createObjectCallback.action.CreateObject;
import com.rubbishman.rubbishRedux.createObjectCallback.reducer.CreateObjectReducer;

import com.rubbishman.rubbishRedux.multistageActionTest.action.IncrementCounter;
import com.rubbishman.rubbishRedux.multistageActionTest.action.IncrementCounterResolved;
import com.rubbishman.rubbishRedux.multistageActionTest.processor.CreateCounterProcessor;
import com.rubbishman.rubbishRedux.multistageActionTest.processor.CreateCounterStopperProcessor;
import com.rubbishman.rubbishRedux.multistageActionTest.state.MultistageActionState;
import redux.api.Reducer;

import java.io.PrintStream;

public class MyReducer implements Reducer<MultistageActionState> {
    private PrintStream printStream;
    private CreateObjectReducer<MultistageActionState> createReducer;


    public MyReducer(PrintStream printStream) {
        this.printStream = printStream;
        createReducer = new CreateObjectReducer<>();

        createReducer.addProcessor(new CreateCounterProcessor());
        createReducer.addProcessor(new CreateCounterStopperProcessor());
    }

    public MultistageActionState reduce(MultistageActionState state, Object action) {
        MultistageActionState cloned = state.clone();

        if(action == redux.api.Store.INIT) {
            printStream.println("Initial state INIT");
        } else if(action instanceof CreateObject) {
            printStream.println("Adding " + action.toString());
            cloned = createReducer.reduce(cloned, action);
        } else if(action instanceof IncrementCounter) {

        } else if(action instanceof IncrementCounterResolved) {

        }

        return cloned;
    }
}
