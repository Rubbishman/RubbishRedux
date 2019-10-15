package com.rubbishman.rubbishRedux.timeStampedLogger.action;

public class TimeStampedAction {
    public final long nowTime;
    public final Object action;

    public TimeStampedAction(long nowTime, Object action) {
        this.nowTime = nowTime;
        this.action = action;
    }
}
