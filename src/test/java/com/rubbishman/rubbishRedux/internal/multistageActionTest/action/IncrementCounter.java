package com.rubbishman.rubbishRedux.internal.multistageActionTest.action;

import com.rubbishman.rubbishRedux.internal.dynamicObjectStore.store.Identifier;
import com.rubbishman.rubbishRedux.internal.multistageActions.action.MultistageAction;

public class IncrementCounter extends MultistageAction {
    public final Identifier targetCounterId;

    public IncrementCounter(Identifier targetCounterId) {
        this.targetCounterId = targetCounterId;
    }
}
