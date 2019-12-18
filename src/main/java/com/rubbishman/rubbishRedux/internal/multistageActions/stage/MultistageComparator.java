package com.rubbishman.rubbishRedux.internal.multistageActions.stage;

import java.util.Comparator;

public class MultistageComparator implements Comparator<StageAction> {

    public int compare(StageAction o1, StageAction o2) {
        if (o1.stage.priority < o2.stage.priority) {
            return -1;
        } else if (o1.stage.priority > o2.stage.priority) {
            return 1;
        }

        return o1.stage.identifier.compareTo(o2.stage.identifier);
    }
}