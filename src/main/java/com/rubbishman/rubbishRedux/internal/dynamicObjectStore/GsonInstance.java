package com.rubbishman.rubbishRedux.internal.dynamicObjectStore;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.rubbishman.rubbishRedux.internal.dynamicObjectStore.adapter.DynamicStoreAdapter;
import com.rubbishman.rubbishRedux.internal.dynamicObjectStore.adapter.IdentifierAdapter;
import com.rubbishman.rubbishRedux.external.operational.store.Identifier;
import com.rubbishman.rubbishRedux.external.operational.store.ObjectStore;

public class GsonInstance {
    private static Gson gson;

    public static Gson getInstance() {
        if (gson == null) {
            GsonBuilder builder = new GsonBuilder();
            builder.registerTypeAdapter(ObjectStore.class, new DynamicStoreAdapter());
            builder.registerTypeAdapter(Identifier.class, new IdentifierAdapter());
//            builder.setPrettyPrinting();
            gson = builder.create();
        }

        return gson;
    }
}
