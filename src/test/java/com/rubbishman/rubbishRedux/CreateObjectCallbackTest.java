package com.rubbishman.rubbishRedux;

import com.rubbishman.rubbishRedux.createObjectCallback.action.CreateObject;
import com.rubbishman.rubbishRedux.createObjectCallback.interfaces.ICreateObjectCallback;
import com.rubbishman.rubbishRedux.createObjectCallback.interfaces.ICreateObjectProcessor;
import com.rubbishman.rubbishRedux.createObjectCallback.reducer.CreateObjectReducer;
import com.rubbishman.rubbishRedux.createObjectCallbackTest.processor.CreateObjectProcessor;
import com.rubbishman.rubbishRedux.createObjectCallbackTest.state.CreateObjectState;
import com.rubbishman.rubbishRedux.createObjectCallbackTest.state.CreateObjectStateObject;
import org.junit.Test;
import redux.api.Store;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;

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
        CreateObjectReducer<CreateObjectState> reducer = new CreateObjectReducer<>();

        reducer.addProcessor(new CreateObjectProcessor());

        CreateObject newObjectCreator = createObjectTest(printStream);

        Store<CreateObjectState> store = creator.create(reducer, new CreateObjectState());

        store.dispatch(newObjectCreator);
        store.dispatch(newObjectCreator);
        store.dispatch(newObjectCreator);

        assertEquals("We just created: {   id: 0   message: MOOOOO}"
                + "We just created: {   id: 1   message: MOOOOO}"
                + "We just created: {   id: 2   message: MOOOOO}",
                stringBuilder.toString().replaceAll(System.lineSeparator(), ""));
    }

    private CreateObject createObjectTest(PrintStream printStream) {
        CreateObject newObjectCreator = new CreateObject();
        CreateObjectStateObject myStateObject = new CreateObjectStateObject();
        myStateObject.message = "MOOOOO";
        newObjectCreator.createObject = myStateObject;
        newObjectCreator.callback = new ICreateObjectCallback() {
            public void postCreateState(Object object) {
                printStream.println("We just created: " + object.toString());
            }
        };

        return newObjectCreator;
    }
}
