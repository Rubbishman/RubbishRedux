package com.rubbishman.rubbishRedux.keyBinding;

import java.util.HashMap;

public class KeyState {
    public HashMap<Object, Boolean> keyState = new HashMap<>();

    public KeyState clone() {
        KeyState cloned = new KeyState();

        cloned.keyState = (HashMap<Object, Boolean>)this.keyState.clone();

        return cloned;
    }
}
