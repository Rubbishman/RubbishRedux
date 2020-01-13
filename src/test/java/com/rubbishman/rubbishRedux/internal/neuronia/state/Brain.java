package com.rubbishman.rubbishRedux.internal.neuronia.state;

import com.rubbishman.rubbishRedux.external.operational.store.Identifier;
import org.organicdesign.fp.collections.PersistentHashMap;

import java.util.ArrayList;

public class Brain {
    private final PersistentHashMap<Integer, PersistentHashMap<Integer, ArrayList<Identifier>>> concepts;

    public Brain() {
        concepts = PersistentHashMap.empty();
    }

    public Brain(PersistentHashMap<Integer, PersistentHashMap<Integer, ArrayList<Identifier>>> concepts) {
        this.concepts = concepts;
    }

    public Brain addConcept(Identifier concept, int x, int y) {
        PersistentHashMap<Integer, ArrayList<Identifier>> xCoord;

        if(concepts.containsKey(x)) {
            xCoord = concepts.get(x);
            if(xCoord.containsKey(y)) {
                ArrayList<Identifier> xyCoord = xCoord.get(y);
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
}
