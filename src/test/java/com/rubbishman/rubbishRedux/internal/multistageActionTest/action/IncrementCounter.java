package com.rubbishman.rubbishRedux.internal.multistageActionTest.action;

import com.rubbishman.rubbishRedux.external.operational.action.multistageAction.IMultistageAction;
import com.rubbishman.rubbishRedux.external.operational.store.Identifier;

public class IncrementCounter implements IMultistageAction {
    public final Identifier targetCounterId;

    public IncrementCounter(Identifier targetCounterId) {
        this.targetCounterId = targetCounterId;
    }
}
