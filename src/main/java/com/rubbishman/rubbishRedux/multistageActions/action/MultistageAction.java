package com.rubbishman.rubbishRedux.multistageActions.action;

import com.rubbishman.rubbishRedux.multistageActions.stage.Stage;

public interface MultistageAction<S, A> {
    Stage getStage();
    Object provideAction(A action, S state, long nowTime);
}
