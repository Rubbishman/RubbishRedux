package com.rubbishman.rubbishRedux.external.setup_extra.createObject.reducer;

import com.rubbishman.rubbishRedux.external.operational.action.createObject.CreateObject;
import com.rubbishman.rubbishRedux.internal.dynamicObjectStore.store.CreatedObjectStore;
import com.rubbishman.rubbishRedux.external.operational.store.ObjectStore;
import redux.api.Reducer;

public class CreateObjectReducer implements Reducer<ObjectStore> {
    private Reducer<ObjectStore> wrappedReducer;
    private Runnable postDispatchRunnable;

    public void setWrappedReducer(Reducer<ObjectStore> wrapperReducer) {
        this.wrappedReducer = wrapperReducer;
    }

    public void postDispatch() {
        if(postDispatchRunnable != null) {
            postDispatchRunnable.run();
            postDispatchRunnable = null;
        }
    }

    public ObjectStore reduceCreateObject(ObjectStore state, CreateObject action) {
        CreatedObjectStore createdObject = state.createObject(action.createObject);
        if(action.callback != null) {
            this.postDispatchRunnable = new Runnable() {
                @Override
                public void run() {
                    action.callback.postCreateState(createdObject.createdObject);
                }
            };
        }

        return createdObject.state;
    }

    public ObjectStore reduce(ObjectStore state, Object action) {
        if(action instanceof CreateObject) {
            return reduceCreateObject(state, (CreateObject)action);
        } else if(wrappedReducer != null) {
            return wrappedReducer.reduce(state, action);
        }

        return state;
    }
}
