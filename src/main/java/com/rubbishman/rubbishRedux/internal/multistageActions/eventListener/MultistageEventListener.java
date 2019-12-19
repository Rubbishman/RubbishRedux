package com.rubbishman.rubbishRedux.internal.multistageActions.eventListener;

import com.rubbishman.rubbishRedux.external.operational.store.ObjectStore;

public interface MultistageEventListener {
    public boolean listen(ObjectStore state, long nowTime);
}
