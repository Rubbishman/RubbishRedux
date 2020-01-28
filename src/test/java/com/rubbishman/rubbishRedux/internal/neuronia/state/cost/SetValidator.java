package com.rubbishman.rubbishRedux.internal.neuronia.state.cost;

import com.google.common.collect.ImmutableList;
import com.rubbishman.rubbishRedux.internal.neuronia.state.concept.Concept;

import java.util.ArrayList;

public class SetValidator {
    public final ImmutableList<Concept> requiredConcepts;

    public SetValidator(ImmutableList<Concept> requiredConcepts) {
        this.requiredConcepts = requiredConcepts;
    }

    public boolean validate(ImmutableList<Concept> availableConcepts) {
        ArrayList<Concept> searchConcepts = new ArrayList<>(requiredConcepts.asList());

        for(Concept concept: availableConcepts) {
            searchConcepts.remove(concept);
        }

        return searchConcepts.size() == 0;
    }
}
