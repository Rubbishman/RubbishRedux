package com.rubbishman.rubbishRedux.keyBinding.helper;

import com.rubbishman.rubbishRedux.keyBinding.data.ActionBind;
import com.rubbishman.rubbishRedux.keyBinding.data.ActionBindInstance;
import com.rubbishman.rubbishRedux.keyBinding.data.BindListener;
import javafx.scene.input.KeyEvent;

public class KeyBindingHelper {
    public static ActionBindInstance resolvePressedAction(KeyEvent keyEvent, BindListener bindListener) {
        if(bindListener.pressed) {
            return null;
        }
        ActionBind actionBind = resolveAction(keyEvent, bindListener);

        bindListener.pressed = true;

        return new ActionBindInstance(bindListener.pressed, actionBind);
    }

    public static ActionBindInstance resolveReleasedAction(KeyEvent keyEvent, BindListener bindListener) {
        ActionBind actionBind = resolveAction(keyEvent, bindListener);

        bindListener.pressed = false;

        return new ActionBindInstance(bindListener.pressed, actionBind);
    }

    private static ActionBind resolveAction(KeyEvent keyEvent, BindListener bindListener) {
        ActionBind actionBind = null;

        if(keyEvent.isShiftDown() && keyEvent.isControlDown() && bindListener.bind.bothKeyAction != null) {
            actionBind = bindListener.bind.bothKeyAction;
        } else if (keyEvent.isShiftDown() && bindListener.bind.shiftKeyAction != null) {
            actionBind = bindListener.bind.shiftKeyAction;
        } else if (keyEvent.isControlDown() && bindListener.bind.ctrlKeyAction != null) {
            actionBind = bindListener.bind.ctrlKeyAction;
        } else if (bindListener.bind.baseKeyAction != null) {
            actionBind = bindListener.bind.baseKeyAction;
        }

        return actionBind;
    }
}
