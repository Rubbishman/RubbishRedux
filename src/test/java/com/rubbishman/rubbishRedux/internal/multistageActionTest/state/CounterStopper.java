package com.rubbishman.rubbishRedux.internal.multistageActionTest.state;

public class CounterStopper {
    private final int targetCounterId;
    private final int stopNumber;

    public CounterStopper(int targetCounterId, int stopNumber) {
        this.targetCounterId = targetCounterId;
        this.stopNumber = stopNumber;
    }
}
