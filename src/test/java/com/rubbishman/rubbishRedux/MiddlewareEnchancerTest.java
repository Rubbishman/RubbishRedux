package com.rubbishman.rubbishRedux;

import com.rubbishman.rubbishRedux.internal.middlewareEnhancer.MiddlewareEnhancer;
import com.rubbishman.rubbishRedux.misc.MyLoggingMiddleware;
import com.rubbishman.rubbishRedux.misc.MyReducer;
import com.rubbishman.rubbishRedux.misc.MyState;
import org.junit.Before;
import org.junit.Test;
import redux.api.Store;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

import static org.junit.Assert.assertEquals;

public class MiddlewareEnchancerTest {
    Store<MyState> store;
    StringBuilder stringBuilder;

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
        enhancer.addMiddleware(new MyLoggingMiddleware(printStream, "moo2"));
        enhancer.addMiddleware(new MyLoggingMiddleware(printStream, "moo3"));

        creator = enhancer.enhance(creator);

        store = creator.create(reducer, new MyState());
    }

    @Test
    public void testMiddlewareEnhancer() {
        store.dispatch("First");
        store.dispatch("Second");
        store.dispatch("Third");

        assertEquals(
       "Initial state INIT" +
               "moo1 String \"First\"" +
               "moo2 String \"First\"" +
               "moo3 String \"First\"" +
               "Adding String \"First\"" +
               "moo1 String \"Second\"" +
               "moo2 String \"Second\"" +
               "moo3 String \"Second\"" +
               "Adding String \"Second\"" +
               "moo1 String \"Third\"" +
               "moo2 String \"Third\"" +
               "moo3 String \"Third\"" +
               "Adding String \"Third\"",
                stringBuilder.toString().replaceAll(System.lineSeparator(), ""));
    }
}
