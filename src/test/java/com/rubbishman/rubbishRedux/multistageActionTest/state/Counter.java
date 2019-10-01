package com.rubbishman.rubbishRedux.multistageActionTest.state;

public class Counter {
    public final int id;
    public final int count;

    public final int incrementDiceNum = 1;
    public final int incrementDiceSize = 6;

    public Counter(int id, int count) {
        this.id = id;
        this.count = count;
    }

    public String toString() {
        return "[Counter(" + id + "): " + count + " | " + incrementDiceNum + "d" + incrementDiceSize + "]";
    }
}
