package com.rubbishman.rubbishRedux.createObjectCallback.enhancer;

import com.rubbishman.rubbishRedux.createObjectCallback.interfaces.ICreateObjectProcessor;
import com.rubbishman.rubbishRedux.createObjectCallback.reducer.CreateObjectReducer;
import redux.api.Reducer;
import redux.api.Store;

public class CreateObjectEnhancer<S> implements Store.Enhancer<S> {
    private CreateObjectReducer<S> createObjectReducer;

    public CreateObjectEnhancer() {
        createObjectReducer = new CreateObjectReducer<>();
    }

    public void addProcessor(ICreateObjectProcessor<S, ?> processor) {
        createObjectReducer.addProcessor(processor);
    }

    public Store.Creator<S> enhance(Store.Creator<S> next) {
        return (reducer, initialState) -> {
            createObjectReducer.setWrappedReducer(reducer);
            Store<S> baseStore = next.create(createObjectReducer, initialState);

            return new Store<S>() {
                public Object dispatch(Object action) {
                    //Not sure if this is best...
                    Object result = baseStore.dispatch(action);
                    createObjectReducer.postDispatch();
                    return result;
                }

                public S getState() {
                    return baseStore.getState();
                }
                public Subscription subscribe(Subscriber subscriber) {
                    return baseStore.subscribe(subscriber);
                }
                public void replaceReducer(Reducer<S> reducer1) {
                    createObjectReducer.setWrappedReducer(reducer1);
                    baseStore.replaceReducer(createObjectReducer);
                }
            };
        };
    }
}