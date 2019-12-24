package com.rubbishman.rubbishRedux.internal.neuronia.actions;

import com.rubbishman.rubbishRedux.external.operational.store.Identifier;

public class EndTurn {
    public final Identifier initLocId;
    public final Identifier curLocId;

    public EndTurn(Identifier initLocId, Identifier curLocId) {
        this.initLocId = initLocId;
        this.curLocId = curLocId;
    }
}
