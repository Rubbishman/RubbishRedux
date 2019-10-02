package com.rubbishman.rubbishRedux;

import com.rubbishman.rubbishRedux.createObjectCallback.action.CreateObject;
import com.rubbishman.rubbishRedux.createObjectCallback.enhancer.CreateObjectEnhancer;
import com.rubbishman.rubbishRedux.createObjectCallback.interfaces.ICreateObjectCallback;
import com.rubbishman.rubbishRedux.multistageActionTest.processor.CreateCounterProcessor;
import com.rubbishman.rubbishRedux.multistageActionTest.processor.CreateCounterStopperProcessor;
import com.rubbishman.rubbishRedux.multistageActionTest.reducer.MyReducer;
import com.rubbishman.rubbishRedux.multistageActionTest.state.Counter;
import com.rubbishman.rubbishRedux.multistageActionTest.state.MultistageActionState;
import org.junit.Before;
import org.junit.Test;
import redux.api.Store;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

import static org.junit.Assert.assertEquals;

public class MultistageActionTest {
    Store<MultistageActionState> store;
    CreateObject newObjectCreator;
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

        Store.Creator<MultistageActionState> creator = new com.glung.redux.Store.Creator();
        CreateObjectEnhancer<MultistageActionState> enhancer = new CreateObjectEnhancer();

        enhancer.addProcessor(new CreateCounterProcessor());
        enhancer.addProcessor(new CreateCounterStopperProcessor());

        creator = enhancer.enhance(creator);

        MyReducer reducer = new MyReducer(printStream);

        newObjectCreator = createObjectTest(printStream);

        store = creator.create(reducer, new MultistageActionState());
    }

    @Test
    public void testMultistageActions() {
        // TODO, this need to be changed to point at multistage.
        store.dispatch(newObjectCreator);
        store.dispatch(newObjectCreator);
        store.dispatch(newObjectCreator);

        assertEquals("Initial state INIT" +
                        "We just created: [Counter(0): 0 | 1d6]" +
                        "We just created: [Counter(1): 0 | 1d6]" +
                        "We just created: [Counter(2): 0 | 1d6]"
                        ,
                stringBuilder.toString().replaceAll(System.lineSeparator(), ""));
    }

    private CreateObject createObjectTest(PrintStream printStream) {
        CreateObject newObjectCreator = new CreateObject();
        Counter myStateObject = new Counter(-1,0);

        newObjectCreator.createObject = myStateObject;
        newObjectCreator.callback = new ICreateObjectCallback() {
            public void postCreateState(Object object) {
                printStream.println("We just created: " + object.toString());
            }
        };

        return newObjectCreator;
    }
}
