package com.rubbishman.rubbishRedux.external.setup_extra;

import com.rubbishman.rubbishRedux.external.setup_extra.actionTrack.ActionTrack;
import com.rubbishman.rubbishRedux.external.operational.store.ObjectStore;
import redux.api.Store;

public abstract class TickSystem {
    protected Store<ObjectStore> store;

    public void setStore(Store<ObjectStore> store) {
        this.store = store;
    }

    public abstract void beforeDispatchStarted(ActionTrack actionQueue, Long nowTime);
    public abstract void afterDispatchFinished();
}
