package com.rubbishman.rubbishRedux;

import com.google.gson.Gson;
import com.rubbishman.rubbishRedux.createObjectCallback.action.CreateObject;
import com.rubbishman.rubbishRedux.createObjectCallback.enhancer.CreateObjectEnhancer;
import com.rubbishman.rubbishRedux.createObjectCallback.interfaces.ICreateObjectCallback;
import com.rubbishman.rubbishRedux.middlewareEnhancer.MiddlewareEnhancer;
import com.rubbishman.rubbishRedux.misc.MyLoggingMiddleware;
import com.rubbishman.rubbishRedux.multistageActionTest.action.IncrementCounter;
import com.rubbishman.rubbishRedux.multistageActionTest.action.MultistageAction.IncrementCounterResolution;
import com.rubbishman.rubbishRedux.multistageActionTest.processor.CreateCounterProcessor;
import com.rubbishman.rubbishRedux.multistageActionTest.processor.CreateCounterStopperProcessor;
import com.rubbishman.rubbishRedux.multistageActionTest.reducer.MyReducer;
import com.rubbishman.rubbishRedux.multistageActionTest.state.Counter;
import com.rubbishman.rubbishRedux.multistageActionTest.state.MultistageActionState;
import com.rubbishman.rubbishRedux.multistageActions.MultistageActions;
import org.junit.Before;
import org.junit.Test;
import redux.api.Store;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

import static org.junit.Assert.assertEquals;

public class MultistageActionTest {
    MultistageActions<MultistageActionState> multiStageActions;
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

        Store.Creator<MultistageActionState> creator = new com.glung.redux.Store.Creator();
        CreateObjectEnhancer<MultistageActionState> enhancer = new CreateObjectEnhancer();

        enhancer.addProcessor(new CreateCounterProcessor());
        enhancer.addProcessor(new CreateCounterStopperProcessor());

        creator = enhancer.enhance(creator);

        MiddlewareEnhancer<MultistageActionState> middlewareEnhancer = new MiddlewareEnhancer<>();
        middlewareEnhancer.addMiddleware(new MyLoggingMiddleware(printStream, "MOO"));

        creator = middlewareEnhancer.enhance(creator);

        MyReducer reducer = new MyReducer(printStream);

        multiStageActions = new MultistageActions(creator, reducer, new MultistageActionState());
        multiStageActions.addMultistageProcessor(IncrementCounter.class, new IncrementCounterResolution(1234l));

        reducer.setMultistageAction(multiStageActions);
    }

    @Test
    public void testMultistageActions() {
        // TODO, this need to be changed to point at multistage.
        multiStageActions.addAction(createObjectTest(printStream, 1, 6));
        multiStageActions.addAction(createObjectTest(printStream, 2, 6));
        multiStageActions.addAction(createObjectTest(printStream, 3, 3));

        multiStageActions.addAction(new IncrementCounter(0));
        multiStageActions.addAction(new IncrementCounter(0));

        multiStageActions.addAction(new IncrementCounter(1));
        multiStageActions.addAction(new IncrementCounter(2));

        multiStageActions.addAction(new IncrementCounter(0));
        multiStageActions.addAction(new IncrementCounter(1));
        multiStageActions.addAction(new IncrementCounter(2));

        multiStageActions.doActions();

        assertEquals("Output was: " + stringBuilder.toString(),"Initial state INIT" +
                        "MOO CreateObject {\"createObject\":{\"id\":-1,\"count\":0,\"incrementDiceNum\":1,\"incrementDiceSize\":6}}" +
                        "We just created: Counter {\"id\":0,\"count\":0,\"incrementDiceNum\":1,\"incrementDiceSize\":6}" +
                        "MOO CreateObject {\"createObject\":{\"id\":-1,\"count\":0,\"incrementDiceNum\":2,\"incrementDiceSize\":6}}" +
                        "We just created: Counter {\"id\":1,\"count\":0,\"incrementDiceNum\":2,\"incrementDiceSize\":6}" +
                        "MOO CreateObject {\"createObject\":{\"id\":-1,\"count\":0,\"incrementDiceNum\":3,\"incrementDiceSize\":3}}" +
                        "We just created: Counter {\"id\":2,\"count\":0,\"incrementDiceNum\":3,\"incrementDiceSize\":3}" +
                        "MOO IncrementCounter {\"targetCounterId\":0}" +
                        "MOO IncrementCounterResolved {\"targetCounterId\":0,\"incrementAmount\":3}" +
                        "MOO IncrementCounter {\"targetCounterId\":0}" +
                        "MOO IncrementCounterResolved {\"targetCounterId\":0,\"incrementAmount\":6}" +
                        "MOO IncrementCounter {\"targetCounterId\":1}" +
                        "MOO IncrementCounterResolved {\"targetCounterId\":1,\"incrementAmount\":7}" +
                        "MOO IncrementCounter {\"targetCounterId\":2}" +
                        "MOO IncrementCounterResolved {\"targetCounterId\":2,\"incrementAmount\":6}" +
                        "MOO IncrementCounter {\"targetCounterId\":0}" +
                        "MOO IncrementCounterResolved {\"targetCounterId\":0,\"incrementAmount\":2}" +
                        "MOO IncrementCounter {\"targetCounterId\":1}" +
                        "MOO IncrementCounterResolved {\"targetCounterId\":1,\"incrementAmount\":4}" +
                        "MOO IncrementCounter {\"targetCounterId\":2}" +
                        "MOO IncrementCounterResolved {\"targetCounterId\":2,\"incrementAmount\":2}"
                        ,
                stringBuilder.toString().replaceAll(System.lineSeparator(), ""));

        assertEquals(11,multiStageActions.getState().counterObjects.get(0).count);
        assertEquals(11,multiStageActions.getState().counterObjects.get(1).count);
        assertEquals(8,multiStageActions.getState().counterObjects.get(2).count);
    }

    private CreateObject createObjectTest(PrintStream printStream, int incrementDiceNum, int incrementDiceSize) {
        CreateObject newObjectCreator = new CreateObject();
        Counter myStateObject = new Counter(-1,0, incrementDiceNum, incrementDiceSize);

        newObjectCreator.createObject = myStateObject;
        newObjectCreator.callback = new ICreateObjectCallback() {
            public void postCreateState(Object object) {
                Gson gson = new Gson();
                printStream.println("We just created: " + object.getClass().getSimpleName() + " " + gson.toJson(object));
            }
        };

        return newObjectCreator;
    }
}
