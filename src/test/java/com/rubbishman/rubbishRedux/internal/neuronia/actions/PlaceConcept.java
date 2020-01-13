package com.rubbishman.rubbishRedux.internal.neuronia.actions;

import com.rubbishman.rubbishRedux.external.operational.store.Identifier;
import com.rubbishman.rubbishRedux.internal.neuronia.state.concept.ConceptTypes;

public class PlaceConcept {
    public final Identifier brainId;
    public final ConceptTypes type;
    public final int x;
    public final int y;

    public PlaceConcept(Identifier brainId, ConceptTypes type, int x, int y) {
        this.brainId = brainId;
        this.type = type;
        this.x = x;
        this.y = y;
    }
}
