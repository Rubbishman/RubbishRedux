package com.rubbishman.rubbishRedux.internal.multistageCreateObjectTest.action.mulitstageAction;

import com.rubbishman.rubbishRedux.external.operational.action.createObject.CreateObject;
import com.rubbishman.rubbishRedux.external.operational.store.ObjectStore;
import com.rubbishman.rubbishRedux.internal.multistageActionTest.stage.StageConstants;
import com.rubbishman.rubbishRedux.internal.multistageActionTest.state.Counter;
import com.rubbishman.rubbishRedux.external.operational.action.createObject.IMultistageCreateObject;
import com.rubbishman.rubbishRedux.external.operational.action.multistageAction.MultistageActionResolver;
import com.rubbishman.rubbishRedux.external.operational.action.multistageAction.Stage.Stage;
import com.rubbishman.rubbishRedux.internal.multistageCreateObjectTest.action.CreateCounter;

import java.util.Random;

public class CounterCreateResolution implements MultistageActionResolver<IMultistageCreateObject<CreateCounter>> {
    private final Random rand;

    public CounterCreateResolution(long seed) {
        rand = new Random(seed);
    }

    public Stage getStage() {
        return StageConstants.COUNTER_CREATE_RESOLUTION;
    }

    public Object provideAction(IMultistageCreateObject<CreateCounter> action, ObjectStore state, long nowTime) {
        int diceNum = (rand.nextInt(action.createObject.diceNumMax - action.createObject.diceNumMin)
                + 1
                + action.createObject.diceNumMin);
        int diceSize = (rand.nextInt(action.createObject.diceSizeMax - action.createObject.diceSizeMin)
                + 1
                + action.createObject.diceSizeMin);

        return new CreateObject(new Counter(0, diceNum, diceSize), action.callback);
    }
}
