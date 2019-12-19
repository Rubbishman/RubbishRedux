package com.rubbishman.rubbishRedux.internal.multistageActionTest.state;

public class Counter {
    public final int count;

    public final int incrementDiceNum;
    public final int incrementDiceSize;

    private Counter(int count) {
        this.count = count;
        incrementDiceNum = 1;
        incrementDiceSize = 6;
    }

    public Counter(int count, int incrementDiceNum, int incrementDiceSize) {
        this.count = count;
        this.incrementDiceNum = incrementDiceNum;
        this.incrementDiceSize = incrementDiceSize;
    }

    public Counter increment(int incrementAmount) {
        return new Counter(count + incrementAmount);
    }
}
