package com.rubbishman.rubbishRedux.internal;

import com.rubbishman.rubbishRedux.experimental.keyBinding.BindType;
import com.rubbishman.rubbishRedux.experimental.keyBinding.KeyListener;
import com.rubbishman.rubbishRedux.experimental.keyBinding.KeyState;
import com.rubbishman.rubbishRedux.experimental.keyBinding.data.ActionBind;
import com.rubbishman.rubbishRedux.experimental.keyBinding.data.Bind;
import com.rubbishman.rubbishRedux.experimental.keyBinding.data.KeyBind;
import com.rubbishman.rubbishRedux.experimental.keyBinding.reducer.KeyBindReducer;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import org.junit.Before;
import org.junit.Test;
import redux.api.Store;
import java.util.ArrayList;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class KeyBindingTest {

    public static final String ACTION_UP = "up";
    public static final String ACTION_DOWN = "down";
    public static final String ACTION_LEFT = "left";
    public static final String ACTION_RIGHT = "right";

    Store<KeyState> store;
    KeyListener keyListener;

    @Before
    public void setup() {
        Store.Creator<KeyState> creator = new com.glung.redux.Store.Creator();
        store = creator.create(new KeyBindReducer(),new KeyState());
        keyListener = new  KeyListener(store, simpleKeyBindings());
    }


    @Test
    public void testKeyBindingHoldForOn() {
        assertFalse(store.getState().getKeyState(ACTION_UP));

        keyListener.getKeyEventHandler().handle(createSimpleKeyEvent(KeyCode.UP, true));

        assertTrue(store.getState().getKeyState(ACTION_UP));

        keyListener.getKeyEventHandler().handle(createSimpleKeyEvent(KeyCode.UP, false));

        assertFalse(store.getState().getKeyState(ACTION_UP));
    }

    @Test
    public void testKeyBindingTurnOnTurnOff() {
        assertFalse(store.getState().getKeyState(ACTION_DOWN));

        keyListener.getKeyEventHandler().handle(createSimpleKeyEvent(KeyCode.DOWN, true));
        assertTrue(store.getState().getKeyState(ACTION_DOWN));
        keyListener.getKeyEventHandler().handle(createSimpleKeyEvent(KeyCode.DOWN, false));
        assertTrue(store.getState().getKeyState(ACTION_DOWN));

        keyListener.getKeyEventHandler().handle(createSimpleKeyEventShift(KeyCode.DOWN, true));
        assertFalse(store.getState().getKeyState(ACTION_DOWN));
        keyListener.getKeyEventHandler().handle(createSimpleKeyEventShift(KeyCode.DOWN, false));
        assertFalse(store.getState().getKeyState(ACTION_DOWN));
    }

    @Test
    public void testKeyBindingToggle() {
        assertFalse(store.getState().getKeyState(ACTION_LEFT));

        keyListener.getKeyEventHandler().handle(createSimpleKeyEvent(KeyCode.LEFT, true));
        assertTrue(store.getState().getKeyState(ACTION_LEFT));
        keyListener.getKeyEventHandler().handle(createSimpleKeyEvent(KeyCode.LEFT, false));
        assertTrue(store.getState().getKeyState(ACTION_LEFT));

        keyListener.getKeyEventHandler().handle(createSimpleKeyEvent(KeyCode.LEFT, true));
        assertFalse(store.getState().getKeyState(ACTION_LEFT));
        keyListener.getKeyEventHandler().handle(createSimpleKeyEvent(KeyCode.LEFT, false));
        assertFalse(store.getState().getKeyState(ACTION_LEFT));
    }

    static KeyEvent createSimpleKeyEvent(KeyCode key, boolean pressed) {
        return new KeyEvent( pressed ? KeyEvent.KEY_PRESSED : KeyEvent.KEY_RELEASED, key.toString(), key.getName(), key, false, false, false,false);
    }

    static KeyEvent createSimpleKeyEventShift(KeyCode key, boolean pressed) {
        return new KeyEvent( pressed ? KeyEvent.KEY_PRESSED : KeyEvent.KEY_RELEASED, key.toString(), key.getName(), key, true, false, false,false);
    }

    static ArrayList<KeyBind> simpleKeyBindings() {
        ArrayList<KeyBind> binds = new ArrayList<>();

        KeyBind bind = new KeyBind(
                KeyCode.UP.getName(),
                new Bind(new ActionBind(ACTION_UP, BindType.HOLD_FOR_ON))
        );
        binds.add(bind);

        bind = new KeyBind(
                KeyCode.DOWN.getName(),
                new Bind(
                        new ActionBind(ACTION_DOWN, BindType.TURN_ON),
                        new ActionBind(ACTION_DOWN, BindType.TURN_OFF),
                        null,
                        null
                )
        );
        binds.add(bind);

        bind = new KeyBind(
                KeyCode.LEFT.getName(),
                new Bind(new ActionBind(ACTION_LEFT, BindType.TOGGLE))
        );
        binds.add(bind);

        bind = new KeyBind(
                KeyCode.RIGHT.getName(),
                new Bind(new ActionBind(ACTION_RIGHT, BindType.ACTION))
        );
        binds.add(bind);

        return binds;
    }
}
