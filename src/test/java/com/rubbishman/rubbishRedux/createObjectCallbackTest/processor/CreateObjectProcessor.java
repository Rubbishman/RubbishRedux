package com.rubbishman.rubbishRedux.createObjectCallbackTest.processor;

import com.rubbishman.rubbishRedux.CreateObjectCallbackTest;
import com.rubbishman.rubbishRedux.createObjectCallback.action.CreateObject;
import com.rubbishman.rubbishRedux.createObjectCallback.interfaces.ICreateObjectProcessor;
import com.rubbishman.rubbishRedux.createObjectCallbackTest.state.CreateObjectState;
import com.rubbishman.rubbishRedux.createObjectCallbackTest.state.CreateObjectStateObject;

public class CreateObjectProcessor implements ICreateObjectProcessor<CreateObjectState, CreateObjectStateObject> {
    private CreateObjectStateObject createdObject;
    public CreateObjectStateObject getPostCreateObject() {
        return createdObject;
    }

    public CreateObjectState run(CreateObjectState state, CreateObject action) {
        if(action.createObject instanceof CreateObjectStateObject) {
            createdObject = ((CreateObjectStateObject)action.createObject).clone();
            createdObject.id = state.stateObjects.size();
            state.stateObjects.add(createdObject);
        }

        return state;
    }
}
