package com.rubbishman.rubbishRedux.multistageActionTest.action;

import com.rubbishman.rubbishRedux.dynamicObjectStore.store.Identifier;
import com.rubbishman.rubbishRedux.multistageActions.action.MultistageAction;

public class IncrementCounter extends MultistageAction {
    public final Identifier targetCounterId;

    public IncrementCounter(Identifier targetCounterId) {
        this.targetCounterId = targetCounterId;
    }
}
