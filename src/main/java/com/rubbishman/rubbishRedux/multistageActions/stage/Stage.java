package com.rubbishman.rubbishRedux.multistageActions.stage;

public class Stage {
    public final String identifier;
    public final long priority;

    public Stage(String identifier, long priority) {
        this.identifier = identifier;
        this.priority = priority;
    }
}
