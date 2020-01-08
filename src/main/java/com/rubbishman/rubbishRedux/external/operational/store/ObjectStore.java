package com.rubbishman.rubbishRedux.external.operational.store;

import com.rubbishman.rubbishRedux.internal.dynamicObjectStore.store.CreatedObjectStore;
import com.rubbishman.rubbishRedux.internal.dynamicObjectStore.store.IdGenerator;
import org.organicdesign.fp.collections.PersistentHashMap;

import java.util.Iterator;

public class ObjectStore {
    private final PersistentHashMap<Class, PersistentHashMap<Identifier,IdObject>> objectMap;
    public final IdGenerator idGenerator;

    public ObjectStore() {
        objectMap = PersistentHashMap.empty();
        idGenerator = new IdGenerator();
    }

    public ObjectStore(PersistentHashMap<Class, PersistentHashMap<Identifier,IdObject>> objectMap, IdGenerator idGenerator) {
        this.objectMap = objectMap;
        this.idGenerator = idGenerator;
    }

    public PersistentHashMap<Identifier,IdObject> getObjectsByClass(Class clazz) {
        return objectMap.get(clazz);
    }

    public Iterator<Class> getClassIterator() {
        return objectMap.keyIterator();
    }

    public CreatedObjectStore createObject(Object newObject) {
        Identifier identifier = this.idGenerator.nextId(newObject.getClass());

        ObjectStore state = setObject(identifier, newObject);
        return new CreatedObjectStore(state, new IdObject(identifier, newObject));
    }

    public ObjectStore setObject(Identifier id, Object obj) {
        PersistentHashMap<Identifier,IdObject> theseObjects =
                objectMap.containsKey(id.clazz)
                        ? objectMap.get(id.clazz)
                            : PersistentHashMap.empty();

        theseObjects = theseObjects.assoc(id, new IdObject(id, obj));

        return new ObjectStore(
                objectMap.assoc(id.clazz, theseObjects),
                this.idGenerator
        );
    }

    public ObjectStore clearObjects(Class clazz) {
        return new ObjectStore(
                objectMap.assoc(clazz, PersistentHashMap.empty()),
                this.idGenerator
        );
    }

    public <T> T getObject(Identifier id) {
        return (T) objectMap.get(id.clazz).get(id).object;
    }
}
