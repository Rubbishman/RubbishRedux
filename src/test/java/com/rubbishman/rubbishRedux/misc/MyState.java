package com.rubbishman.rubbishRedux.misc;

import java.util.ArrayList;

public class MyState {
    public ArrayList<Object> actions = new ArrayList<>();

    public MyState clone() {
        MyState clone = new MyState();

        clone.actions.addAll(this.actions);

        return clone;
    }

    public String toString() {
        return actions.toString();
    }
}
