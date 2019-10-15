package com.rubbishman.rubbishRedux.dynamicObjectStore;

import java.util.HashMap;

public class IdGenerator {
    private long FIRST_ID = 1l;
    private HashMap<Class, Long> idSequence = new HashMap<>();

    public IdObject nextId(Class clazz) {
        Long id = idSequence.get(clazz);

        id = id == null ?
                FIRST_ID 
                : id++;

        idSequence.put(clazz, id);

        return new IdObject(clazz, id);
    }
}
