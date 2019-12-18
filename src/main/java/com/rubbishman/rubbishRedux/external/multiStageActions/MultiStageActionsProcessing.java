package com.rubbishman.rubbishRedux.external.multiStageActions;

import com.rubbishman.rubbishRedux.external.RubbishReducer;
import com.rubbishman.rubbishRedux.internal.dynamicObjectStore.store.ObjectStore;
import com.rubbishman.rubbishRedux.internal.multistageActions.action.MultistageActionResolver;
import com.rubbishman.rubbishRedux.internal.multistageActions.stage.MultistageComparator;
import com.rubbishman.rubbishRedux.internal.multistageActions.stage.StageAction;
import redux.api.Store;

import java.util.HashMap;
import java.util.PriorityQueue;

public class MultiStageActionsProcessing {
    private Store<ObjectStore> state;
    private MultistageComparator comparator = new MultistageComparator();
    private PriorityQueue<StageAction> stageQueue = new PriorityQueue<>(comparator);
    private HashMap<Class, MultistageActionResolver> multistageProcessor = new HashMap<>();

    public MultiStageActionsProcessing(Store<ObjectStore> state, RubbishReducer reducer) {
        this.state = state;
        reducer.setMultiStageActions(this);
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

    public void addMultistageAction(Object action) {
        MultistageActionResolver msAction = multistageProcessor.get(action.getClass());
        stageQueue.add(new StageAction(msAction.getStage(), action));
    }

    public void addMultistageProcessor(Class clazz, MultistageActionResolver msAction) {
        multistageProcessor.put(clazz, msAction);
    }
}
