package com.rubbishman.rubbishRedux.statefullTimer.state;

public class RepeatingTimer {
    public final int id;
    public final long startTime;
    public final long period;
    public final int repeats;
    public final int currentRepeats;

    public final Object action;

    public RepeatingTimer(int id, long startTime, long period, int repeats, int currentRepeats, Object action) {
        this.id = id;
        this.startTime = startTime;
        this.period = period;
        this.repeats = repeats;
        this.currentRepeats = currentRepeats;
        this.action = action;
    }

    public RepeatingTimer clone() {
        RepeatingTimer cloned = new RepeatingTimer(
            this.id,
            this.startTime,
            this.period,
            this.repeats,
            this.currentRepeats,
            this.action
        );

        return cloned;
    }

    public RepeatingTimer assignId(int id) {
        RepeatingTimer cloned = new RepeatingTimer(
                id,
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
                this.id,
                this.startTime,
                this.period,
                this.repeats,
                newRepeats,
                this.action
        );

        return cloned;
    }

    public String toString() {
        return "RepeatingTimer {" +
                " id: " + id +
                " startTime: " + startTime +
                " period: " + period +
                " currentRepeats: " + currentRepeats +
                " repeats: " + repeats +
                " action: " + action.toString();
    }
}
