package com.rubbishman.rubbishRedux.experimental.keyBinding.reducer;

import com.rubbishman.rubbishRedux.experimental.keyBinding.KeyState;
import com.rubbishman.rubbishRedux.experimental.keyBinding.data.ActionBindInstance;
import redux.api.Reducer;

public class KeyBindReducer implements Reducer<KeyState> {
    public KeyState reduce(KeyState state, Object action) {
        KeyState cloned = state.clone();

        if(action instanceof ActionBindInstance) {
            ActionBindInstance actionBind = (ActionBindInstance) action;

            switch(actionBind.actionBind.bindType) {
                case ACTION:
                    if(actionBind.pressed) {
                        System.out.println(actionBind.actionBind.action);
                    }
                    break;
                case HOLD_FOR_ON:
                    if(actionBind.pressed) {
                        cloned.setKeyState(actionBind.actionBind.action, true);
                    } else {
                        cloned.setKeyState(actionBind.actionBind.action, false);
                    }
                    break;
                case TOGGLE:
                    if(actionBind.pressed) {
                        cloned.setKeyState(actionBind.actionBind.action, !cloned.getKeyState(actionBind.actionBind.action));
                    }
                    break;
                case TURN_ON:
                    if(actionBind.pressed) {
                        cloned.setKeyState(actionBind.actionBind.action, true);
                    }
                    break;
                case TURN_OFF:
                    if(actionBind.pressed) {
                        cloned.setKeyState(actionBind.actionBind.action, false);
                    }
                    break;
            }
        }

        return cloned;
    }
}
