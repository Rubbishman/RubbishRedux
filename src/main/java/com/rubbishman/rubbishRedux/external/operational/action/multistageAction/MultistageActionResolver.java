package com.rubbishman.rubbishRedux.external.operational.action.multistageAction;

import com.rubbishman.rubbishRedux.external.operational.store.ObjectStore;
import com.rubbishman.rubbishRedux.external.operational.action.multistageAction.Stage.Stage;

public interface MultistageActionResolver<A> {
    Stage getStage();
    Object provideAction(A action, ObjectStore state, long nowTime);
}
