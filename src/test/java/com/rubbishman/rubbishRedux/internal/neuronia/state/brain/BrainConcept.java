package com.rubbishman.rubbishRedux.internal.neuronia.state.brain;

import com.rubbishman.rubbishRedux.external.operational.store.Identifier;
import com.rubbishman.rubbishRedux.internal.neuronia.state.Brain;

public class BrainConcept {
    public final Brain brain;
    public final Identifier conceptId;

    public BrainConcept(Brain brain, Identifier conceptId) {
        this.brain = brain;
        this.conceptId = conceptId;
    }
}
