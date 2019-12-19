package com.rubbishman.rubbishRedux;

import com.rubbishman.rubbishRedux.internal.dynamicObjectStore.GsonInstance;
import com.rubbishman.rubbishRedux.internal.dynamicObjectStore.store.IdObject;
import com.rubbishman.rubbishRedux.internal.dynamicObjectStore.store.Identifier;
import com.rubbishman.rubbishRedux.internal.dynamicObjectStore.store.ObjectStore;
import com.rubbishman.rubbishRedux.internal.multistageActionTest.state.Counter;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class DynamicStoreAdapterTest {
    @Test
    public void testDynamicStoreAdapter() {
        ObjectStore store = new ObjectStore();

        Identifier counterId = new Identifier(1, Counter.class);

        store = store.setObject(counterId,
                new IdObject(
                        counterId,
                        new Counter(
                              0,
                              1,
                              6
                        )
                ));

        assertEquals("{\"objectMap\":[{\"key\":\"{\\\"id\\\":1,\\\"clazz\\\":\\\"com.rubbishman.rubbishRedux.internal.multistageActionTest.state.Counter\\\"}\",\"value\":\"{\\\"id\\\":{\\\"id\\\":1,\\\"clazz\\\":\\\"com.rubbishman.rubbishRedux.internal.multistageActionTest.state.Counter\\\"},\\\"object\\\":{\\\"id\\\":{\\\"id\\\":1,\\\"clazz\\\":\\\"com.rubbishman.rubbishRedux.internal.multistageActionTest.state.Counter\\\"},\\\"object\\\":{\\\"count\\\":0,\\\"incrementDiceNum\\\":1,\\\"incrementDiceSize\\\":6}}}\"}],\"idGenerator\":\"{\\\"idSequence\\\":{}}\"}", GsonInstance.getInstance().toJson(store));
    }
}
