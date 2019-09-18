package com.rubbishman.rubbishRedux.keyBinding;

import com.rubbishman.rubbishRedux.keyBinding.data.*;
import com.rubbishman.rubbishRedux.keyBinding.helper.KeyBindingHelper;
import javafx.event.EventHandler;
import javafx.scene.input.*;
import redux.api.Store;

import java.util.ArrayList;
import java.util.HashMap;

public class KeyListener {
    private HashMap<KeyCode, BindListener> keyBindings = new HashMap<>();

    private KeyEventHandler keyPressedHandler;

    private Store<KeyState> store;

    public KeyListener(Store<KeyState> store, ArrayList<KeyBind> binds) {
        this.store = store;
        initializeKeyMap(binds);
    }

    private void initializeKeyMap(ArrayList<KeyBind> binds) {
        for(KeyBind keyBind: binds) {
            keyBindings.put(KeyCode.getKeyCode(keyBind.keyCode), new BindListener(keyBind.bind));
        }
    }

    public EventHandler<KeyEvent> getKeyEventHandler() {
        if(keyPressedHandler == null) {
            keyPressedHandler = new KeyEventHandler();
        }

        return keyPressedHandler;
    }

    private class KeyEventHandler implements EventHandler<KeyEvent> {
        public void handle(KeyEvent keyEvent) {
            BindListener bindListener = keyBindings.get(keyEvent.getCode());

            if(bindListener != null) {

                ActionBindInstance action;
                if(keyEvent.getEventType() == KeyEvent.KEY_PRESSED || keyEvent.getEventType() == KeyEvent.KEY_TYPED) {
                    action = KeyBindingHelper.resolvePressedAction(keyEvent, bindListener);
                } else {
                    action = KeyBindingHelper.resolveReleasedAction(keyEvent, bindListener);
                }

                store.dispatch(action);
            }
        }
    }
}
