package com.rubbishman.rubbishRedux.internal.neuronia.state.card.experience;

import com.rubbishman.rubbishRedux.internal.neuronia.state.brain.Concept;

public class ConceptPlacement {
    public final Concept type;
    public final int x;
    public final int y;
    public final int costTier;

    public ConceptPlacement(Concept type, int x, int y, int costTier) {
        this.type = type;
        this.x = x;
        this.y = y;
        this.costTier = costTier;
    }
}
