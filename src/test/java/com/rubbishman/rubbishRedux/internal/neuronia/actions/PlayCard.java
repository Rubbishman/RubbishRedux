package com.rubbishman.rubbishRedux.internal.neuronia.actions;

import com.rubbishman.rubbishRedux.external.operational.store.Identifier;

public class PlayCard {
    public final Identifier cardId;

    public PlayCard(Identifier cardId) {
        this.cardId = cardId;
    }
}
