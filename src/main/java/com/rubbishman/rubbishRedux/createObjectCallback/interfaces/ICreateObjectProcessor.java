package com.rubbishman.rubbishRedux.createObjectCallback.interfaces;

import com.rubbishman.rubbishRedux.createObjectCallback.action.CreateObject;

public interface ICreateObjectProcessor<S, O> {
    O getPostCreateObject();
    S run(S state, CreateObject action);
}
