package com.rubbishman.rubbishRedux;

import com.google.gson.Gson;
import com.rubbishman.rubbishRedux.dynamicObjectStore.ObjectStore;
import com.rubbishman.rubbishRedux.dynamicObjectStore.reducer.CreateObjectReducer;
import com.rubbishman.rubbishRedux.middlewareEnhancer.MiddlewareEnhancer;
import com.rubbishman.rubbishRedux.misc.MyLoggingMiddleware;
import org.junit.Before;
import org.junit.Test;
import redux.api.Store;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

public class DynamicCreateObjectTest {
    Store<ObjectStore> store;
    StringBuilder stringBuilder = new StringBuilder();
    PrintStream printStream;

    @Before
    public void setup() {
        OutputStream myOutput = new OutputStream() {
            public void write(int b) throws IOException {
                stringBuilder.append((char)b);
            }
        };

        printStream = new PrintStream(myOutput);

        Store.Creator<ObjectStore> creator = new com.glung.redux.Store.Creator();

        MiddlewareEnhancer<ObjectStore> middlewareEnhancer = new MiddlewareEnhancer<>();
        middlewareEnhancer.addMiddleware(new MyLoggingMiddleware(printStream, "MOO"));

        creator = middlewareEnhancer.enhance(creator);

        CreateObjectReducer reducer = new CreateObjectReducer();

        store = creator.create(reducer, new ObjectStore());
    }

    @Test
    public void dynamicCreateObjectTest() {
        store.dispatch("String!");
        store.dispatch(new Integer(5));
        store.dispatch(new Long(5));

        store.dispatch("String!");
        store.dispatch(new Integer(5));
        store.dispatch(new Long(5));

        Gson gson = new Gson();

        System.out.println(printStream.toString());

        System.out.println(store.getState().idGenerator.idSequence.toString());
        System.out.println(store.getState().objectMap.toString());
    }
}
