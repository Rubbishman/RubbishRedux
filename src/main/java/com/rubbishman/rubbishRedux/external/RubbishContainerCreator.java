package com.rubbishman.rubbishRedux.external;

import com.rubbishman.rubbishRedux.internal.createObjectCallback.enhancer.CreateObjectEnhancer;
import com.rubbishman.rubbishRedux.internal.dynamicObjectStore.store.ObjectStore;
import com.rubbishman.rubbishRedux.external.multiStageActions.MultiStageActionsProcessing;
import com.rubbishman.rubbishRedux.external.statefullTimer.StatefullTimerProcessing;
import com.rubbishman.rubbishRedux.internal.middlewareEnhancer.MiddlewareEnhancer;
import redux.api.Store;
import redux.api.enhancer.Middleware;

import java.util.concurrent.ConcurrentLinkedQueue;

public class RubbishContainerCreator {
    public static RubbishContainer getRubbishContainer(RubbishContainerOptions options) {
        Store.Creator<ObjectStore> creator;
        StatefullTimerProcessing timer;
        RubbishReducer rubbishReducer;
        Store<ObjectStore> store;
        MultiStageActionsProcessing multistageActions = null;
        ConcurrentLinkedQueue<Object> actionQueue = new ConcurrentLinkedQueue<>();
        CreateObjectEnhancer coEnhancer = new CreateObjectEnhancer(); //TODO, make this so we don't have to enhance?

        creator = new com.glung.redux.Store.Creator();
        creator = coEnhancer.enhance(creator);

        if(!options.middlewareList.isEmpty()) {
            MiddlewareEnhancer<ObjectStore> middlewareEnhancer = new MiddlewareEnhancer<>();
            creator = middlewareEnhancer.enhance(creator);

            for(Middleware<ObjectStore> middleware: options.middlewareList) {
                middlewareEnhancer.addMiddleware(middleware);
            }
        }

        rubbishReducer = new RubbishReducer(options.reducer);

        store = creator.create(rubbishReducer, new ObjectStore());
        if(!options.multistageActionList.isEmpty()) {
            multistageActions = new MultiStageActionsProcessing(store, rubbishReducer);
            for(Class clazz: options.multistageActionList.keySet()) {
                multistageActions.addMultistageProcessor(clazz, options.multistageActionList.get(clazz));
            }
        }

        timer = new StatefullTimerProcessing(store);

        return new RubbishContainer(actionQueue, multistageActions, timer, store);
    }
}
