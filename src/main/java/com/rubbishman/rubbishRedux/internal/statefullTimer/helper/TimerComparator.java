package com.rubbishman.rubbishRedux.internal.statefullTimer.helper;

import com.rubbishman.rubbishRedux.internal.dynamicObjectStore.store.ObjectStore;
import com.rubbishman.rubbishRedux.internal.statefullTimer.logic.TimerLogic;
import redux.api.Store;

import java.util.Comparator;

public class TimerComparator implements Comparator<TimerLogic> {

    private Store<ObjectStore> store;

    public TimerComparator(Store<ObjectStore> store) {
        this.store = store;
    }

    public int compare(TimerLogic o1, TimerLogic o2) {
        ObjectStore state = store.getState();

        long nextPeriodO1 = TimerHelper.nextPeriodTime(o1.getRepeatingTimer(state));
        long nextPeriodO2 = TimerHelper.nextPeriodTime(o2.getRepeatingTimer(state));
        if(nextPeriodO1 < nextPeriodO2) {
            return -1;
        } else if(nextPeriodO1 > nextPeriodO2) {
            return 1;
        }

        return 0;
    }
}