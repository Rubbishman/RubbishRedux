package com.rubbishman.rubbishRedux.multistageActionTest.action;

import com.rubbishman.rubbishRedux.dynamicObjectStore.store.Identifier;

public class IncrementCounter {
    public final Identifier targetCounterId;

    public IncrementCounter(Identifier targetCounterId) {
        this.targetCounterId = targetCounterId;
    }
}
