package com.rubbishman.rubbishRedux.external;

import com.rubbishman.rubbishRedux.internal.dynamicObjectStore.store.ObjectStore;
import com.rubbishman.rubbishRedux.internal.multistageActions.action.MultistageActionResolver;
import redux.api.Reducer;
import redux.api.enhancer.Middleware;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RubbishContainerOptions {
    protected List<Middleware<ObjectStore>> middlewareList = new ArrayList<>();
    protected Map<Class,MultistageActionResolver<ObjectStore>> multistageActionList = new HashMap<>();
    protected Reducer<ObjectStore> reducer;
    public RubbishContainerOptions addMiddleware(Middleware<ObjectStore> middleware) {
        middlewareList.add(middleware);

        return this;
    }

    public RubbishContainerOptions addMultistageAction(Class clazz, MultistageActionResolver multistageActionResolver) {
        multistageActionList.put(clazz, multistageActionResolver);

        return this;
    }

    public void setReducer(Reducer<ObjectStore> reducer) {
        this.reducer = reducer;
    }
}
