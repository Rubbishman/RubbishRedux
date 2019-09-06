package com.rubbishman.rubbishRedux;

import com.rubbishman.rubbishRedux.middlewareEnhancer.MiddlewareEnhancer;
import com.rubbishman.rubbishRedux.misc.MyLoggingMiddleware;
import com.rubbishman.rubbishRedux.misc.MyReducer;
import com.rubbishman.rubbishRedux.misc.MyState;
import com.rubbishman.rubbishRedux.timeStampedLogger.middleware.TimeStampedMiddleware;
import org.junit.Test;
import redux.api.Store;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

import static org.junit.Assert.assertEquals;

public class TimeStampedActionTest {
    @Test
    public void testTimeStampedAction() {
        StringBuilder stringBuilder = new StringBuilder();

        OutputStream myOutput = new OutputStream() {
            public void write(int b) throws IOException {
                stringBuilder.append((char)b);
            }
        };

        PrintStream printStream = new PrintStream(myOutput);

        Store.Creator<MyState> creator = new com.glung.redux.Store.Creator();
        MyReducer reducer = new MyReducer(printStream);

        MiddlewareEnhancer<MyState> enhancer = new MiddlewareEnhancer<>();
        enhancer.addMiddleware(new MyLoggingMiddleware(printStream, "moo1"));

        TimeStampedMiddleware timeStampedMiddleware = new TimeStampedMiddleware();
        enhancer.addMiddleware(timeStampedMiddleware);

        creator = enhancer.enhance(creator);

        Store<MyState> store = creator.create(reducer, new MyState());

        store.dispatch("First");
        store.dispatch("Second");
        store.dispatch("Third");

        assertEquals(
                "Initial state INIT" +
                        "moo1 First" +
                        "Adding First" +
                        "moo1 Second" +
                        "Adding Second" +
                        "moo1 Third" +
                        "Adding Third",
                stringBuilder.toString().replaceAll(System.lineSeparator(), ""));

        timeStampedMiddleware.dispatchLogInstantly(store);

        assertEquals(
                "Initial state INIT" +
                        "moo1 First" +
                        "Adding First" +
                        "moo1 Second" +
                        "Adding Second" +
                        "moo1 Third" +
                        "Adding Third" +
                        "moo1 First" +
                        "Adding First" +
                        "moo1 Second" +
                        "Adding Second" +
                        "moo1 Third" +
                        "Adding Third",
                stringBuilder.toString().replaceAll(System.lineSeparator(), ""));
    }
}
