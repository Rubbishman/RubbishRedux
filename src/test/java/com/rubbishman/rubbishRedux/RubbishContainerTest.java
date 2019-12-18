package com.rubbishman.rubbishRedux;

import com.google.gson.Gson;
import com.rubbishman.rubbishRedux.createObjectCallback.enhancer.CreateObjectEnhancer;
import com.rubbishman.rubbishRedux.createObjectCallback.interfaces.ICreateObjectCallback;
import com.rubbishman.rubbishRedux.dynamicObjectStore.GsonInstance;
import com.rubbishman.rubbishRedux.dynamicObjectStore.store.Identifier;
import com.rubbishman.rubbishRedux.dynamicObjectStore.store.ObjectStore;
import com.rubbishman.rubbishRedux.external.RubbishContainer;
import com.rubbishman.rubbishRedux.external.RubbishContainerCreator;
import com.rubbishman.rubbishRedux.external.RubbishContainerOptions;
import com.rubbishman.rubbishRedux.misc.MyLoggingMiddleware;
import com.rubbishman.rubbishRedux.multistageActionTest.action.IncrementCounter;
import com.rubbishman.rubbishRedux.multistageActionTest.action.IncrementCounterResolved;
import com.rubbishman.rubbishRedux.multistageActionTest.action.multistageAction.IncrementCounterResolution;

import com.rubbishman.rubbishRedux.multistageActionTest.state.Counter;
import com.rubbishman.rubbishRedux.multistageActions.action.MultistageCreateObject;
import com.rubbishman.rubbishRedux.multistageCreateObjectTest.action.mulitstageAction.CounterCreateResolution;
import com.rubbishman.rubbishRedux.multistageCreateObjectTest.action.CreateCounter;
import org.junit.Before;
import org.junit.Test;
import redux.api.Reducer;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

import static org.junit.Assert.assertEquals;

public class RubbishContainerTest {
    RubbishContainer rubbish;

    @Before
    public void setup() {
        RubbishContainerOptions options = new RubbishContainerOptions();
        setupStringLogger(options);

        options
            .addMultistageAction(IncrementCounter.class, new IncrementCounterResolution(1234))
            .addMultistageAction(MultistageCreateObject.class, new CounterCreateResolution(1234))
            .setReducer(getReducer());

        rubbish = RubbishContainerCreator.getRubbishContainer(options);
    }

    StringBuilder stringBuilder = new StringBuilder();
    PrintStream printStream;

    public void setupStringLogger(RubbishContainerOptions options) {
        OutputStream myOutput = new OutputStream() {
            public void write(int b) throws IOException {
                stringBuilder.append((char)b);
            }
        };

        printStream = new PrintStream(myOutput);

        options.addMiddleware(new MyLoggingMiddleware(printStream, "MOO"));
    }

