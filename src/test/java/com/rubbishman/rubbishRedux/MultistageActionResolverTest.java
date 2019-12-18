package com.rubbishman.rubbishRedux;

import com.google.gson.Gson;
import com.rubbishman.rubbishRedux.createObjectCallback.action.CreateObject;
import com.rubbishman.rubbishRedux.createObjectCallback.enhancer.CreateObjectEnhancer;
import com.rubbishman.rubbishRedux.createObjectCallback.interfaces.ICreateObjectCallback;
import com.rubbishman.rubbishRedux.dynamicObjectStore.GsonInstance;
import com.rubbishman.rubbishRedux.dynamicObjectStore.store.Identifier;
import com.rubbishman.rubbishRedux.dynamicObjectStore.store.ObjectStore;
import com.rubbishman.rubbishRedux.middlewareEnhancer.MiddlewareEnhancer;
import com.rubbishman.rubbishRedux.misc.MyLoggingMiddleware;
import com.rubbishman.rubbishRedux.multistageActionTest.action.IncrementCounter;
import com.rubbishman.rubbishRedux.multistageActionTest.action.multistageAction.IncrementCounterResolution;
import com.rubbishman.rubbishRedux.multistageActionTest.reducer.MyReducer;
import com.rubbishman.rubbishRedux.multistageActionTest.state.Counter;
import com.rubbishman.rubbishRedux.multistageActions.MultistageActions;
import org.junit.Before;
import org.junit.Test;
import redux.api.Store;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

import static org.junit.Assert.assertEquals;

public class MultistageActionResolverTest {
    MultistageActions multiStageActions;
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
        CreateObjectEnhancer enhancer = new CreateObjectEnhancer();

        creator = enhancer.enhance(creator);

        MiddlewareEnhancer<ObjectStore> middlewareEnhancer = new MiddlewareEnhancer<>();
        middlewareEnhancer.addMiddleware(new MyLoggingMiddleware(printStream, "MOO"));

        creator = middlewareEnhancer.enhance(creator);

        MyReducer reducer = new MyReducer(printStream);

        multiStageActions = new MultistageActions(creator, reducer, new ObjectStore());
        multiStageActions.addMultistageProcessor(IncrementCounter.class, new IncrementCounterResolution(1234));

