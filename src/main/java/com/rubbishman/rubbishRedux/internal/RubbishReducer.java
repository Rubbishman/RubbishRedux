package com.rubbishman.rubbishRedux.internal;

import com.rubbishman.rubbishRedux.external.RubbishContainer;
import com.rubbishman.rubbishRedux.external.operational.action.createObject.CreateObject;
import com.rubbishman.rubbishRedux.external.setup.IRubbishReducer;
import com.rubbishman.rubbishRedux.external.setup_extra.actionTrack.ActionTrack;
import com.rubbishman.rubbishRedux.external.setup_extra.actionTrack.IActionTrack;
import com.rubbishman.rubbishRedux.external.setup_extra.createObject.reducer.CreateObjectReducer;
import com.rubbishman.rubbishRedux.external.operational.store.ObjectStore;
import com.rubbishman.rubbishRedux.internal.statefullTimer.action.IncrementTimer;
import com.rubbishman.rubbishRedux.internal.statefullTimer.reducer.TimerReducer;
import redux.api.Reducer;

public class RubbishReducer implements Reducer<ObjectStore> {
    private TimerReducer timerReducer = new TimerReducer();
    private CreateObjectReducer createObjectReducer = new CreateObjectReducer();
    private IRubbishReducer wrappedReducer;
    protected RubbishContainer rubbishContainer;

    public RubbishReducer() {}

    public RubbishReducer(IRubbishReducer wrappedReducer) {
        if(wrappedReducer == null) {
            throw new RuntimeException();
        }
        this.wrappedReducer = wrappedReducer;
    }

    public void setRubbishContainer(RubbishContainer rubbishContainer) {
        this.rubbishContainer = rubbishContainer;
        wrappedReducer.setRubbishContainer(rubbishContainer);
    }

    public void setCurrentActionTrack(IActionTrack currentActionTrack) {
        wrappedReducer.setCurrentActionTrack(currentActionTrack);
        timerReducer.setCurrentActionTrack(currentActionTrack);
        createObjectReducer.setCurrentActionTrack(currentActionTrack);
    }

    public ObjectStore reduce(ObjectStore state, Object action) {
        if(action == redux.api.Store.INIT) {
            // Meh.
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
