package com.rubbishman.rubbishRedux.statefullTimer.helper;

import com.rubbishman.rubbishRedux.statefullTimer.state.RepeatingTimer;

public class TimerHelper {
    public static int calculateNewRepeats(long nowTime, RepeatingTimer perInc) {
        long diff = nowTime - perInc.timer.startTime;
        return (int)Math.floor(diff / perInc.timer.period);
    }

    public static double calculatePercentToNextRepeat(long nowTime, RepeatingTimer perInc) {
        if(perInc.currentRepeats == perInc.repeats) {
            return 1;
        }
        long startCurRepeat = (perInc.currentRepeats * perInc.timer.period) + perInc.timer.startTime;
        long diff = nowTime - startCurRepeat;
        double percent = (double)diff / perInc.timer.period;
        percent = percent - (int)percent; //Cheeky, in case current repeats did not get updated.

        return Math.max(Math.min(1, percent), 0);
    }

    public static long nextPeriodTime(RepeatingTimer perInc) {
        return ((perInc.currentRepeats + 1) * perInc.timer.period) + perInc.timer.startTime;
    }

    public static boolean repeatsChanged(RepeatingTimer periodicIncrementer, long nowTime) {
        int newRepeats = TimerHelper.calculateNewRepeats(nowTime, periodicIncrementer);
        return periodicIncrementer.currentRepeats != newRepeats;
    }
}
