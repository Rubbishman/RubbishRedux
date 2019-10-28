package com.rubbishman.rubbishRedux.multistageActionTest.state;

public class Counter {
    public final int id;
    public final int count;

    public final int incrementDiceNum;
    public final int incrementDiceSize;

    public Counter(int id, int count) {
        this.id = id;
        this.count = count;
        incrementDiceNum = 1;
        incrementDiceSize = 6;
    }

    public Counter(int id, int count, int incrementDiceNum, int incrementDiceSize) {
        this.id = id;
        this.count = count;
        this.incrementDiceNum = incrementDiceNum;
        this.incrementDiceSize = incrementDiceSize;
    }

    public Counter setId(int id) {
        return new Counter(
                id,
                this.count,
                this.incrementDiceNum,
                this.incrementDiceSize
        );
    }

    public Counter increment(int incrementAmount) {
        return new Counter(id, count + incrementAmount);
    }
}
