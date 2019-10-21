package com.rubbishman.rubbishRedux;

import com.google.gson.Gson;
import com.rubbishman.rubbishRedux.createObjectCallback.action.CreateObject;
import com.rubbishman.rubbishRedux.createObjectCallback.enhancer.CreateObjectEnhancer;
import com.rubbishman.rubbishRedux.createObjectCallback.interfaces.ICreateObjectCallback;
import com.rubbishman.rubbishRedux.createObjectCallbackTest.processor.CreateObjectProcessor;
import com.rubbishman.rubbishRedux.createObjectCallbackTest.state.CreateObjectState;
import com.rubbishman.rubbishRedux.createObjectCallbackTest.state.CreateObjectStateObject;
import com.rubbishman.rubbishRedux.dynamicObjectStore.GsonInstance;
import org.junit.Test;
import redux.api.Store;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

import static org.junit.Assert.assertEquals;

public class CreateObjectCallbackTest {

    @Test
    public void testCreateObjectCallback() {
        StringBuilder stringBuilder = new StringBuilder();

        OutputStream myOutput = new OutputStream() {
            public void write(int b) throws IOException {
                stringBuilder.append((char)b);
            }
        };

        PrintStream printStream = new PrintStream(myOutput);

        Store.Creator<CreateObjectState> creator = new com.glung.redux.Store.Creator();

        CreateObjectEnhancer<CreateObjectState> enhancer = new CreateObjectEnhancer();
        enhancer.addProcessor(new CreateObjectProcessor());

        creator = enhancer.enhance(creator);

        CreateObject newObjectCreator = createObjectTest(printStream);

        Store<CreateObjectState> store = creator.create(null, new CreateObjectState());

        store.dispatch(newObjectCreator);
        store.dispatch(newObjectCreator);
        store.dispatch(newObjectCreator);

        assertEquals("We just created: CreateObjectStateObject {\"id\":0,\"message\":\"MOOOOO\"}" +
                        "We just created: CreateObjectStateObject {\"id\":1,\"message\":\"MOOOOO\"}" +
                        "We just created: CreateObjectStateObject {\"id\":2,\"message\":\"MOOOOO\"}",
                stringBuilder.toString().replaceAll(System.lineSeparator(), ""));
    }

    private CreateObject createObjectTest(PrintStream printStream) {
        CreateObject newObjectCreator = new CreateObject();
        CreateObjectStateObject myStateObject = new CreateObjectStateObject();
        myStateObject.message = "MOOOOO";
        newObjectCreator.createObject = myStateObject;
        newObjectCreator.callback = new ICreateObjectCallback() {
            public void postCreateState(Object object) {
                Gson gson = GsonInstance.getInstance();
                printStream.println("We just created: " + object.getClass().getSimpleName() + " " + gson.toJson(object));
            }
        };

        return newObjectCreator;
    }
}
