package com.rubbishman.rubbishRedux.keyBinding.reducer;

import com.rubbishman.rubbishRedux.keyBinding.BindType;
import com.rubbishman.rubbishRedux.keyBinding.KeyState;
import com.rubbishman.rubbishRedux.keyBinding.data.ActionBind;
import com.rubbishman.rubbishRedux.keyBinding.data.ActionBindInstance;
import redux.api.Reducer;

public class KeyBindReducer implements Reducer<KeyState> {
    public KeyState reduce(KeyState state, Object action) {
        KeyState cloned = state.clone();

        if(action instanceof ActionBindInstance) {
            ActionBindInstance actionBind = (ActionBindInstance) action;

            Boolean value = cloned.keyState.get(actionBind.actionBind.action);

            switch(actionBind.actionBind.bindType) {
                case ACTION:
                    if(actionBind.pressed) {
                        System.out.println(actionBind.actionBind.action);
                    }
                    break;
                case HOLD_FOR_ON:
                    if(actionBind.pressed) {
                        cloned.keyState.put(actionBind.actionBind.action, true);
                    } else {
                        cloned.keyState.put(actionBind.actionBind.action, false);
                    }
                    break;
                case TOGGLE:
                    if(actionBind.pressed) {
                        cloned.keyState.put(actionBind.actionBind.action, !value);
                    }
                    break;
                case TURN_ON:
                    if(actionBind.pressed) {
                        cloned.keyState.put(actionBind.actionBind.action, true);
                    }
                    break;
                case TURN_OFF:
                    if(actionBind.pressed) {
                        cloned.keyState.put(actionBind.actionBind.action, false);
                    }
                    break;
            }
        }

        return cloned;
    }
}
