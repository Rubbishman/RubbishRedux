package com.rubbishman.rubbishRedux.experimental.nestedHistoryScratcher;

import java.util.List;

public class ScratchHistory {
    public ScratchHistory parent;
    public ScratchHistory child;
    public List past;
    public List future;
    public Object present;

    public void nextUntilParent() {

    }

    public void previousUntilParent() {

    }

    public void previous() {
        if(child != null) {
            child.previous();
        }
    }

    public void next() {
        if(child != null) {
            child.next();
        }
    }
}
