package com.rubbishman.rubbishRedux.statefullTimer.action;

public class IncrementTimer {
    public final long nowTime;
    public final int subject;

    public IncrementTimer(long nowTime, int subject) {
        this.nowTime = nowTime;
        this.subject = subject;
    }

    public String toString() {
        return "[IncrementTimer("+subject+") @ " + nowTime + "]";
    }
}