        reducer.setMultiStageActions(multiStageActions);
    }

    @Test
    public void testMultistageActions() {
        // TODO, this need to be changed to point at multistage.
        multiStageActions.addAction(createObjectTest(printStream, 1, 6));
        multiStageActions.addAction(createObjectTest(printStream, 2, 6));
        multiStageActions.addAction(createObjectTest(printStream, 3, 3));

        Identifier counter1 = new Identifier(1, Counter.class);
        Identifier counter2 = new Identifier(2, Counter.class);
        Identifier counter3 = new Identifier(3, Counter.class);

        multiStageActions.addAction(new IncrementCounter(counter1));
        multiStageActions.addAction(new IncrementCounter(counter1));

        multiStageActions.addAction(new IncrementCounter(counter2));
        multiStageActions.addAction(new IncrementCounter(counter3));

        multiStageActions.addAction(new IncrementCounter(counter1));
        multiStageActions.addAction(new IncrementCounter(counter2));
        multiStageActions.addAction(new IncrementCounter(counter3));

        multiStageActions.doActions(0l);

        assertEquals("Output was: " + stringBuilder.toString(),"Initial state INIT" +
                        "MOO CreateObject {\"createObject\":{\"count\":0,\"incrementDiceNum\":1,\"incrementDiceSize\":6}}" +
                        "We just created: IdObject {\"id\":{\"id\":1,\"clazz\":\"com.rubbishman.rubbishRedux.multistageActionTest.state.Counter\"},\"object\":{\"count\":0,\"incrementDiceNum\":1,\"incrementDiceSize\":6}}" +
                        "MOO CreateObject {\"createObject\":{\"count\":0,\"incrementDiceNum\":2,\"incrementDiceSize\":6}}" +
                        "We just created: IdObject {\"id\":{\"id\":2,\"clazz\":\"com.rubbishman.rubbishRedux.multistageActionTest.state.Counter\"},\"object\":{\"count\":0,\"incrementDiceNum\":2,\"incrementDiceSize\":6}}" +
                        "MOO CreateObject {\"createObject\":{\"count\":0,\"incrementDiceNum\":3,\"incrementDiceSize\":3}}" +
                        "We just created: IdObject {\"id\":{\"id\":3,\"clazz\":\"com.rubbishman.rubbishRedux.multistageActionTest.state.Counter\"},\"object\":{\"count\":0,\"incrementDiceNum\":3,\"incrementDiceSize\":3}}" +
                        "MOO IncrementCounter {\"targetCounterId\":{\"id\":1,\"clazz\":\"com.rubbishman.rubbishRedux.multistageActionTest.state.Counter\"}}" +
                        "MOO IncrementCounterResolved {\"targetCounterId\":{\"id\":1,\"clazz\":\"com.rubbishman.rubbishRedux.multistageActionTest.state.Counter\"},\"incrementAmount\":3}" +
                        "MOO IncrementCounter {\"targetCounterId\":{\"id\":1,\"clazz\":\"com.rubbishman.rubbishRedux.multistageActionTest.state.Counter\"}}" +
                        "MOO IncrementCounterResolved {\"targetCounterId\":{\"id\":1,\"clazz\":\"com.rubbishman.rubbishRedux.multistageActionTest.state.Counter\"},\"incrementAmount\":6}" +
                        "MOO IncrementCounter {\"targetCounterId\":{\"id\":2,\"clazz\":\"com.rubbishman.rubbishRedux.multistageActionTest.state.Counter\"}}" +
                        "MOO IncrementCounterResolved {\"targetCounterId\":{\"id\":2,\"clazz\":\"com.rubbishman.rubbishRedux.multistageActionTest.state.Counter\"},\"incrementAmount\":7}" +
                        "MOO IncrementCounter {\"targetCounterId\":{\"id\":3,\"clazz\":\"com.rubbishman.rubbishRedux.multistageActionTest.state.Counter\"}}" +
                        "MOO IncrementCounterResolved {\"targetCounterId\":{\"id\":3,\"clazz\":\"com.rubbishman.rubbishRedux.multistageActionTest.state.Counter\"},\"incrementAmount\":6}" +
                        "MOO IncrementCounter {\"targetCounterId\":{\"id\":1,\"clazz\":\"com.rubbishman.rubbishRedux.multistageActionTest.state.Counter\"}}" +
                        "MOO IncrementCounterResolved {\"targetCounterId\":{\"id\":1,\"clazz\":\"com.rubbishman.rubbishRedux.multistageActionTest.state.Counter\"},\"incrementAmount\":2}" +
                        "MOO IncrementCounter {\"targetCounterId\":{\"id\":2,\"clazz\":\"com.rubbishman.rubbishRedux.multistageActionTest.state.Counter\"}}" +
                        "MOO IncrementCounterResolved {\"targetCounterId\":{\"id\":2,\"clazz\":\"com.rubbishman.rubbishRedux.multistageActionTest.state.Counter\"},\"incrementAmount\":4}" +
                        "MOO IncrementCounter {\"targetCounterId\":{\"id\":3,\"clazz\":\"com.rubbishman.rubbishRedux.multistageActionTest.state.Counter\"}}" +
                        "MOO IncrementCounterResolved {\"targetCounterId\":{\"id\":3,\"clazz\":\"com.rubbishman.rubbishRedux.multistageActionTest.state.Counter\"},\"incrementAmount\":2}"
                        ,
                stringBuilder.toString().replaceAll(System.lineSeparator(), ""));

        assertEquals(11,multiStageActions.getState().<Counter>getObject(counter1).count);
        assertEquals(11,multiStageActions.getState().<Counter>getObject(counter2).count);
        assertEquals(8,multiStageActions.getState().<Counter>getObject(counter3).count);
    }

    private CreateObject<Counter> createObjectTest(PrintStream printStream, int incrementDiceNum, int incrementDiceSize) {
        CreateObject<Counter> newObjectCreator = new CreateObject(
                new Counter(0, incrementDiceNum, incrementDiceSize),

                new ICreateObjectCallback() {
                    public void postCreateState(Object object) {
                        Gson gson = GsonInstance.getInstance();
                        printStream.println("We just created: " + object.getClass().getSimpleName() + " " + gson.toJson(object));
                    }
                }
        );

        return newObjectCreator;
    }
}