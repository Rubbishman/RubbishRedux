package com.rubbishman.rubbishRedux.multistageActions;

import com.rubbishman.rubbishRedux.multistageActions.action.MultistageAction;
import com.rubbishman.rubbishRedux.multistageActions.stage.MultistageComparator;
import com.rubbishman.rubbishRedux.multistageActions.stage.StageActions;
import redux.api.Reducer;
import redux.api.Store;

import java.util.PriorityQueue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class MultistageActions<S> {
    private Store<S> state;
    private ConcurrentLinkedQueue<Object> actionQueue;
    private MultistageComparator comparator;
    private PriorityQueue<StageActions> stageQueue;


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

    // TODO, multistage actions that happen after everything else (IE, on empty multistage object).
    // IE, something that responds to all of something that happened..?

    private void doMultistageActions(Long nowTime) {
        StageActions actions = stageQueue.poll();
        while(actions != null) {
            MultistageAction action = actions.poll();
            while(action != null) {
                state.dispatch(action.provideAction(state.getState(), nowTime));
                action = actions.poll();
            }
            actions = stageQueue.poll();
        }
    }
}
