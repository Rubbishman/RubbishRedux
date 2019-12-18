package com.rubbishman.rubbishRedux.external;

import com.rubbishman.rubbishRedux.createObjectCallback.action.CreateObject;
import com.rubbishman.rubbishRedux.dynamicObjectStore.reducer.CreateObjectReducer;
import com.rubbishman.rubbishRedux.dynamicObjectStore.store.ObjectStore;
import com.rubbishman.rubbishRedux.multistageActions.MultistageActions;
import com.rubbishman.rubbishRedux.multistageActions.action.MultistageAction;
import com.rubbishman.rubbishRedux.multistageActions.action.MultistageCreateObject;
import com.rubbishman.rubbishRedux.statefullTimer.action.IncrementTimer;
import com.rubbishman.rubbishRedux.statefullTimer.reducer.TimerReducer;
import com.sun.javaws.exceptions.InvalidArgumentException;
import redux.api.Reducer;

public class RubbishReducer implements Reducer<ObjectStore> {
    private TimerReducer timerReducer = new TimerReducer();
    private CreateObjectReducer createObjectReducer = new CreateObjectReducer();
    private Reducer<ObjectStore> wrappedReducer;
    protected MultistageActions multistageAction;

    public RubbishReducer() {}

    public RubbishReducer(Reducer<ObjectStore> wrappedReducer) {
        this.wrappedReducer = wrappedReducer;
    }

    public void setMultiStageActions(MultistageActions multistageAction) {
        this.multistageAction = multistageAction;
    }

    public ObjectStore reduce(ObjectStore state, Object action) {
        if(action == redux.api.Store.INIT) {
            // Meh.
        } else if(action instanceof MultistageAction) {
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
