package com.rubbishman.rubbishRedux.statefullTimer.helper;

import com.rubbishman.rubbishRedux.statefullTimer.logic.TimerLogic;
import com.rubbishman.rubbishRedux.statefullTimer.state.RepeatingTimer;
import com.rubbishman.rubbishRedux.statefullTimer.state.TimerState;
import redux.api.Store;

import java.util.Comparator;

public class TimerComparator implements Comparator<TimerLogic> {

    private Store<TimerState> store;

    public TimerComparator(Store<TimerState> store) {
        this.store = store;
    }

    public int compare(TimerLogic o1, TimerLogic o2) {
        TimerState state = store.getState();

        long nextPeriodO1 = TimerHelper.nextPeriodTime(o1.getPeriodicIncrementer(state));
        long nextPeriodO2 = TimerHelper.nextPeriodTime(o2.getPeriodicIncrementer(state));
        if(nextPeriodO1 < nextPeriodO2) {
            return -1;
        } else if(nextPeriodO1 > nextPeriodO2) {
            return 1;
        }

        return 0;
    }
}