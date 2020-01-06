package com.rubbishman.rubbishRedux.internal;

import com.rubbishman.rubbishRedux.internal.dynamicObjectStore.GsonInstance;
import com.rubbishman.rubbishRedux.external.operational.store.IdObject;
import com.rubbishman.rubbishRedux.external.operational.store.Identifier;
import com.rubbishman.rubbishRedux.external.operational.store.ObjectStore;
import com.rubbishman.rubbishRedux.internal.multistageActionTest.state.Counter;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class DynamicStoreAdapterTest {
    @Test
    public void testDynamicStoreAdapter() {
        ObjectStore store = new ObjectStore();

        store = store.createObject(new Counter(
                        0,
                        1,
                        6
                )).state;

        assertEquals("{\"objectMap\":[{\"key\":\"{\\\"id\\\":1,\\\"clazz\\\":\\\"com.rubbishman.rubbishRedux.internal.multistageActionTest.state.Counter\\\"}\",\"value\":\"{\\\"id\\\":{\\\"id\\\":1,\\\"clazz\\\":\\\"com.rubbishman.rubbishRedux.internal.multistageActionTest.state.Counter\\\"},\\\"object\\\":{\\\"count\\\":0,\\\"incrementDiceNum\\\":1,\\\"incrementDiceSize\\\":6}}\"}],\"idGenerator\":\"{\\\"idSequence\\\":{\\\"class com.rubbishman.rubbishRedux.internal.multistageActionTest.state.Counter\\\":1}}\"}",
                GsonInstance.getInstance().toJson(store));
    }
}
