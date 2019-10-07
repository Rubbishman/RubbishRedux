package com.rubbishman.rubbishRedux.multistageActionTest.action;

public class IncrementCounter {
    public final int targetCounterId;

    public IncrementCounter(int targetCounterId) {
        this.targetCounterId = targetCounterId;
    }

    public String toString() {
        return "[Increment: (" + targetCounterId + ")]";
    }
}
