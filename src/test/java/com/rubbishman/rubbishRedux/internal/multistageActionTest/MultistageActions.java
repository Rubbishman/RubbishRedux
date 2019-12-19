package com.rubbishman.rubbishRedux.internal.multistageActionTest;

import com.rubbishman.rubbishRedux.internal.dynamicObjectStore.store.ObjectStore;
import com.rubbishman.rubbishRedux.external.RubbishReducer;
import com.rubbishman.rubbishRedux.external.multiStageActions.MultiStageActionsProcessing;
import com.rubbishman.rubbishRedux.internal.multistageActions.action.MultistageActionResolver;
import redux.api.Store;

import java.util.concurrent.ConcurrentLinkedQueue;

public class MultistageActions {
    private Store<ObjectStore> state;
    private ConcurrentLinkedQueue<Object> actionQueue = new ConcurrentLinkedQueue<>();
    private MultiStageActionsProcessing multiProcessing;

    public void addAction(Object action) {
        actionQueue.add(action);
    }

    public MultistageActions(Store.Creator<ObjectStore> creator, RubbishReducer reducer, ObjectStore initialState) {
        state = creator.create(reducer, initialState);
        multiProcessing = new MultiStageActionsProcessing(state, reducer);
    }

    public ObjectStore getState() {
        return state.getState();
    }

    public void addMultistageProcessor(Class clazz, MultistageActionResolver msAction) {
        multiProcessing.addMultistageProcessor(clazz, msAction);
    }

    public void doActions(Long nowTime) {
        ConcurrentLinkedQueue<Object> internalQueue;
        synchronized (actionQueue) {
            internalQueue = actionQueue;
            actionQueue = new ConcurrentLinkedQueue<>();
        }
        Object action = internalQueue.poll();
        while(action != null) {
            state.dispatch(action);
            multiProcessing.afterDispatch(nowTime); // If an action has triggered multistage actions they resolve before the next action.
            action = internalQueue.poll();
        }
    }
}
