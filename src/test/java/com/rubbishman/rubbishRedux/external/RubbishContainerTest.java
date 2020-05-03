package com.rubbishman.rubbishRedux.external;

import com.google.common.collect.ImmutableList;
import com.google.gson.Gson;
import com.rubbishman.rubbishRedux.external.setup_extra.actionTrack.stage.StageWrap;
import com.rubbishman.rubbishRedux.external.operational.action.multistageAction.Stage.Stage;
import com.rubbishman.rubbishRedux.external.setup.IRubbishReducer;
import com.rubbishman.rubbishRedux.external.setup.RubbishContainerCreator;
import com.rubbishman.rubbishRedux.external.setup.RubbishContainerOptions;
import com.rubbishman.rubbishRedux.external.operational.action.createObject.ICreateObjectCallback;
import com.rubbishman.rubbishRedux.internal.dynamicObjectStore.GsonInstance;
import com.rubbishman.rubbishRedux.external.operational.store.Identifier;
import com.rubbishman.rubbishRedux.external.operational.store.ObjectStore;
import com.rubbishman.rubbishRedux.internal.misc.MyLoggingMiddleware;
import com.rubbishman.rubbishRedux.internal.multistageActionTest.action.IncrementCounter;
import com.rubbishman.rubbishRedux.internal.multistageActionTest.action.IncrementCounterResolved;
import com.rubbishman.rubbishRedux.internal.multistageActionTest.action.multistageAction.IncrementCounterResolution;

import com.rubbishman.rubbishRedux.internal.multistageActionTest.state.Counter;
import com.rubbishman.rubbishRedux.external.operational.action.createObject.IMultistageCreateObject;
import com.rubbishman.rubbishRedux.internal.multistageCreateObjectTest.action.mulitstageAction.CounterCreateResolution;
import com.rubbishman.rubbishRedux.internal.multistageCreateObjectTest.action.CreateCounter;
import org.junit.Before;
import org.junit.Test;

import java.io.OutputStream;
import java.io.PrintStream;

import static org.junit.Assert.assertEquals;

public class RubbishContainerTest {
    private RubbishContainer rubbish;
    private StringBuilder stringBuilder = new StringBuilder();
    private PrintStream printStream;

    @Before
    public void setup() {
        RubbishContainerOptions options = new RubbishContainerOptions();
        setupStringLogger(options);

        try {
            Stage createStage = options.createStage("CreateCounter");
            Stage incrementStage = options.createStage("IncrementCounter");

            options.setStageProcessor(IncrementCounter.class,
                    ImmutableList.of(
                            new StageWrap(incrementStage, new IncrementCounterResolution(1234))
                    )
            );

            options.setStageProcessor(CounterCreateObject.class,
                    ImmutableList.of(
                            new StageWrap(createStage, new CounterCreateResolution(1234))
                    )
            );
        } catch (Exception e) {
            e.printStackTrace();
        }

        options
            .setReducer(getReducer());

        rubbish = RubbishContainerCreator.getRubbishContainer(options);
    }


    public void setupStringLogger(RubbishContainerOptions options) {
        OutputStream myOutput = new OutputStream() {
            public void write(int b) {
                stringBuilder.append((char)b);
            }
        };

        printStream = new PrintStream(myOutput);

        options.addMiddleware(new MyLoggingMiddleware(printStream, "MOO"));
    }

