package com.rubbishman.rubbishRedux.internal.multistageActions.action;

import com.rubbishman.rubbishRedux.internal.dynamicObjectStore.store.ObjectStore;
import com.rubbishman.rubbishRedux.internal.multistageActions.stage.Stage;

public interface MultistageActionResolver<A> {
    Stage getStage();
    Object provideAction(A action, ObjectStore state, long nowTime);
}
