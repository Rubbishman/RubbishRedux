package com.rubbishman.rubbishRedux.external.setup_extra;

import com.rubbishman.rubbishRedux.external.operational.action.createObject.CreateObject;
import com.rubbishman.rubbishRedux.external.operational.action.multistageAction.IMultistageAction;
import com.rubbishman.rubbishRedux.external.setup_extra.createObject.reducer.CreateObjectReducer;
import com.rubbishman.rubbishRedux.external.operational.store.ObjectStore;
import com.rubbishman.rubbishRedux.external.setup_extra.multiStageActions.MultiStageActionsProcessing;
import com.rubbishman.rubbishRedux.internal.statefullTimer.action.IncrementTimer;
import com.rubbishman.rubbishRedux.internal.statefullTimer.reducer.TimerReducer;
import redux.api.Reducer;

public class RubbishReducer implements Reducer<ObjectStore> {
    private TimerReducer timerReducer = new TimerReducer();
    private CreateObjectReducer createObjectReducer = new CreateObjectReducer();
    private Reducer<ObjectStore> wrappedReducer;
    protected MultiStageActionsProcessing multistageAction;

    public RubbishReducer() {}

    public RubbishReducer(Reducer<ObjectStore> wrappedReducer) {
        this.wrappedReducer = wrappedReducer;
    }

    public void setMultiStageActions(MultiStageActionsProcessing multistageAction) {
        this.multistageAction = multistageAction;
    }

//    public void postDispatch() {
//        createObjectReducer.postDispatch();
//    }

    public ObjectStore reduce(ObjectStore state, Object action) {
        if(action == redux.api.Store.INIT) {
            // Meh.
        } else if(action instanceof IMultistageAction) {
            multistageAction.addMultistageAction(action);
        } else if (action instanceof IncrementTimer) {
            return timerReducer.reduce(state, (IncrementTimer)action);
        } else if (action instanceof CreateObject) {
            return createObjectReducer.reduceCreateObject(state, (CreateObject)action);
        } else {
            return wrappedReducer.reduce(state, action);
        }

        return state;
    }
}