package com.rubbishman.rubbishRedux.dynamicObjectStore;

import org.organicdesign.fp.collections.PersistentHashMap;

public class objectStore {
    PersistentHashMap<IdObject,Object> originalHashMap = PersistentHashMap.empty();
    IdGenerator idGenerator = new IdGenerator();
}
