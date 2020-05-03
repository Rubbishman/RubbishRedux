package com.rubbishman.rubbishRedux.internal.multistageCreateObjectTest.action.mulitstageAction;

import com.rubbishman.rubbishRedux.external.setup_extra.actionTrack.stage.StageProcessor;
import com.rubbishman.rubbishRedux.external.setup_extra.actionTrack.stage.StageWrapResult;
import com.rubbishman.rubbishRedux.external.setup_extra.actionTrack.stage.StageWrappedAction;
import com.rubbishman.rubbishRedux.external.RubbishContainerTest;
import com.rubbishman.rubbishRedux.external.operational.action.createObject.CreateObject;
import com.rubbishman.rubbishRedux.external.operational.store.ObjectStore;
import com.rubbishman.rubbishRedux.internal.multistageActionTest.state.Counter;

import java.util.Random;

public class CounterCreateResolution implements StageProcessor {
    private final Random rand;

    public CounterCreateResolution(long seed) {
        rand = new Random(seed);
    }

    @Override
    public StageWrapResult processStage(ObjectStore state, StageWrappedAction action) {
        RubbishContainerTest.CounterCreateObject createCounter = (RubbishContainerTest.CounterCreateObject)action.originalAction;

        int diceNum = (rand.nextInt(createCounter.createObject.diceNumMax - createCounter.createObject.diceNumMin)
                + 1
                + createCounter.createObject.diceNumMin);
        int diceSize = (rand.nextInt(createCounter.createObject.diceSizeMax - createCounter.createObject.diceSizeMin)
                + 1
                + createCounter.createObject.diceSizeMin);

        Counter counter = new Counter(0, diceNum, diceSize);

        return new StageWrapResult(
                null,
                counter,
                new CreateObject(new Counter(0, diceNum, diceSize), createCounter.callback)
        );
    }
}
