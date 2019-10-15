package com.rubbishman.rubbishRedux.misc;

import com.google.gson.Gson;
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
            Gson gson = new Gson();
            printStream.println("Adding " + action.getClass().getSimpleName() + " " + gson.toJson(action));
        }

        cloned.actions.add(action);

        return cloned;
    }
}
