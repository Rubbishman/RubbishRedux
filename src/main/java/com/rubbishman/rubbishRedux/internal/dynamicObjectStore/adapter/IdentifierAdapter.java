package com.rubbishman.rubbishRedux.internal.dynamicObjectStore.adapter;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.rubbishman.rubbishRedux.external.operational.store.Identifier;

import java.io.IOException;

public class IdentifierAdapter extends TypeAdapter<Identifier> {
    public void write(JsonWriter out, Identifier value) throws IOException {
        Gson gson = new Gson();

        out.beginObject();
        out.name("id");
        out.value(value != null ? value.id : null);
        out.name("clazz");
        out.value(value != null ? value.clazz.getName() : null);
        out.endObject();
    }

    public Identifier read(JsonReader in) throws IOException {
        return null;
    }
}