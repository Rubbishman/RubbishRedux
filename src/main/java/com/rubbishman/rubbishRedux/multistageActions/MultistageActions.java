package com.rubbishman.rubbishRedux.multistageActions;

import com.rubbishman.rubbishRedux.dynamicObjectStore.store.ObjectStore;
import com.rubbishman.rubbishRedux.external.RubbishReducer;
import com.rubbishman.rubbishRedux.multistageActions.action.MultistageActionResolver;
import com.rubbishman.rubbishRedux.multistageActions.stage.MultistageComparator;
import com.rubbishman.rubbishRedux.multistageActions.stage.StageAction;
import redux.api.Store;

import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class MultistageActions {
    private Store<ObjectStore> state;
    private ConcurrentLinkedQueue<Object> actionQueue = new ConcurrentLinkedQueue<>();
    private MultistageComparator comparator = new MultistageComparator();
    private PriorityQueue<StageAction> stageQueue = new PriorityQueue<>(comparator);
    private HashMap<Class, MultistageActionResolver> multistageProcessor = new HashMap<>();

    public void addAction(Object action) {
        actionQueue.add(action);
    }

    public MultistageActions(Store.Creator<ObjectStore> creator, RubbishReducer reducer, ObjectStore initialState) {
        reducer.setMultiStageActions(this);
        state = creator.create(reducer, initialState);
    }

    public MultistageActions(ConcurrentLinkedQueue<Object> actionQueue, Store<ObjectStore> state, RubbishReducer reducer) {
        this.actionQueue = actionQueue;
        reducer.setMultiStageActions(this);
        this.state = state;
    }

    public ObjectStore getState() {
        return state.getState();
    }

    public void addMultistageProcessor(Class clazz, MultistageActionResolver msAction) {
        multistageProcessor.put(clazz, msAction);
    }

    public void doActions(Long nowTime) {
        ConcurrentLinkedQueue<Object> internalQueue;
        synchronized (actionQueue) {
            internalQueue = actionQueue;
            actionQueue = new ConcurrentLinkedQueue<Object>();
        }
        Object action = internalQueue.poll();
        while(action != null) {
            state.dispatch(action);
            afterDispatch(nowTime); // If an action has triggered multistage actions they resolve before the next action.
            action = internalQueue.poll();
        }
    }

    public void addMultistageAction(Object action) {
        MultistageActionResolver msAction = multistageProcessor.get(action.getClass());
        stageQueue.add(new StageAction(msAction.getStage(), action));
    }

    public void afterDispatch(Long nowTime) {
        StageAction stageAction = stageQueue.poll();
        while(stageAction != null) {
            MultistageActionResolver msAction = multistageProcessor.get(stageAction.action.getClass());
            if(msAction != null) {
                state.dispatch(msAction.provideAction(stageAction.action, state.getState(), nowTime));
            }

            stageAction = stageQueue.poll();
        }
    }
}
