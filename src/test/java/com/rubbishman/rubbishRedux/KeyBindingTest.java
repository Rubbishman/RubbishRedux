package com.rubbishman.rubbishRedux;

import com.rubbishman.rubbishRedux.keyBinding.BindType;
import com.rubbishman.rubbishRedux.keyBinding.KeyListener;
import com.rubbishman.rubbishRedux.keyBinding.KeyState;
import com.rubbishman.rubbishRedux.keyBinding.data.ActionBind;
import com.rubbishman.rubbishRedux.keyBinding.data.Bind;
import com.rubbishman.rubbishRedux.keyBinding.data.KeyBind;
import com.rubbishman.rubbishRedux.keyBinding.reducer.KeyBindReducer;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

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

    @Test
    public void testKeyBinding() {
        Store.Creator<KeyState> creator = new com.glung.redux.Store.Creator();

        Store<KeyState> store = creator.create(new KeyBindReducer(),new KeyState());
        assertFalse(store.getState().getKeyState(ACTION_UP));

        KeyListener keyListener = new  KeyListener(store, simpleKeyBindings());

        keyListener.getKeyEventHandler().handle(createSimpleKeyEvent(KeyCode.UP, true));

        assertTrue(store.getState().getKeyState(ACTION_UP));
        System.out.println(store.getState());

        keyListener.getKeyEventHandler().handle(createSimpleKeyEvent(KeyCode.UP, false));

        assertFalse(store.getState().getKeyState(ACTION_UP));
        System.out.println(store.getState());
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
