package com.rubbishman.rubbishRedux.dynamicObjectStore.adapter;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.rubbishman.rubbishRedux.dynamicObjectStore.store.Identifier;

import java.io.IOException;
import java.util.Iterator;

public class IdentifierAdapter extends TypeAdapter<Identifier> {
    public void write(JsonWriter out, Identifier value) throws IOException {
        Gson gson = new Gson();

        out.beginObject();
        out.name("id");
        out.value(value.id);
        out.name("clazz");
        out.value(value.clazz.getName());
        out.endObject();
    }

    public Identifier read(JsonReader in) throws IOException {
        return null;
    }
}