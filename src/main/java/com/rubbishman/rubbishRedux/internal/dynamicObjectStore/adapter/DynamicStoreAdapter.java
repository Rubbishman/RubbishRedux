package com.rubbishman.rubbishRedux.internal.dynamicObjectStore.adapter;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.rubbishman.rubbishRedux.external.operational.store.IdObject;
import com.rubbishman.rubbishRedux.internal.dynamicObjectStore.GsonInstance;
import com.rubbishman.rubbishRedux.external.operational.store.Identifier;
import com.rubbishman.rubbishRedux.external.operational.store.ObjectStore;
import org.organicdesign.fp.collections.PersistentHashMap;

import java.io.IOException;
import java.util.Iterator;

public class DynamicStoreAdapter extends TypeAdapter<ObjectStore> {
    public void write(JsonWriter out, ObjectStore value) throws IOException {
        Gson gson = GsonInstance.getInstance();

        out.beginObject();
        out.name("objectMap");
        out.beginArray();

        Iterator<Class> outIterator = value.getClassIterator();
        while(outIterator.hasNext()) {
            PersistentHashMap<Identifier, IdObject> theseObjects
                    = value.getObjectsByClass(outIterator.next());

            Iterator<Identifier> idIterator = theseObjects.keyIterator();
            while(idIterator.hasNext()) {
                Identifier key = idIterator.next();

                out.beginObject();
                out.name("key");
                out.value(gson.toJson(key));
                out.name("value");
                out.value(gson.toJson(theseObjects.get(key)));
                out.endObject();
            }
        }

        out.endArray();
        out.name("idGenerator");
        out.value(gson.toJson(value.idGenerator));
        out.endObject();
    }

    public ObjectStore read(JsonReader in) throws IOException {
        return null;
    }
}
