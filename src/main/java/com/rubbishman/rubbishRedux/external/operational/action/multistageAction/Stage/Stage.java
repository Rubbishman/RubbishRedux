package com.rubbishman.rubbishRedux.external.operational.action.multistageAction.Stage;

public class Stage implements Comparable {
    public final String identifier;
    public final long priority;

    public Stage(String identifier, long priority) {
        this.identifier = identifier;
        this.priority = priority;
    }

    @Override
    public int compareTo(Object o) {
        if(o instanceof Stage) {
            return Long.compare(this.priority, ((Stage)o).priority);
        }

        return 0;
    }
}
