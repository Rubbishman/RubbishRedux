package com.rubbishman.rubbishRedux.multistageActions.action;

public interface MultistageAction<S> {
    Object provideAction(S state, long nowTime);
}
