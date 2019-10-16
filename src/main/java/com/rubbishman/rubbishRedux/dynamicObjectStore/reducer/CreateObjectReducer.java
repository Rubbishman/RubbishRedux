package com.rubbishman.rubbishRedux.dynamicObjectStore.reducer;

import com.rubbishman.rubbishRedux.dynamicObjectStore.IdGenerator;
import com.rubbishman.rubbishRedux.dynamicObjectStore.IdObject;
import com.rubbishman.rubbishRedux.dynamicObjectStore.Identifier;
import com.rubbishman.rubbishRedux.dynamicObjectStore.ObjectStore;
import redux.api.Reducer;

public class CreateObjectReducer implements Reducer<ObjectStore> {

    public ObjectStore reduce(ObjectStore state, Object action) {
        IdGenerator idGenerator = new IdGenerator(state.idGenerator.idSequence);

        Identifier identifier = idGenerator.nextId(action.getClass());
        IdObject idObject = new IdObject(identifier, action);

        ObjectStore cloned = new ObjectStore(
                state.objectMap.assoc(identifier, idObject),
                new IdGenerator(idGenerator.idSequence)
        );

        return cloned;
    }
}
