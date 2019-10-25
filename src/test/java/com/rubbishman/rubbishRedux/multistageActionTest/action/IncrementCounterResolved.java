package com.rubbishman.rubbishRedux.multistageActionTest.action;

import com.rubbishman.rubbishRedux.dynamicObjectStore.store.Identifier;

public class IncrementCounterResolved {
    public final Identifier targetCounterId;
    public final int incrementAmount;

    public IncrementCounterResolved(Identifier targetCounterId, int incrementAmount) {
        this.targetCounterId = targetCounterId;
        this.incrementAmount = incrementAmount;
    }
}
