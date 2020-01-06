package com.rubbishman.rubbishRedux.external.setup_extra.createObject;

import com.rubbishman.rubbishRedux.external.setup_extra.createObject.reducer.CreateObjectReducer;
import com.rubbishman.rubbishRedux.external.operational.store.ObjectStore;
import redux.api.Reducer;
import redux.api.Store;

public class CreateObjectEnhancer implements Store.Enhancer<ObjectStore> {
    private CreateObjectReducer createObjectReducer;

    public CreateObjectEnhancer() {
        createObjectReducer = new CreateObjectReducer();
    }

    public Store.Creator<ObjectStore> enhance(Store.Creator<ObjectStore> next) {
        return new Store.Creator<ObjectStore>() {
            @Override
            public Store<ObjectStore> create(Reducer<ObjectStore> reducer, ObjectStore initialState) {
                createObjectReducer.setWrappedReducer(reducer);
                Store<ObjectStore> baseStore = next.create(createObjectReducer, initialState);

                return new Store<ObjectStore>() {
                    public Object dispatch(Object action) {
                        //Not sure if this is best...
                        Object result = baseStore.dispatch(action);
                        createObjectReducer.postDispatch();
                        return result;
                    }

                    public ObjectStore getState() {
                        return baseStore.getState();
                    }

                    public Subscription subscribe(Subscriber subscriber) {
                        return baseStore.subscribe(subscriber);
                    }

                    public void replaceReducer(Reducer<ObjectStore> reducer1) {
                        createObjectReducer.setWrappedReducer(reducer1);
                        baseStore.replaceReducer(createObjectReducer);
                    }
                };
            }
        };
    }
}