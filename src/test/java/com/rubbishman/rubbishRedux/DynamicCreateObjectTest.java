package com.rubbishman.rubbishRedux;

import com.google.gson.Gson;
import com.rubbishman.rubbishRedux.internal.createObjectCallback.action.CreateObject;
import com.rubbishman.rubbishRedux.internal.createObjectCallback.interfaces.ICreateObjectCallback;
import com.rubbishman.rubbishRedux.internal.dynamicObjectStore.GsonInstance;
import com.rubbishman.rubbishRedux.internal.dynamicObjectStore.store.ObjectStore;
import com.rubbishman.rubbishRedux.internal.dynamicObjectStore.reducer.CreateObjectReducer;
import com.rubbishman.rubbishRedux.internal.middlewareEnhancer.MiddlewareEnhancer;
import com.rubbishman.rubbishRedux.internal.misc.MyLoggingMiddleware;
import org.junit.Before;
import org.junit.Test;
import redux.api.Store;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

import static org.junit.Assert.assertEquals;

public class DynamicCreateObjectTest {
    private Store<ObjectStore> store;
    private StringBuilder stringBuilder = new StringBuilder();
    private PrintStream printStream;
    private CreateObjectReducer reducer;

    @Before
    public void setup() {
        OutputStream myOutput = new OutputStream() {
            public void write(int b) {
                stringBuilder.append((char)b);
            }
        };

        printStream = new PrintStream(myOutput);

        Store.Creator<ObjectStore> creator = new com.glung.redux.Store.Creator();

        MiddlewareEnhancer<ObjectStore> middlewareEnhancer = new MiddlewareEnhancer<>();
        middlewareEnhancer.addMiddleware(new MyLoggingMiddleware(printStream, "MOO"));

        creator = middlewareEnhancer.enhance(creator);

        reducer = new CreateObjectReducer();

        store = creator.create(reducer, new ObjectStore());
    }

    @Test
    public void dynamicCreateObjectTest() {
        // This looks good... Now drop this in place of the existing createObject enhancer stuff.
        // And then the multistage stuff -> how are the reducers going to end up after doing this...
        store.dispatch(createObjectTest("String!", printStream));
        reducer.postDispatch();
        store.dispatch(createObjectTest(5, printStream));
        reducer.postDispatch();
        store.dispatch(createObjectTest(5, printStream));
        reducer.postDispatch();

        store.dispatch(createObjectTest("String!", printStream));
        reducer.postDispatch();
        store.dispatch(createObjectTest(5, printStream));
        reducer.postDispatch();
        store.dispatch(createObjectTest(5, printStream));
        reducer.postDispatch();

        Gson gson = GsonInstance.getInstance();

        assertEquals("MOO CreateObject {\"createObject\":\"String!\"}" +
                        "We just created: IdObject {\"id\":{\"id\":1,\"clazz\":\"java.lang.String\"},\"object\":\"String!\"}" +
                        "MOO CreateObject {\"createObject\":5}" +
                        "We just created: IdObject {\"id\":{\"id\":1,\"clazz\":\"java.lang.Integer\"},\"object\":5}" +
                        "MOO CreateObject {\"createObject\":5}" +
                        "We just created: IdObject {\"id\":{\"id\":1,\"clazz\":\"java.lang.Long\"},\"object\":5}" +
                        "MOO CreateObject {\"createObject\":\"String!\"}" +
                        "We just created: IdObject {\"id\":{\"id\":2,\"clazz\":\"java.lang.String\"},\"object\":\"String!\"}" +
                        "MOO CreateObject {\"createObject\":5}" +
                        "We just created: IdObject {\"id\":{\"id\":2,\"clazz\":\"java.lang.Integer\"},\"object\":5}" +
                        "MOO CreateObject {\"createObject\":5}" +
                        "We just created: IdObject {\"id\":{\"id\":2,\"clazz\":\"java.lang.Long\"},\"object\":5}",
                stringBuilder.toString().replaceAll(System.lineSeparator(), ""));
// Replace this with a better check since the map is not always going to look the same...
//        assertEquals("{\"objectMap\":[{\"key\":\"{\\\"id\\\":1,\\\"clazz\\\":\\\"java.lang.Long\\\"}\",\"value\":\"{\\\"id\\\":{\\\"id\\\":1,\\\"clazz\\\":\\\"java.lang.Long\\\"},\\\"object\\\":5}\"},{\"key\":\"{\\\"id\\\":2,\\\"clazz\\\":\\\"java.lang.String\\\"}\",\"value\":\"{\\\"id\\\":{\\\"id\\\":2,\\\"clazz\\\":\\\"java.lang.String\\\"},\\\"object\\\":\\\"String!\\\"}\"},{\"key\":\"{\\\"id\\\":1,\\\"clazz\\\":\\\"java.lang.Integer\\\"}\",\"value\":\"{\\\"id\\\":{\\\"id\\\":1,\\\"clazz\\\":\\\"java.lang.Integer\\\"},\\\"object\\\":5}\"},{\"key\":\"{\\\"id\\\":2,\\\"clazz\\\":\\\"java.lang.Long\\\"}\",\"value\":\"{\\\"id\\\":{\\\"id\\\":2,\\\"clazz\\\":\\\"java.lang.Long\\\"},\\\"object\\\":5}\"},{\"key\":\"{\\\"id\\\":1,\\\"clazz\\\":\\\"java.lang.String\\\"}\",\"value\":\"{\\\"id\\\":{\\\"id\\\":1,\\\"clazz\\\":\\\"java.lang.String\\\"},\\\"object\\\":\\\"String!\\\"}\"},{\"key\":\"{\\\"id\\\":2,\\\"clazz\\\":\\\"java.lang.Integer\\\"}\",\"value\":\"{\\\"id\\\":{\\\"id\\\":2,\\\"clazz\\\":\\\"java.lang.Integer\\\"},\\\"object\\\":5}\"}],\"idGenerator\":\"{\\\"idSequence\\\":{\\\"class java.lang.Long\\\":2,\\\"class java.lang.Integer\\\":2,\\\"class java.lang.String\\\":2}}\"}",
////                gson.toJson(store.getState()));
    }

    private CreateObject createObjectTest(Object object, PrintStream printStream) {
        return new CreateObject(
                object,

                new ICreateObjectCallback() {
                    public void postCreateState(Object object1) {
                        Gson gson = GsonInstance.getInstance();
                        printStream.println("We just created: " + object1.getClass().getSimpleName() + " " + gson.toJson(object1));
                    }
                }
        );
    }
}
