package com.rubbishman.rubbishRedux.createObjectCallbackTest.state;

import com.rubbishman.rubbishRedux.CreateObjectCallbackTest;

import java.util.ArrayList;

public class CreateObjectState {
    public ArrayList<Object> actions = new ArrayList<>();
    public ArrayList<CreateObjectStateObject> stateObjects = new ArrayList<>();

    public CreateObjectState clone() {
        CreateObjectState clone = new CreateObjectState();

        clone.actions.addAll(this.actions);
        clone.stateObjects.addAll(this.stateObjects);

        return clone;
    }

    public String toString() {
        return "{" +
                "   actions: " + actions.toString() +
                "   stateObjects: " + stateObjects.toString() +
                "}";
    }
}
