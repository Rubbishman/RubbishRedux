package com.rubbishman.rubbishRedux.multistageActionTest.processor;

import com.rubbishman.rubbishRedux.createObjectCallback.action.CreateObject;
import com.rubbishman.rubbishRedux.createObjectCallback.interfaces.ICreateObjectProcessor;
import com.rubbishman.rubbishRedux.multistageActionTest.state.Counter;
import com.rubbishman.rubbishRedux.multistageActionTest.state.MultistageActionState;

public class CreateCounterProcessor implements ICreateObjectProcessor<MultistageActionState, Counter> {
    private Counter createdObject;

    public Counter getPostCreateObject() {
        return createdObject;
    }

    public MultistageActionState run(MultistageActionState state, CreateObject action) {
        if(action.createObject instanceof Counter) {
            createdObject = new Counter(state.counterObjects.size(), ((Counter)action.createObject).count);
            state.counterObjects.add(createdObject);
        }

        return state;
    }
}
