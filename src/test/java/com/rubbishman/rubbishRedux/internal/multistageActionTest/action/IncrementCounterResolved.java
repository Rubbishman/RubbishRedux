package com.rubbishman.rubbishRedux.internal.multistageActionTest.action;

import com.rubbishman.rubbishRedux.external.operational.store.Identifier;

public class IncrementCounterResolved {
    public final Identifier targetCounterId;
    public final int incrementAmount;

    public IncrementCounterResolved(Identifier targetCounterId, int incrementAmount) {
        this.targetCounterId = targetCounterId;
        this.incrementAmount = incrementAmount;
    }
}
