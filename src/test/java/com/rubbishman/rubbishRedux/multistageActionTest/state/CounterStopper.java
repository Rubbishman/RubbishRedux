package com.rubbishman.rubbishRedux.multistageActionTest.state;

public class CounterStopper {
    public final int targetCounterId;
    public final int stopNumber;

    public CounterStopper(int targetCounterId, int stopNumber) {
        this.targetCounterId = targetCounterId;
        this.stopNumber = stopNumber;
    }

    public String toString() {
        return "[StopCounter (" + targetCounterId + ") : " + stopNumber + "]";
    }
}
