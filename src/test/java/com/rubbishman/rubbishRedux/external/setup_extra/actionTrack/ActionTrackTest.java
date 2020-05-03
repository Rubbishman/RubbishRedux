package com.rubbishman.rubbishRedux.external.setup_extra.actionTrack;

import com.google.common.collect.ImmutableList;
import com.rubbishman.rubbishRedux.external.setup_extra.actionTrack.stage.*;
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

        try {
            Stage stageOne = options.createStage("One");
            Stage stageTwo = options.createStage("Two");
            Stage stageThree = options.createStage("Three");
            Stage stageFour = options.createStage("Four");

            options.setStageProcessor(TestOne.class,
                    ImmutableList.of(
                            new StageWrap(stageOne, new StageOneProcessor()),
                            new StageWrap(stageTwo, new StageTwoProcessor())
                    )
            );

            options.setStageProcessor(TestTwo.class,
                    ImmutableList.of(
                            new StageWrap(stageTwo, new StageTwo2Processor()),
                            new StageWrap(stageThree, new StageThreeProcessor())
                    )
            );

            options.setStageProcessor(TestThree.class,
                    ImmutableList.of(
                            new StageWrap(stageThree, new StageThree3Processor()),
                            new StageWrap(stageFour, new StageFourProcessor())
                    )
            );
        } catch (Exception e) {
            e.printStackTrace();
        }

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
        rubbish.addAction(new TestTwo());
        rubbish.addAction(new TestThree());
        rubbish.addAction(new TestOne());
        rubbish.addAction(new TestOne());
        rubbish.addAction(new TestNoStage());
        rubbish.addAction(new TestNoStage());

        rubbish.performActions();

        assertEquals("MOO String \"StageTwo: StageOne: class com.rubbishman.rubbishRedux.external.setup_extra.actionTrack.ActionTrackTest$TestOne\"" +
                "MOO String \"StageTwo: StageOne: class com.rubbishman.rubbishRedux.external.setup_extra.actionTrack.ActionTrackTest$TestOne\"" +
                "MOO String \"StageThree: StageTwo2: class com.rubbishman.rubbishRedux.external.setup_extra.actionTrack.ActionTrackTest$TestTwo\"" +
                "MOO String \"StageFour: StageThree3: class com.rubbishman.rubbishRedux.external.setup_extra.actionTrack.ActionTrackTest$TestThree\"" +
                "MOO TestNoStage {}" +
                "MOO TestNoStage {}",
                stringBuilder.toString().replaceAll(System.lineSeparator(), ""));
    }

    private class TestOne {
        @Override
        public String toString() {
            return this.getClass().toString();
        }
    }

    private class TestTwo {
        @Override
        public String toString() {
            return this.getClass().toString();
        }
    }

    private class TestThree {
        @Override
        public String toString() {
            return this.getClass().toString();
        }
    }

    private class TestNoStage {
        @Override
        public String toString() {
            return this.getClass().toString();
        }
    }

    private class StageOneProcessor implements StageProcessor {

        @Override
        public StageWrapResult processStage(ObjectStore state, StageWrappedAction action) {
            return new StageWrapResult(
                    "StageOne: " + action.currentAction,
                    "StageOne: " + action.currentAction,
                    null
            );
        }
    }

    private class StageTwoProcessor implements StageProcessor {

        @Override
        public StageWrapResult processStage(ObjectStore state, StageWrappedAction action) {
            return new StageWrapResult(
                    null,
                    "StageTwo: " + action.currentAction,
                    "StageTwo: " + action.currentAction
            );
        }
    }

    private class StageTwo2Processor implements StageProcessor {

        @Override
        public StageWrapResult processStage(ObjectStore state, StageWrappedAction action) {
            return new StageWrapResult(
                    "StageTwo2: " + action.currentAction,
                    "StageTwo2: " + action.currentAction,
                    null
            );
        }
    }

    private class StageThreeProcessor implements StageProcessor {

        @Override
        public StageWrapResult processStage(ObjectStore state, StageWrappedAction action) {
            return new StageWrapResult(
                    null,
                    "StageThree: " + action.currentAction,
                    "StageThree: " + action.currentAction
            );
        }
    }

    private class StageThree3Processor implements StageProcessor {

        @Override
        public StageWrapResult processStage(ObjectStore state, StageWrappedAction action) {
            return new StageWrapResult(
                    "StageThree3: " + action.currentAction,
                    "StageThree3: " + action.currentAction,
                    null
            );
        }
    }

    private class StageFourProcessor implements StageProcessor {

        @Override
        public StageWrapResult processStage(ObjectStore state, StageWrappedAction action) {
            return new StageWrapResult(
                    null,
                    "StageFour: " + action.currentAction,
                    "StageFour: " + action.currentAction
            );
        }
    }
}
