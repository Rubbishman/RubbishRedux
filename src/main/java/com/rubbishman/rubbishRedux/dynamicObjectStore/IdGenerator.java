package com.rubbishman.rubbishRedux.dynamicObjectStore;

import org.organicdesign.fp.collections.PersistentHashMap;

public class IdGenerator {
    private long FIRST_ID = 1l;
    public PersistentHashMap<Class, Long> idSequence;

    public IdGenerator() {
        idSequence = PersistentHashMap.empty();
    }

    public IdGenerator(PersistentHashMap<Class, Long> idSequence) {
        this.idSequence = idSequence;
    }

    public Identifier nextId(Class clazz) {
        Long id = idSequence.get(clazz);

        id = id == null ?
                FIRST_ID 
                : ++id;

        idSequence = idSequence.assoc(clazz, id);

        return new Identifier(clazz, id);
    }
}
