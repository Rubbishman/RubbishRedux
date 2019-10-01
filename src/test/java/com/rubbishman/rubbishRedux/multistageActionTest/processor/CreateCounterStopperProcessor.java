package com.rubbishman.rubbishRedux.multistageActionTest.processor;

import com.rubbishman.rubbishRedux.createObjectCallback.action.CreateObject;
import com.rubbishman.rubbishRedux.createObjectCallback.interfaces.ICreateObjectProcessor;
import com.rubbishman.rubbishRedux.multistageActionTest.state.Counter;
import com.rubbishman.rubbishRedux.multistageActionTest.state.CounterStopper;
import com.rubbishman.rubbishRedux.multistageActionTest.state.MultistageActionState;

public class CreateCounterStopperProcessor implements ICreateObjectProcessor<MultistageActionState, CounterStopper>{

    private CounterStopper createdObject;

    public CounterStopper getPostCreateObject() {
        return createdObject;
    }


    public MultistageActionState run(MultistageActionState state, CreateObject action) {
        if(action.createObject instanceof CounterStopper) {
            createdObject = (CounterStopper)action.createObject;
            state.counterStopperObjects.add(createdObject);
        }

        return state;
    }
}