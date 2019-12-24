package com.rubbishman.rubbishRedux.experimental.nestedHistoryScratcher;

import com.rubbishman.rubbishRedux.external.operational.store.ObjectStore;

import java.util.*;

public class ScratchHistory {
    public LinkedList past;
    public Iterator future;

    public ObjectStore state;

    public void nextUntilParent() {
        while(future.hasNext()) {
            Object next = future.next();
            if(next instanceof ScratchHistory) {
                ((ScratchHistory)next).next();
            } else {
                //Do an actual action
            }
        }
    }

    public void previousUntilParent() {

    }

    public boolean previous() {
        Object previous = past.poll();
        if(previous == null) {
            return false;
        }
        if(previous instanceof ScratchHistory) {
            ((ScratchHistory)previous).previous();
        } else if(previous instanceof ScratchHistoryItem) {


        }

        return true;
    }

    public boolean next() {
        if(future.hasNext()) {
            Object next = future.next();
            if(next instanceof ScratchHistory) {
                ((ScratchHistory)next).next();
            } else {
                //Do an actual action
            }
            return true;
        }

        return false;
    }
}
