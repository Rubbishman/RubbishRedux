package com.rubbishman.rubbishRedux.internal.multistageActionTest.action.multistageAction;

import com.rubbishman.rubbishRedux.external.operational.store.ObjectStore;
import com.rubbishman.rubbishRedux.internal.multistageActionTest.action.IncrementCounter;
import com.rubbishman.rubbishRedux.internal.multistageActionTest.action.IncrementCounterResolved;
import com.rubbishman.rubbishRedux.internal.multistageActionTest.stage.StageConstants;
import com.rubbishman.rubbishRedux.internal.multistageActionTest.state.Counter;
import com.rubbishman.rubbishRedux.external.operational.action.multistageAction.MultistageActionResolver;
import com.rubbishman.rubbishRedux.external.operational.action.multistageAction.Stage.Stage;

import java.util.Random;

public class IncrementCounterResolution implements MultistageActionResolver<IncrementCounter> {
    private final Random rand;

    public IncrementCounterResolution(long seed) {
        rand = new Random(seed);
    }

    public Stage getStage() {
        return StageConstants.INCREMENT_RESOLUTION;
    }

    public Object provideAction(IncrementCounter action, ObjectStore state, long nowTime) {
        Counter counter = (Counter)state.objectMap.get(action.targetCounterId).object;

        int increment = 0;
        for(int i = 0; i < counter.incrementDiceNum; i++) {
            increment += (rand.nextInt(counter.incrementDiceSize) + 1);
        }

        return new IncrementCounterResolved(action.targetCounterId, increment);
    }
}
