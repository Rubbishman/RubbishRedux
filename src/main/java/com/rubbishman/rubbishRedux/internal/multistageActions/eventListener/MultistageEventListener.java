package com.rubbishman.rubbishRedux.internal.multistageActions.eventListener;

import com.rubbishman.rubbishRedux.internal.dynamicObjectStore.store.ObjectStore;

public interface MultistageEventListener {
    public boolean listen(ObjectStore state, long nowTime);
}