    @Test
    public void testMultistageActions() {
        // TODO, this need to be changed to point at multistage.
        rubbish.performAction(new CounterCreateObject(6, 12));
        rubbish.performAction(new CounterCreateObject(6, 12));
        rubbish.performAction(new CounterCreateObject(6, 12));

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
                        "MOO CreateObject {\"createObject\":{\"count\":0,\"incrementDiceNum\":5,\"incrementDiceSize\":12}}" +
                                "We just created: IdObject {\"id\":{\"id\":1,\"clazz\":\"com.rubbishman.rubbishRedux.internal.multistageActionTest.state.Counter\"},\"object\":{\"count\":0,\"incrementDiceNum\":5,\"incrementDiceSize\":12}}" +
                                "MOO CreateObject {\"createObject\":{\"count\":0,\"incrementDiceNum\":5,\"incrementDiceSize\":7}}" +
                                "We just created: IdObject {\"id\":{\"id\":2,\"clazz\":\"com.rubbishman.rubbishRedux.internal.multistageActionTest.state.Counter\"},\"object\":{\"count\":0,\"incrementDiceNum\":5,\"incrementDiceSize\":7}}" +
                                "MOO CreateObject {\"createObject\":{\"count\":0,\"incrementDiceNum\":2,\"incrementDiceSize\":9}}" +
                                "We just created: IdObject {\"id\":{\"id\":3,\"clazz\":\"com.rubbishman.rubbishRedux.internal.multistageActionTest.state.Counter\"},\"object\":{\"count\":0,\"incrementDiceNum\":2,\"incrementDiceSize\":9}}" +
                                "MOO IncrementCounterResolved {\"targetCounterId\":{\"id\":1,\"clazz\":\"com.rubbishman.rubbishRedux.internal.multistageActionTest.state.Counter\"},\"incrementAmount\":25}" +
                                "MOO IncrementCounterResolved {\"targetCounterId\":{\"id\":3,\"clazz\":\"com.rubbishman.rubbishRedux.internal.multistageActionTest.state.Counter\"},\"incrementAmount\":12}" +
                                "MOO IncrementCounterResolved {\"targetCounterId\":{\"id\":2,\"clazz\":\"com.rubbishman.rubbishRedux.internal.multistageActionTest.state.Counter\"},\"incrementAmount\":25}" +
                                "MOO IncrementCounterResolved {\"targetCounterId\":{\"id\":1,\"clazz\":\"com.rubbishman.rubbishRedux.internal.multistageActionTest.state.Counter\"},\"incrementAmount\":6}" +
                                "MOO IncrementCounterResolved {\"targetCounterId\":{\"id\":3,\"clazz\":\"com.rubbishman.rubbishRedux.internal.multistageActionTest.state.Counter\"},\"incrementAmount\":5}" +
                                "MOO IncrementCounterResolved {\"targetCounterId\":{\"id\":2,\"clazz\":\"com.rubbishman.rubbishRedux.internal.multistageActionTest.state.Counter\"},\"incrementAmount\":6}" +
                                "MOO IncrementCounterResolved {\"targetCounterId\":{\"id\":1,\"clazz\":\"com.rubbishman.rubbishRedux.internal.multistageActionTest.state.Counter\"},\"incrementAmount\":1}"
                ,
                stringBuilder.toString().replaceAll(System.lineSeparator(), ""));

        assertEquals(32,rubbish.getState().<Counter>getObject(counter1).count);
        assertEquals(31,rubbish.getState().<Counter>getObject(counter2).count);
        assertEquals(17,rubbish.getState().<Counter>getObject(counter3).count);
    }

    private IRubbishReducer getReducer() {
        return new IRubbishReducer() {
            @Override
            public ObjectStore reduce(ObjectStore state, Object action) {
                if(action instanceof IncrementCounterResolved) {
                    IncrementCounterResolved resolved = (IncrementCounterResolved) action;

                    Counter counter = state.getObject(resolved.targetCounterId);

                    return state.setObject(
                            resolved.targetCounterId,
                            counter.increment(resolved.incrementAmount)
                    );
                }
                return state;
            }
        };
    }

    public class CounterCreateObject extends IMultistageCreateObject<CreateCounter> {
        public CounterCreateObject(int incrementDiceNum, int incrementDiceSize) {
            super(
                new CreateCounter(1, incrementDiceNum, 1, incrementDiceSize),
                new ICreateObjectCallback() {
                    public void postCreateState(Object object) {
                        Gson gson = GsonInstance.getInstance();
                        printStream.println("We just created: " + object.getClass().getSimpleName() + " " + gson.toJson(object));
                    }
                }
            );
        }
    }
}
