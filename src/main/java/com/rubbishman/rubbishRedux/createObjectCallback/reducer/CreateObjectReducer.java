package com.rubbishman.rubbishRedux.createObjectCallback.reducer;

import com.rubbishman.rubbishRedux.createObjectCallback.action.CreateObject;
import com.rubbishman.rubbishRedux.createObjectCallback.interfaces.ICreateObjectProcessor;
import redux.api.Reducer;

import java.util.HashMap;

public class CreateObjectReducer<S> implements Reducer<S> {
    private HashMap<Class, ICreateObjectProcessor<S, ?>> createObjectProcessor = new HashMap<>();

    public void addProcessor(ICreateObjectProcessor<S, ?> processor) {
        // Have to use the type against the method...?
        try {
            createObjectProcessor.put(processor.getClass().getMethod("getPostCreateObject").getReturnType(), processor);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    public S reduce(S state, CreateObject action) {
        ICreateObjectProcessor<S, ?> runnable = createObjectProcessor.get(action.createObject.getClass());
        if(runnable != null) {
            S newState = runnable.run(state, action);
            Object createdObject = runnable.getPostCreateObject();
            action.callback.postCreateState(createdObject);
            System.out.println(state);
            return newState;
        }

        return state;
    }

    public S reduce(S state, Object action) {
//        S cloned = state.clone(); //ZzZz?
        if(action instanceof CreateObject) {
            return reduce(state, (CreateObject)action);
        }

        return state;
    }
}
