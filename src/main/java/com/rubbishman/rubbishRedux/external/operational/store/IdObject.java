package com.rubbishman.rubbishRedux.external.operational.store;

public class IdObject {
    public final Identifier id;
    public final Object object;

    public IdObject(Identifier id, Object object) {
        this.id = id;
        this.object = object;
    }
}
