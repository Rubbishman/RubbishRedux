package com.rubbishman.rubbishRedux.multistageCreateObjectTest.action.mulitstageAction;

import com.rubbishman.rubbishRedux.createObjectCallback.action.CreateObject;
import com.rubbishman.rubbishRedux.dynamicObjectStore.store.ObjectStore;
import com.rubbishman.rubbishRedux.multistageActionTest.stage.StageConstants;
import com.rubbishman.rubbishRedux.multistageActionTest.state.Counter;
import com.rubbishman.rubbishRedux.multistageActions.action.MultistageActionResolver;
import com.rubbishman.rubbishRedux.multistageActions.stage.Stage;
import com.rubbishman.rubbishRedux.multistageCreateObjectTest.action.CreateCounter;
import com.rubbishman.rubbishRedux.multistageActions.action.MultistageCreateObject;

import java.util.Random;

public class CounterCreateResolution implements MultistageActionResolver<MultistageCreateObject<CreateCounter>> {
    public final Random rand;

    public CounterCreateResolution(long seed) {
        rand = new Random(seed);
    }

    public Stage getStage() {
        return StageConstants.COUNTER_CREATE_RESOLUTION;
    }

    public Object provideAction(MultistageCreateObject<CreateCounter> action, ObjectStore state, long nowTime) {
        int diceNum = (int)(rand.nextInt(action.createObject.diceNumMax - action.createObject.diceNumMin)
                + 1
                + action.createObject.diceNumMin);
        int diceSize = (int)(rand.nextInt(action.createObject.diceSizeMax - action.createObject.diceSizeMin)
                + 1
                + action.createObject.diceSizeMin);

        return new CreateObject(new Counter(0, diceNum, diceSize), action.callback);
    }
}
