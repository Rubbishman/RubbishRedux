package com.rubbishman.rubbishRedux.experimental.actionTrack;

import com.google.common.collect.ImmutableList;
import com.rubbishman.rubbishRedux.external.RubbishContainer;
import com.rubbishman.rubbishRedux.external.operational.action.multistageAction.Stage.Stage;
import com.rubbishman.rubbishRedux.external.operational.store.ObjectStore;
import com.rubbishman.rubbishRedux.external.setup.IRubbishReducer;
import com.rubbishman.rubbishRedux.external.setup.RubbishContainerCreator;
import com.rubbishman.rubbishRedux.external.setup.RubbishContainerOptions;
import com.rubbishman.rubbishRedux.internal.misc.MyLoggingMiddleware;
import org.junit.Before;
import org.junit.Test;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.HashMap;

import static org.junit.Assert.assertEquals;

public class ActionTrackTest {
    private RubbishContainer rubbish;
    private StringBuilder stringBuilder = new StringBuilder();
    private PrintStream printStream;

    @Before
    public void setup() {
        RubbishContainerOptions options = new RubbishContainerOptions();
        setupStringLogger(options);

        options.setReducer(new IRubbishReducer() {
            @Override
            public ObjectStore reduce(ObjectStore state, Object action) {
                return state;
            }
        });

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
    public void basicTest() {
        Stage stageOne = new Stage("1", 1);
        Stage stageTwo = new Stage("2", 2);
        Stage stageThree = new Stage("3", 3);
        Stage stageFour = new Stage("4", 4);

        HashMap<Class, ImmutableList<StageWrap>> actionStageMap = new HashMap<>();

        actionStageMap.put(TestOne.class,
                ImmutableList.of(
                        new StageWrap(stageOne, new StageOneProcessor()),
                        new StageWrap(stageTwo, new StageTwoProcessor())
                )
        );

        actionStageMap.put(TestTwo.class,
                ImmutableList.of(
                        new StageWrap(stageTwo, new StageTwo2Processor()),
                        new StageWrap(stageThree, new StageThreeProcessor())
                )
        );

        actionStageMap.put(TestThree.class,
                ImmutableList.of(
                        new StageWrap(stageThree, new StageThree3Processor()),
                        new StageWrap(stageFour, new StageFourProcessor())
                )
        );

        StageStack stageStack = new StageStack(actionStageMap);

        ActionTrack actionTrack = new ActionTrack(rubbish, stageStack);

        actionTrack.addAction(new TestTwo());
        actionTrack.addAction(new TestThree());
        actionTrack.addAction(new TestOne());
        actionTrack.addAction(new TestOne());
        actionTrack.addAction(new TestNoStage());
        actionTrack.addAction(new TestNoStage());

        while(actionTrack.hasNext()) {
            actionTrack.processNextAction();
        }

        assertEquals("MOO String \"StageTwo: StageOne: com.rubbishman.rubbishRedux.experimental.actionTrack.ActionTrackTest$TestOne@289dd121\"" +
                "MOO String \"StageTwo: StageOne: com.rubbishman.rubbishRedux.experimental.actionTrack.ActionTrackTest$TestOne@4049f72a\"" +
                "MOO String \"StageThree: StageTwo2: com.rubbishman.rubbishRedux.experimental.actionTrack.ActionTrackTest$TestTwo@349d1bac\"" +
                "MOO String \"StageFour: StageThree3: com.rubbishman.rubbishRedux.experimental.actionTrack.ActionTrackTest$TestThree@7ade4ea0\"" +
                "MOO TestNoStage {}" +
                "MOO TestNoStage {}",
                stringBuilder.toString().replaceAll(System.lineSeparator(), ""));
    }

    private class TestOne {}

    private class TestTwo {}

    private class TestThree {}

    private class TestNoStage {}

    private class StageOneProcessor implements StageProcessor<TestOne> {

        @Override
        public StageWrapResult processStage(StageWrappedAction action) {
            return new StageWrapResult(
                    "StageOne: " + action.currentAction,
                    "StageOne: " + action.currentAction,
                    null
            );
        }
    }

    private class StageTwoProcessor implements StageProcessor<TestOne> {

        @Override
        public StageWrapResult processStage(StageWrappedAction action) {
            return new StageWrapResult(
                    null,
                    "StageTwo: " + action.currentAction,
                    "StageTwo: " + action.currentAction
            );
        }
    }

    private class StageTwo2Processor implements StageProcessor<TestTwo> {

        @Override
        public StageWrapResult processStage(StageWrappedAction action) {
            return new StageWrapResult(
                    "StageTwo2: " + action.currentAction,
                    "StageTwo2: " + action.currentAction,
                    null
            );
        }
    }

    private class StageThreeProcessor implements StageProcessor<TestTwo> {

        @Override
        public StageWrapResult processStage(StageWrappedAction action) {
            return new StageWrapResult(
                    null,
                    "StageThree: " + action.currentAction,
                    "StageThree: " + action.currentAction
            );
        }
    }

    private class StageThree3Processor implements StageProcessor<TestThree> {

        @Override
        public StageWrapResult processStage(StageWrappedAction action) {
            return new StageWrapResult(
                    "StageThree3: " + action.currentAction,
                    "StageThree3: " + action.currentAction,
                    null
            );
        }
    }

    private class StageFourProcessor implements StageProcessor<TestThree> {

        @Override
        public StageWrapResult processStage(StageWrappedAction action) {
            return new StageWrapResult(
                    null,
                    "StageFour: " + action.currentAction,
                    "StageFour: " + action.currentAction
            );
        }
    }
}
