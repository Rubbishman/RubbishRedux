package com.rubbishman.rubbishRedux;

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
                        "MOO [CREATE:[Counter(-1): 0 | 1d6]]" +
                        "We just created: [Counter(0): 0 | 1d6]" +
                        "MOO [CREATE:[Counter(-1): 0 | 2d6]]" +
                        "We just created: [Counter(1): 0 | 2d6]" +
                        "MOO [CREATE:[Counter(-1): 0 | 3d3]]" +
                        "We just created: [Counter(2): 0 | 3d3]" +
                        "MOO [Increment: (0)]" +
                        "MOO [Resolved Increment: (0) +3]" +
                        "MOO [Increment: (0)]" +
                        "MOO [Resolved Increment: (0) +6]" +
                        "MOO [Increment: (1)]" +
                        "MOO [Resolved Increment: (1) +7]" +
                        "MOO [Increment: (2)]" +
                        "MOO [Resolved Increment: (2) +6]" +
                        "MOO [Increment: (0)]" +
                        "MOO [Resolved Increment: (0) +2]" +
                        "MOO [Increment: (1)]" +
                        "MOO [Resolved Increment: (1) +4]" +
                        "MOO [Increment: (2)]" +
                        "MOO [Resolved Increment: (2) +2]"
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
                printStream.println("We just created: " + object.toString());
            }
        };

        return newObjectCreator;
    }
}
