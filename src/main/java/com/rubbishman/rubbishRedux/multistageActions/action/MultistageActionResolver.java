package com.rubbishman.rubbishRedux.multistageActions.action;

import com.rubbishman.rubbishRedux.dynamicObjectStore.store.ObjectStore;
import com.rubbishman.rubbishRedux.multistageActions.stage.Stage;

public interface MultistageActionResolver<A> {
    Stage getStage();
    Object provideAction(A action, ObjectStore state, long nowTime);
}
