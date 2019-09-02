package com.rubbishman.rubbishRedux;

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
        printStream.println(prefix + " " + action.toString());
        return next.dispatch(action);
    }
}
