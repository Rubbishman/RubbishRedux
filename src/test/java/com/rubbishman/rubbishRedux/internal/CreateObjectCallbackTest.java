package com.rubbishman.rubbishRedux.internal;

import com.google.gson.Gson;
import com.rubbishman.rubbishRedux.external.operational.action.createObject.CreateObject;
import com.rubbishman.rubbishRedux.external.setup_extra.createObject.CreateObjectEnhancer;
import com.rubbishman.rubbishRedux.external.operational.action.createObject.ICreateObjectCallback;
import com.rubbishman.rubbishRedux.internal.createObjectCallbackTest.state.CreateObjectStateObject;
import com.rubbishman.rubbishRedux.internal.dynamicObjectStore.GsonInstance;
import com.rubbishman.rubbishRedux.external.operational.store.ObjectStore;
import org.junit.Before;
import org.junit.Test;
import redux.api.Store;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

import static org.junit.Assert.assertEquals;

public class CreateObjectCallbackTest {
    private Store<ObjectStore> store;
    private StringBuilder stringBuilder;
    private CreateObject newObjectCreator;

    @Before
    public void setup() {
        stringBuilder = new StringBuilder();

        OutputStream myOutput = new OutputStream() {
            public void write(int b) throws IOException {
                stringBuilder.append((char)b);
            }
        };

        PrintStream printStream = new PrintStream(myOutput);

        Store.Creator<ObjectStore> creator = new com.glung.redux.Store.Creator();

        CreateObjectEnhancer enhancer = new CreateObjectEnhancer();

        creator = enhancer.enhance(creator);

        newObjectCreator = createObjectTest(printStream);

        store = creator.create(null, new ObjectStore());
    }

    @Test
    public void testCreateObjectCallback() {
        store.dispatch(newObjectCreator);
        store.dispatch(newObjectCreator);
        store.dispatch(newObjectCreator);

        assertEquals("We just created: IdObject {\"id\":{\"id\":1,\"clazz\":\"com.rubbishman.rubbishRedux.internal.createObjectCallbackTest.state.CreateObjectStateObject\"}," +
                            "\"object\":{\"message\":\"MOOOOO\"}}" +
                        "We just created: IdObject {\"id\":{\"id\":2,\"clazz\":\"com.rubbishman.rubbishRedux.internal.createObjectCallbackTest.state.CreateObjectStateObject\"}," +
                            "\"object\":{\"message\":\"MOOOOO\"}}" +
                        "We just created: IdObject {\"id\":{\"id\":3,\"clazz\":\"com.rubbishman.rubbishRedux.internal.createObjectCallbackTest.state.CreateObjectStateObject\"}," +
                            "\"object\":{\"message\":\"MOOOOO\"}}",
                stringBuilder.toString().replaceAll(System.lineSeparator(), ""));
    }

    private CreateObject createObjectTest(PrintStream printStream) {
        return (CreateObject<CreateObjectStateObject>) new CreateObject(
                new CreateObjectStateObject("MOOOOO"),
                new ICreateObjectCallback() {
                    public void postCreateState(Object object) {
                        Gson gson = GsonInstance.getInstance();
                        printStream.println("We just created: " + object.getClass().getSimpleName() + " " + gson.toJson(object));
                    }
                }
        );
    }
}
