package com.rubbishman.rubbishRedux.internal.multistageActionTest.action;

import com.rubbishman.rubbishRedux.external.operational.store.Identifier;

public class IncrementCounter {
    public final Identifier targetCounterId;

    public IncrementCounter(Identifier targetCounterId) {
        this.targetCounterId = targetCounterId;
    }
}
