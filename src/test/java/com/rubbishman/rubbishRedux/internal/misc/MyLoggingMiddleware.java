package com.rubbishman.rubbishRedux.internal.misc;

import com.google.gson.Gson;
import com.rubbishman.rubbishRedux.internal.dynamicObjectStore.GsonInstance;
import redux.api.Dispatcher;
import redux.api.Store;
import redux.api.enhancer.Middleware;

import java.io.PrintStream;

public class MyLoggingMiddleware implements Middleware {

    private String prefix;
    private PrintStream printStream;

    public MyLoggingMiddleware(PrintStream printStream, String prefix) {
        this.printStream = printStream;
        this.prefix = prefix;
    }

    public Object dispatch(Store store, Dispatcher next, Object action) {
        Gson gson = GsonInstance.getInstance();
        printStream.println(prefix + " " + action.getClass().getSimpleName() + " " + gson.toJson(action));
        return next.dispatch(action);
    }
}
