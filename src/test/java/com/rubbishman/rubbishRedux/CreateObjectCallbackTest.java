package com.rubbishman.rubbishRedux;

import com.rubbishman.rubbishRedux.createObjectCallback.action.CreateObject;
import com.rubbishman.rubbishRedux.createObjectCallback.interfaces.ICreateObjectCallback;
import com.rubbishman.rubbishRedux.createObjectCallback.interfaces.ICreateObjectProcessor;
import com.rubbishman.rubbishRedux.createObjectCallback.reducer.CreateObjectReducer;
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

        Store.Creator<MyState> creator = new com.glung.redux.Store.Creator();
        CreateObjectReducer<MyState> reducer = new CreateObjectReducer<>();

        reducer.addProcessor(new MyCreateObjectProcessor());

        CreateObject newObjectCreator = createObjectTest(printStream);

        Store<MyState> store = creator.create(reducer, new MyState());

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
        MyStateObject myStateObject = new MyStateObject();
        myStateObject.message = "MOOOOO";
        newObjectCreator.createObject = myStateObject;
        newObjectCreator.callback = new ICreateObjectCallback() {
            public void postCreateState(Object object) {
                printStream.println("We just created: " + object.toString());
            }
        };

        return newObjectCreator;
    }

    private class MyCreateObjectProcessor implements ICreateObjectProcessor<MyState, MyStateObject> {
        private MyStateObject createdObject;
        public MyStateObject getPostCreateObject() {
            return createdObject;
        }

        public MyState run(MyState state, CreateObject action) {
            if(action.createObject instanceof MyStateObject) {
                createdObject = ((MyStateObject)action.createObject).clone();
                createdObject.id = state.stateObjects.size();
                state.stateObjects.add(createdObject);
            }

            return state;
        }
    }

//    private class MyStateObjectCreator {
//        public String message;
//    }

    private class MyStateObject {
        public int id;
        public String message;

        public String toString() {
            return "{" +
                    "   id: " + id +
                    "   message: " + message +
                    "}";
        }

        public MyStateObject clone() {
            MyStateObject clone = new MyStateObject();
            clone.id = id;
            clone.message = message;

            return clone;
        }
    }

    private class MyState {
        public ArrayList<Object> actions = new ArrayList<>();
        public ArrayList<MyStateObject> stateObjects = new ArrayList<>();

        public MyState clone() {
            MyState clone = new MyState();

            clone.actions.addAll(this.actions);
            clone.stateObjects.addAll(this.stateObjects);

            return clone;
        }

        public String toString() {
            return "{" +
                    "   actions: " + actions.toString() +
                    "   stateObjects: " + stateObjects.toString() +
                   "}";
        }
    }
}
