package com.rubbishman.rubbishRedux.experimental.keyBinding;

import java.util.HashMap;

public class KeyState {
    private HashMap<Object, Boolean> keyState = new HashMap<>();

    public Boolean getKeyState(Object key) {
        Boolean value = keyState.get(key);

        return value == null ? false : value;
    }

    public void setKeyState(Object key, Boolean value) {
        keyState.put(key, value);
    }

    public KeyState clone() {
        KeyState cloned = new KeyState();

        cloned.keyState = (HashMap<Object, Boolean>)this.keyState.clone();

        return cloned;
    }
}
