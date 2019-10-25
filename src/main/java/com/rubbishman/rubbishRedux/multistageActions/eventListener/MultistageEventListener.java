package com.rubbishman.rubbishRedux.multistageActions.eventListener;

import com.rubbishman.rubbishRedux.dynamicObjectStore.store.ObjectStore;

public interface MultistageEventListener {
    public boolean listen(ObjectStore state, long nowTime);
}