    @Test
    public void testMultistageActions() {
        // TODO, this need to be changed to point at multistage.
        rubbish.performAction(createObjectTest(printStream, 6, 12));
        rubbish.performAction(createObjectTest(printStream, 6, 12));
        rubbish.performAction(createObjectTest(printStream, 6, 12));

        Identifier counter1 = new Identifier(1, Counter.class);
        Identifier counter2 = new Identifier(2, Counter.class);
        Identifier counter3 = new Identifier(3, Counter.class);

        rubbish.addAction(new IncrementCounter(counter1));
        rubbish.addAction(new IncrementCounter(counter1));

        rubbish.addAction(new IncrementCounter(counter2));
        rubbish.addAction(new IncrementCounter(counter3));

        rubbish.addAction(new IncrementCounter(counter1));
        rubbish.addAction(new IncrementCounter(counter2));
        rubbish.addAction(new IncrementCounter(counter3));

        rubbish.performActions();

        assertEquals("Output was: " + stringBuilder.toString(),
                        "MOO MultistageCreateObject {\"createObject\":{\"diceNumMin\":1,\"diceNumMax\":6,\"diceSizeMin\":1,\"diceSizeMax\":12}}" +
                        "MOO CreateObject {\"createObject\":{\"count\":0,\"incrementDiceNum\":5,\"incrementDiceSize\":12}}" +
                        "MOO MultistageCreateObject {\"createObject\":{\"diceNumMin\":1,\"diceNumMax\":6,\"diceSizeMin\":1,\"diceSizeMax\":12}}" +
                        "MOO CreateObject {\"createObject\":{\"count\":0,\"incrementDiceNum\":5,\"incrementDiceSize\":7}}" +
                        "MOO MultistageCreateObject {\"createObject\":{\"diceNumMin\":1,\"diceNumMax\":6,\"diceSizeMin\":1,\"diceSizeMax\":12}}" +
                        "MOO CreateObject {\"createObject\":{\"count\":0,\"incrementDiceNum\":2,\"incrementDiceSize\":9}}" +
                        "MOO IncrementCounter {\"targetCounterId\":{\"id\":1,\"clazz\":\"com.rubbishman.rubbishRedux.multistageActionTest.state.Counter\"}}" +
                        "MOO IncrementCounterResolved {\"targetCounterId\":{\"id\":1,\"clazz\":\"com.rubbishman.rubbishRedux.multistageActionTest.state.Counter\"},\"incrementAmount\":25}" +
                        "MOO IncrementCounter {\"targetCounterId\":{\"id\":1,\"clazz\":\"com.rubbishman.rubbishRedux.multistageActionTest.state.Counter\"}}" +
                        "MOO IncrementCounterResolved {\"targetCounterId\":{\"id\":1,\"clazz\":\"com.rubbishman.rubbishRedux.multistageActionTest.state.Counter\"},\"incrementAmount\":4}" +
                        "MOO IncrementCounter {\"targetCounterId\":{\"id\":2,\"clazz\":\"com.rubbishman.rubbishRedux.multistageActionTest.state.Counter\"}}" +
                        "MOO IncrementCounterResolved {\"targetCounterId\":{\"id\":2,\"clazz\":\"com.rubbishman.rubbishRedux.multistageActionTest.state.Counter\"},\"incrementAmount\":26}" +
                        "MOO IncrementCounter {\"targetCounterId\":{\"id\":3,\"clazz\":\"com.rubbishman.rubbishRedux.multistageActionTest.state.Counter\"}}" +
                        "MOO IncrementCounterResolved {\"targetCounterId\":{\"id\":3,\"clazz\":\"com.rubbishman.rubbishRedux.multistageActionTest.state.Counter\"},\"incrementAmount\":12}" +
                        "MOO IncrementCounter {\"targetCounterId\":{\"id\":1,\"clazz\":\"com.rubbishman.rubbishRedux.multistageActionTest.state.Counter\"}}" +
                        "MOO IncrementCounterResolved {\"targetCounterId\":{\"id\":1,\"clazz\":\"com.rubbishman.rubbishRedux.multistageActionTest.state.Counter\"},\"incrementAmount\":5}" +
                        "MOO IncrementCounter {\"targetCounterId\":{\"id\":2,\"clazz\":\"com.rubbishman.rubbishRedux.multistageActionTest.state.Counter\"}}" +
                        "MOO IncrementCounterResolved {\"targetCounterId\":{\"id\":2,\"clazz\":\"com.rubbishman.rubbishRedux.multistageActionTest.state.Counter\"},\"incrementAmount\":6}" +
                        "MOO IncrementCounter {\"targetCounterId\":{\"id\":3,\"clazz\":\"com.rubbishman.rubbishRedux.multistageActionTest.state.Counter\"}}" +
                        "MOO IncrementCounterResolved {\"targetCounterId\":{\"id\":3,\"clazz\":\"com.rubbishman.rubbishRedux.multistageActionTest.state.Counter\"},\"incrementAmount\":1}"
                ,
                stringBuilder.toString().replaceAll(System.lineSeparator(), ""));

        assertEquals(34,rubbish.getState().<Counter>getObject(counter1).count);
        assertEquals(32,rubbish.getState().<Counter>getObject(counter2).count);
        assertEquals(13,rubbish.getState().<Counter>getObject(counter3).count);
    }

    private MultistageCreateObject<CreateCounter> createObjectTest(PrintStream printStream, int incrementDiceNum, int incrementDiceSize) {
        MultistageCreateObject<CreateCounter> newObjectCreator = new MultistageCreateObject(
                new CreateCounter(1, incrementDiceNum, 1, incrementDiceSize),

                new ICreateObjectCallback() {
                    public void postCreateState(Object object) {
                        Gson gson = GsonInstance.getInstance();
                        printStream.println("We just created: " + object.getClass().getSimpleName() + " " + gson.toJson(object));
                    }
                }
        );

        return newObjectCreator;
    }

    private Reducer<ObjectStore> getReducer() {
        return new Reducer<ObjectStore>() {
            @Override
            public ObjectStore reduce(ObjectStore state, Object action) {
                if(action instanceof IncrementCounterResolved) {
                    IncrementCounterResolved resolved = (IncrementCounterResolved) action;

                    Counter counter = (Counter)state.objectMap.get(resolved.targetCounterId).object;

                    return state.setObject(
                            resolved.targetCounterId,
                            counter.increment(resolved.incrementAmount)
                    );
                }
                return state;
            }
        };
    }
}
