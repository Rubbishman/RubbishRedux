package com.rubbishman.rubbishRedux.experimental.actionTrack;

import com.google.common.collect.ImmutableList;
import com.rubbishman.rubbishRedux.external.operational.action.multistageAction.Stage.Stage;
import org.junit.Test;

import java.util.HashMap;

import static org.junit.Assert.assertTrue;

public class ActionTrackTest {
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

        ActionTrack actionTrack = new ActionTrack(stageStack);

        actionTrack.addAction(new TestTwo());
        actionTrack.addAction(new TestThree());
        actionTrack.addAction(new TestOne());
        actionTrack.addAction(new TestOne());
        actionTrack.addAction(new TestFour());

        assertTrue(actionTrack.actionQueue.poll().currentAction.getClass().equals(TestOne.class));
        assertTrue(actionTrack.actionQueue.poll().currentAction.getClass().equals(TestOne.class));
        assertTrue(actionTrack.actionQueue.poll().currentAction.getClass().equals(TestTwo.class));
        assertTrue(actionTrack.actionQueue.poll().currentAction.getClass().equals(TestThree.class));
        assertTrue(actionTrack.actionQueue.poll().currentAction.getClass().equals(TestFour.class));
    }

    private class TestOne {}

    private class TestTwo {}

    private class TestThree {}

    private class TestFour {}

    private class StageOneProcessor implements StageProcessor<TestOne> {

        @Override
        public StageWrappedAction<TestOne> processStage(StageWrappedAction<TestOne> action) {
            return null;
        }
    }

    private class StageTwoProcessor implements StageProcessor<TestOne> {

        @Override
        public StageWrappedAction<TestOne> processStage(StageWrappedAction<TestOne> action) {
            return null;
        }
    }

    private class StageTwo2Processor implements StageProcessor<TestTwo> {

        @Override
        public StageWrappedAction<TestTwo> processStage(StageWrappedAction<TestTwo> action) {
            return null;
        }
    }

    private class StageThreeProcessor implements StageProcessor<TestTwo> {

        @Override
        public StageWrappedAction<TestTwo> processStage(StageWrappedAction<TestTwo> action) {
            return null;
        }
    }

    private class StageThree3Processor implements StageProcessor<TestThree> {

        @Override
        public StageWrappedAction<TestThree> processStage(StageWrappedAction<TestThree> action) {
            return null;
        }
    }

    private class StageFourProcessor implements StageProcessor<TestThree> {

        @Override
        public StageWrappedAction<TestThree> processStage(StageWrappedAction<TestThree> action) {
            return null;
        }
    }
}
