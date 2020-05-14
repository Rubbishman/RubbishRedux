package com.rubbishman.rubbishRedux.internal.timekeeper;

public class TimeKeeper {
    protected long nowTime;
    protected long elapsedTime;

    public void progressTime() {
        Long newNowTime = System.nanoTime();
        elapsedTime = newNowTime - nowTime;
        nowTime = newNowTime;
    }

    public long getNowTime() {
        return nowTime;
    }

    public long getElapsedTime() {
        return elapsedTime;
    }
}
