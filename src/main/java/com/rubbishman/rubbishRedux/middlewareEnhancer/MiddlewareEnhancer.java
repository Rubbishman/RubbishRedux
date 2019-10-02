package com.rubbishman.rubbishRedux.middlewareEnhancer;

import redux.api.Reducer;
import redux.api.Store;
import redux.api.enhancer.Middleware;

import java.util.ArrayList;
import java.util.Iterator;

public class MiddlewareEnhancer<S> implements Store.Enhancer<S> {
    private ArrayList<Middleware<S>> middleware = new ArrayList<>();

    public void addMiddleware(Middleware<S> middleware) {
        this.middleware.add(middleware);
    }

    private Object processMiddleware(Store<S> baseStore, Object action, Middleware<S> middleware, Iterator<Middleware<S>> iterator) {
        //Recursion
        if(iterator.hasNext()) {
            return middleware.dispatch(
                    baseStore,
                    action_recurse -> processMiddleware(baseStore, action_recurse, iterator.next(), iterator),
                    action
            );
        }
        // Base case
        return middleware.dispatch(baseStore, baseStore, action);
    }

    public Store.Creator<S> enhance(Store.Creator<S> next) {
        return (reducer, initialState) -> {
            Store<S> baseStore = next.create(reducer, initialState);

            return new Store<S>() {
                public Object dispatch(Object action) {
                    if(middleware.size() > 0) {
                        Iterator<Middleware<S>> iter = middleware.iterator();

                        return processMiddleware(baseStore, action, iter.next(), iter);
                    }
                    return baseStore.dispatch(action);
                }

                public S getState() {
                    return baseStore.getState();
                }
                public Subscription subscribe(Subscriber subscriber) {
                    return baseStore.subscribe(subscriber);
                }
                public void replaceReducer(Reducer<S> reducer1) {
                    baseStore.replaceReducer(reducer1);
                }
            };
        };
    }
}