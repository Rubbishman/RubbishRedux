package com.rubbishman.rubbishRedux.internal.multistageActionTest.action.multistageAction;

import com.rubbishman.rubbishRedux.experimental.actionTrack.stage.StageProcessor;
import com.rubbishman.rubbishRedux.experimental.actionTrack.stage.StageWrapResult;
import com.rubbishman.rubbishRedux.experimental.actionTrack.stage.StageWrappedAction;
import com.rubbishman.rubbishRedux.external.operational.store.ObjectStore;
import com.rubbishman.rubbishRedux.internal.multistageActionTest.action.IncrementCounter;
import com.rubbishman.rubbishRedux.internal.multistageActionTest.action.IncrementCounterResolved;
import com.rubbishman.rubbishRedux.internal.multistageActionTest.stage.StageConstants;
import com.rubbishman.rubbishRedux.internal.multistageActionTest.state.Counter;
import com.rubbishman.rubbishRedux.external.operational.action.multistageAction.Stage.Stage;

import java.util.Random;

public class IncrementCounterResolution implements StageProcessor {
    private final Random rand;

    public IncrementCounterResolution(long seed) {
        rand = new Random(seed);
    }

    @Override
    public StageWrapResult processStage(ObjectStore state, StageWrappedAction action) {
        IncrementCounter incCounter = (IncrementCounter)action.originalAction;
        Counter counter = state.getObject(incCounter.targetCounterId);

        int increment = 0;
        for(int i = 0; i < counter.incrementDiceNum; i++) {
            increment += (rand.nextInt(counter.incrementDiceSize) + 1);
        }

        return new StageWrapResult(
            null,
            increment,
            new IncrementCounterResolved(incCounter.targetCounterId, increment)
        );
    }
}
