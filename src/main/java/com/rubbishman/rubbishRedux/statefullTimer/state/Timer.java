package com.rubbishman.rubbishRedux.statefullTimer.state;

public class Timer {
    public long startTime;
    public long period;

    public Timer clone() {
        Timer cloned = new Timer();
        cloned.startTime = this.startTime;
        cloned.period = this.period;

        return cloned;
    }
}
