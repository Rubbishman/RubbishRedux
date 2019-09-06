package com.rubbishman.rubbishRedux.misc;

import com.rubbishman.rubbishRedux.MiddlewareEnchancerTest;
import redux.api.Reducer;

import java.io.PrintStream;

public class MyReducer implements Reducer<MyState> {

    private PrintStream printStream;

    public MyReducer(PrintStream printStream) {
        this.printStream = printStream;
    }

    public MyState reduce(MyState state, Object action) {
        MyState cloned = state.clone();

        if(action == redux.api.Store.INIT) {
            printStream.println("Initial state INIT");
        } else {
            printStream.println("Adding " + action.toString());
        }

        cloned.actions.add(action);

        return cloned;
    }
}
