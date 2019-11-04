package com.rubbishman.rubbishRedux;

import com.rubbishman.rubbishRedux.middlewareEnhancer.MiddlewareEnhancer;
import com.rubbishman.rubbishRedux.misc.MyLoggingMiddleware;
import com.rubbishman.rubbishRedux.misc.MyReducer;
import com.rubbishman.rubbishRedux.misc.MyState;
import com.rubbishman.rubbishRedux.timeStampedLogger.middleware.TimeStampedMiddleware;
import org.junit.Before;
import org.junit.Test;
import redux.api.Store;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

import static org.junit.Assert.assertEquals;

public class TimeStampedActionTest {
    Store<MyState> store;
    StringBuilder stringBuilder;
    TimeStampedMiddleware timeStampedMiddleware;

    @Before
    public void setup() {
        stringBuilder = new StringBuilder();

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

        timeStampedMiddleware = new TimeStampedMiddleware();
        enhancer.addMiddleware(timeStampedMiddleware);

        creator = enhancer.enhance(creator);

        store = creator.create(reducer, new MyState());
    }
    @Test
    public void testTimeStampedAction() {
        store.dispatch("First");
        store.dispatch("Second");
        store.dispatch("Third");

        assertEquals(
                "Initial state INIT" +
                        "moo1 String \"First\"" +
                        "Adding String \"First\"" +
                        "moo1 String \"Second\"" +
                        "Adding String \"Second\"" +
                        "moo1 String \"Third\"" +
                        "Adding String \"Third\"",
                stringBuilder.toString().replaceAll(System.lineSeparator(), ""));

        timeStampedMiddleware.dispatchLogInstantly(store);

        assertEquals(
                "Initial state INIT" +
                        "moo1 String \"First\"" +
                        "Adding String \"First\"" +
                        "moo1 String \"Second\"" +
                        "Adding String \"Second\"" +
                        "moo1 String \"Third\"" +
                        "Adding String \"Third\"" +
                        "moo1 String \"First\"" +
                        "Adding String \"First\"" +
                        "moo1 String \"Second\"" +
                        "Adding String \"Second\"" +
                        "moo1 String \"Third\"" +
                        "Adding String \"Third\"",
                stringBuilder.toString().replaceAll(System.lineSeparator(), ""));
    }
}
