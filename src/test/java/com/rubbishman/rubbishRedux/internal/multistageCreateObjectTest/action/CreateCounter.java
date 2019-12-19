package com.rubbishman.rubbishRedux.internal.multistageCreateObjectTest.action;

public class CreateCounter {
    public final int diceNumMin;
    public final int diceNumMax;

    public final int diceSizeMin;
    public final int diceSizeMax;

    public CreateCounter(int diceNumMin, int diceNumMax, int diceSizeMin, int diceSizeMax) {
        this.diceNumMin = diceNumMin;
        this.diceNumMax = diceNumMax;
        this.diceSizeMin = diceSizeMin;
        this.diceSizeMax = diceSizeMax;
    }
}
