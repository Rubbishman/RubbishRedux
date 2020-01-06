package com.rubbishman.rubbishRedux.internal.dynamicObjectStore.store;

import com.rubbishman.rubbishRedux.external.operational.store.IdObject;
import com.rubbishman.rubbishRedux.external.operational.store.ObjectStore;

public class CreatedObjectStore {
    public final ObjectStore state;
    public final IdObject createdObject;

    public CreatedObjectStore(ObjectStore state, IdObject createdObject) {
        this.state = state;
        this.createdObject = createdObject;
    }
}
