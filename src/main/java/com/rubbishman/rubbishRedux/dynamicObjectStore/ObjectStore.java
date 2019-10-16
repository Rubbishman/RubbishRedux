package com.rubbishman.rubbishRedux.dynamicObjectStore;

import org.organicdesign.fp.collections.PersistentHashMap;

public class ObjectStore {
    public final PersistentHashMap<Identifier,IdObject> objectMap;
    public final IdGenerator idGenerator;

    public ObjectStore() {
        objectMap = PersistentHashMap.empty();
        idGenerator = new IdGenerator();
    }

    public ObjectStore(PersistentHashMap<Identifier,IdObject> objectMap, IdGenerator idGenerator) {
        this.objectMap = objectMap;
        this.idGenerator = idGenerator;
    }
}
