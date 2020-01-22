package com.rubbishman.rubbishRedux.internal.neuronia.state;

import com.google.common.collect.ImmutableList;
import com.rubbishman.rubbishRedux.external.operational.store.Identifier;
import com.rubbishman.rubbishRedux.internal.neuronia.state.brain.BrainConcept;
import org.organicdesign.fp.collections.PersistentHashMap;

import java.util.ArrayList;
import java.util.List;

public class Brain {
    private final PersistentHashMap<Integer, PersistentHashMap<Integer, List<Identifier>>> concepts;
    public final ImmutableList<Identifier> activeMemory;
    public final ImmutableList<Identifier> conceptReserve;

    public Brain() {
        concepts = PersistentHashMap.empty();
        activeMemory = ImmutableList.of();
        conceptReserve = ImmutableList.of();
    }

    public Brain(PersistentHashMap<Integer, PersistentHashMap<Integer, List<Identifier>>> concepts) {
        this.concepts = concepts;
        activeMemory = ImmutableList.of();
        conceptReserve = ImmutableList.of();
    }

    public Brain(PersistentHashMap<Integer, PersistentHashMap<Integer, List<Identifier>>> concepts,
                 ImmutableList<Identifier> activeMemory,
                 ImmutableList<Identifier> conceptReserve) {
        this.activeMemory = activeMemory;
        this.conceptReserve = conceptReserve;
        this.concepts = concepts;
    }

    public boolean hasConcept(int x, int y) {
        PersistentHashMap<Integer, List<Identifier>> xCoord;
        if(concepts.containsKey(x)) {
            xCoord = concepts.get(x);
            if(xCoord.containsKey(y)) {
                List<Identifier> xyCoord = xCoord.get(y);
                return !xyCoord.isEmpty();
            }
        }
        return false;
    }

    public BrainConcept pickupConcept(int x, int y) {
        PersistentHashMap<Integer, List<Identifier>> xCoord;
        if(concepts.containsKey(x)) {
            xCoord = concepts.get(x);
            if(xCoord.containsKey(y)) {
                List<Identifier> xyCoord = xCoord.get(y);
                if(!xyCoord.isEmpty()) {
                    Identifier conceptId = xyCoord.get(xyCoord.size()-1);
                    Brain newBrain = new Brain(
                        concepts.assoc(x, xCoord.assoc(y, xyCoord.subList(0, xyCoord.size()-1))),
                        ImmutableList.<Identifier>builder().addAll(activeMemory).add(conceptId).build(),
                        conceptReserve
                    );
                    return new BrainConcept(newBrain, conceptId);
                }
            }
        }
        return new BrainConcept(this, null);
    }

    public Brain addConcept(Identifier concept, int x, int y) {
        PersistentHashMap<Integer, List<Identifier>> xCoord;

        if(concepts.containsKey(x)) {
            xCoord = concepts.get(x);
            if(xCoord.containsKey(y)) {
                List<Identifier> xyCoord = xCoord.get(y);
                xyCoord.add(concept);
                return new Brain(concepts.assoc(x, xCoord.assoc(y, xyCoord)));
            }
        } else {
            xCoord = PersistentHashMap.empty();
        }

        ArrayList<Identifier> cp = new ArrayList<Identifier>();
        cp.add(concept);

        return new Brain(concepts.assoc(x, xCoord.assoc(y, cp)));
    }

    public Brain endTurn() {
        return new Brain(
                concepts,
                ImmutableList.of(),
                ImmutableList.<Identifier>builder().addAll(conceptReserve).addAll(activeMemory).build()
        );
    }
}
