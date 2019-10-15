package com.rubbishman.rubbishRedux.dynamicObjectStore;

public class IdObject {
    public static long NO_ID = -1l;

    public static IdObject getNoId(Class clazz) {
        return new IdObject(clazz, NO_ID);
    }

    public final long id;
    public final Class clazz;

    public IdObject(Class clazz, long id) {
        this.clazz = clazz;
        this.id = id;
    }
}
