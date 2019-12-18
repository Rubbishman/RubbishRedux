package com.rubbishman.rubbishRedux.external;

import com.rubbishman.rubbishRedux.dynamicObjectStore.store.ObjectStore;
import com.rubbishman.rubbishRedux.middlewareEnhancer.MiddlewareEnhancer;
import redux.api.Store;

public class RubbishEnhancer {
    public static Store.Creator<ObjectStore> addMiddlewareLayer(Store.Creator<ObjectStore> creator) {
        MiddlewareEnhancer<ObjectStore> middlewareEnhancer = new MiddlewareEnhancer<>();

        creator = middlewareEnhancer.enhance(creator);

        return creator;
    }
}
