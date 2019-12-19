package com.rubbishman.rubbishRedux.external.operational.store;

import com.rubbishman.rubbishRedux.internal.dynamicObjectStore.store.IdGenerator;
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

    public ObjectStore setObject(Identifier id, Object obj) {
        return new ObjectStore(
                this.objectMap.assoc(id, new IdObject(id, obj)),
                this.idGenerator
        );
    }

    public <T> T getObject(Identifier id) {
        return (T)objectMap.get(id).object;
    }
}
