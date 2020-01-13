package com.rubbishman.rubbishRedux.internal.neuronia.state.concept;

import com.rubbishman.rubbishRedux.external.operational.store.Identifier;

public class ConceptLocation {
    public final Identifier conceptId;
    public final int x;
    public final int y;

    public ConceptLocation(Identifier conceptId, int x, int y) {
        this.conceptId = conceptId;
        this.x = x;
        this.y = y;
    }
}
