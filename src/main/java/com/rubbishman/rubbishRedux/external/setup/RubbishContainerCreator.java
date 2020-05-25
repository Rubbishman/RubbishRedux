package com.rubbishman.rubbishRedux.external.setup;

import com.rubbishman.rubbishRedux.external.setup_extra.TickSystem;
import com.rubbishman.rubbishRedux.external.setup_extra.actionTrack.stage.StageStack;
import com.rubbishman.rubbishRedux.external.RubbishContainer;
import com.rubbishman.rubbishRedux.internal.RubbishReducer;
import com.rubbishman.rubbishRedux.external.setup_extra.createObject.CreateObjectEnhancer;
import com.rubbishman.rubbishRedux.external.operational.store.ObjectStore;
import com.rubbishman.rubbishRedux.external.setup_extra.statefullTimer.StatefullTimerProcessing;
import com.rubbishman.rubbishRedux.internal.middlewareEnhancer.MiddlewareEnhancer;
import redux.api.Store;
import redux.api.enhancer.Middleware;

public class RubbishContainerCreator {
    public static RubbishContainer getRubbishContainer(RubbishContainerOptions options) {
        Store.Creator<ObjectStore> creator;
        RubbishReducer rubbishReducer;
        Store<ObjectStore> store;
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

        if(options.reducer == null) {
            throw new NullPointerException("The reducer can not be null");
        }

        rubbishReducer = new RubbishReducer(options.reducer);

        store = creator.create(rubbishReducer, new ObjectStore());

        for(TickSystem tickSystem : options.registeredTickSystems) {
            tickSystem.setStore(store);
        }

        StageStack stageStack = new StageStack(
                options.actionStageMap
        );

        return new RubbishContainer(
                stageStack,
                store,
                rubbishReducer,
                options.registeredTickSystems,
                options.timeKeeper,
                options.listeners
        );
    }
}
