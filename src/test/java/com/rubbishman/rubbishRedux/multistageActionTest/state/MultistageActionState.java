package com.rubbishman.rubbishRedux.multistageActionTest.state;

import java.util.ArrayList;

public class MultistageActionState {
    public ArrayList<Counter> counterObjects = new ArrayList<>();
    public ArrayList<CounterStopper> counterStopperObjects = new ArrayList<>();

    public MultistageActionState clone() {
        MultistageActionState cloned = new MultistageActionState();

        cloned.counterObjects.addAll(this.counterObjects);
        cloned.counterStopperObjects.addAll(this.counterStopperObjects);

        return cloned;
    }
}
