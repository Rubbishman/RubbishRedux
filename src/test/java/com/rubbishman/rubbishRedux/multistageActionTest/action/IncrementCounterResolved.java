package com.rubbishman.rubbishRedux.multistageActionTest.action;

public class IncrementCounterResolved {
    public final int targetCounterId;
    public final int incrementAmount;

    public IncrementCounterResolved(int targetCounterId, int incrementAmount) {
        this.targetCounterId = targetCounterId;
        this.incrementAmount = incrementAmount;
    }
}
