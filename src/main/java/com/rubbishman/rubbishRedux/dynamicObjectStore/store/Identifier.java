package com.rubbishman.rubbishRedux.dynamicObjectStore.store;

import java.util.Objects;

public class Identifier {
    public final long id;
    public final Class clazz;

    public Identifier(long id, Class clazz) {
        this.clazz = clazz;
        this.id = id;
    }

    public boolean equals(Object o) {
        if(o instanceof Identifier) {
            Identifier io = (Identifier)o;
            if(io.id == this.id && io.clazz == this.clazz) {
                return true;
            }
        }

        return false;
    }

    public int hashCode() {
        return Objects.hash(id, clazz);
    }
}
