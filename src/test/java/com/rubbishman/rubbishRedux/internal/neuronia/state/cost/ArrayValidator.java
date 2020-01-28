package com.rubbishman.rubbishRedux.internal.neuronia.state.cost;

import com.google.common.collect.ImmutableList;
import com.rubbishman.rubbishRedux.internal.neuronia.state.concept.Concept;

import java.util.ArrayList;

public class ArrayValidator {
    public final ImmutableList<Concept> requiredConcepts;

    public ArrayValidator(ImmutableList<Concept> requiredConcepts) {
        this.requiredConcepts = requiredConcepts;
    }

    public boolean validate(ImmutableList<Concept> availableConcepts) {
        for(int i = 0; i <= availableConcepts.size() - requiredConcepts.size(); i++) {
            for(int k = 0; k < requiredConcepts.size(); k++) {
                if(requiredConcepts.get(k) != availableConcepts.get(i + k)) {
                    break; // No match...
                } else if(k == requiredConcepts.size() - 1) {
                    return true;
                }
            }
        }

        return false;
    }
}
