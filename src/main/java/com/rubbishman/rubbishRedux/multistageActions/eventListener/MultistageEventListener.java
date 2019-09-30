package com.rubbishman.rubbishRedux.multistageActions.eventListener;

public interface MultistageEventListener<S> {
    public boolean listen(S state, long nowTime);
}
