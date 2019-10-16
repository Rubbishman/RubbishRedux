package com.rubbishman.rubbishRedux.dynamicObjectStore;

public class Identifier {
    public static long NO_ID = -1l;

    public static Identifier getNoId(Class clazz) {
        return new Identifier(clazz, NO_ID);
    }

    public final long id;
    public final Class clazz;

    public Identifier(Class clazz, long id) {
        this.clazz = clazz;
        this.id = id;
    }
}
