package com.rubbishman.rubbishRedux.multistageActions;

import com.rubbishman.rubbishRedux.multistageActions.action.MultistageAction;
import com.rubbishman.rubbishRedux.multistageActions.stage.MultistageComparator;
import com.rubbishman.rubbishRedux.multistageActions.stage.StageAction;
import redux.api.Reducer;
import redux.api.Store;

import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class MultistageActions<S> {
    private Store<S> state;
    private ConcurrentLinkedQueue<Object> actionQueue;
    private MultistageComparator comparator;
    private PriorityQueue<StageAction> stageQueue;
    private HashMap<Class, MultistageAction> multistageProcessor = new HashMap<>();

    public void addAction(Object action) {
        actionQueue.add(action);
    }

    public MultistageActions(Store.Creator<S> creator, Reducer<S> reducer, S initialState) {
        stageQueue = new PriorityQueue<>();
        initialize(creator, reducer, initialState);
    }

    public S getState() {
        return state.getState();
    }

    private void initialize(Store.Creator<S> creator, Reducer<S> reducer, S initialState) {
        state = creator.create(reducer, initialState);
        comparator = new MultistageComparator();
        stageQueue = new PriorityQueue<>(comparator);
        actionQueue = new ConcurrentLinkedQueue<>();
    }

    public void addMultistageProcessor(Class clazz, MultistageAction msAction) {
        multistageProcessor.put(clazz, msAction);
    }

    public void doActions() {
        Long nowTime = System.nanoTime();
        ConcurrentLinkedQueue<Object> internalQueue;
        synchronized (actionQueue) {
            internalQueue = actionQueue;
            actionQueue = new ConcurrentLinkedQueue<Object>();
        }
        Object action = internalQueue.poll();
        while(action != null) {
            state.dispatch(action);
            doMultistageActions(nowTime); // If an action has triggered multistage actions they resolve before the next action.
            action = internalQueue.poll();
        }
    }

    public void addMultistageAction(Object action) {
        MultistageAction msAction = multistageProcessor.get(action.getClass());
        stageQueue.add(new StageAction(msAction.getStage(), action));
    }

    private void doMultistageActions(Long nowTime) {
        StageAction stageAction = stageQueue.poll();
        while(stageAction != null) {
            MultistageAction msAction = multistageProcessor.get(stageAction.action.getClass());
            if(msAction != null) {
                state.dispatch(msAction.provideAction(stageAction.action, state.getState(), nowTime));
            }

            stageAction = stageQueue.poll();
        }
    }
}
