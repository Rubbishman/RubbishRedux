package com.rubbishman.rubbishRedux.internal.neuronia.state.card.experience;

import com.rubbishman.rubbishRedux.external.operational.store.Identifier;
import com.rubbishman.rubbishRedux.internal.neuronia.state.concept.ConceptTypes;

public class ConceptPlacement {
    public final ConceptTypes type;
    public final int x;
    public final int y;
    public final int costTier;

    public ConceptPlacement(ConceptTypes type, int x, int y, int costTier) {
        this.type = type;
        this.x = x;
        this.y = y;
        this.costTier = costTier;
    }
}
