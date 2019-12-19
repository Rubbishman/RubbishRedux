package com.rubbishman.rubbishRedux.internal.multistageCreateObjectTest.action.mulitstageAction;

import com.rubbishman.rubbishRedux.internal.createObjectCallback.action.CreateObject;
import com.rubbishman.rubbishRedux.internal.dynamicObjectStore.store.ObjectStore;
import com.rubbishman.rubbishRedux.internal.multistageActionTest.stage.StageConstants;
import com.rubbishman.rubbishRedux.internal.multistageActionTest.state.Counter;
import com.rubbishman.rubbishRedux.internal.multistageActions.action.MultistageActionResolver;
import com.rubbishman.rubbishRedux.internal.multistageActions.stage.Stage;
import com.rubbishman.rubbishRedux.internal.multistageCreateObjectTest.action.CreateCounter;
import com.rubbishman.rubbishRedux.internal.multistageActions.action.MultistageCreateObject;

import java.util.Random;

public class CounterCreateResolution implements MultistageActionResolver<MultistageCreateObject<CreateCounter>> {
    private final Random rand;

    public CounterCreateResolution(long seed) {
        rand = new Random(seed);
    }

    public Stage getStage() {
        return StageConstants.COUNTER_CREATE_RESOLUTION;
    }

    public Object provideAction(MultistageCreateObject<CreateCounter> action, ObjectStore state, long nowTime) {
        int diceNum = (rand.nextInt(action.createObject.diceNumMax - action.createObject.diceNumMin)
                + 1
                + action.createObject.diceNumMin);
        int diceSize = (rand.nextInt(action.createObject.diceSizeMax - action.createObject.diceSizeMin)
                + 1
                + action.createObject.diceSizeMin);

        return new CreateObject(new Counter(0, diceNum, diceSize), action.callback);
    }
}
