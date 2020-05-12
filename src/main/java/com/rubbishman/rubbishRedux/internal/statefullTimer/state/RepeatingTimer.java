package com.rubbishman.rubbishRedux.internal.statefullTimer.state;

public class RepeatingTimer {
    public final long startTime;
    public final long period;
    public final long repeats;
    public final int currentRepeats;

    public final Object action;

    public RepeatingTimer(long startTime, long period, long repeats, int currentRepeats, Object action) {
        this.startTime = startTime;
        this.period = period;
        this.repeats = repeats;
        this.currentRepeats = currentRepeats;
        this.action = action;
    }

    public RepeatingTimer clone() {
        RepeatingTimer cloned = new RepeatingTimer(
            this.startTime,
            this.period,
            this.repeats,
            this.currentRepeats,
            this.action
        );

        return cloned;
    }

    public RepeatingTimer changeRepeats(int newRepeats) {
        RepeatingTimer cloned = new RepeatingTimer(
                this.startTime,
                this.period,
                this.repeats,
                newRepeats,
                this.action
        );

        return cloned;
    }
}
