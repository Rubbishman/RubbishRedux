package com.rubbishman.rubbishRedux;

import com.rubbishman.rubbishRedux.keyBinding.BindType;
import com.rubbishman.rubbishRedux.keyBinding.data.ActionBind;
import com.rubbishman.rubbishRedux.keyBinding.data.Bind;
import com.rubbishman.rubbishRedux.keyBinding.data.KeyBind;
import javafx.scene.input.KeyCode;
import org.junit.Test;

import java.util.ArrayList;

public class KeyBindingTest {

    public static final String UP = "up";
    public static final String DOWN = "down";
    public static final String LEFT = "left";
    public static final String RIGHT = "right";

    @Test
    public void testKeyBinding() {

    }

    static ArrayList<KeyBind> simpleKeyBindings() {
        ArrayList<KeyBind> binds = new ArrayList<>();

        KeyBind bind = new KeyBind(
                KeyCode.UP.getName(),
                new Bind(new ActionBind(UP, BindType.HOLD_FOR_ON))
        );
        binds.add(bind);

        bind = new KeyBind(
                KeyCode.DOWN.getName(),
                new Bind(new ActionBind(DOWN, BindType.HOLD_FOR_ON))
        );
        binds.add(bind);

        bind = new KeyBind(
                KeyCode.LEFT.getName(),
                new Bind(new ActionBind(LEFT, BindType.HOLD_FOR_ON))
        );
        binds.add(bind);

        bind = new KeyBind(
                KeyCode.RIGHT.getName(),
                new Bind(new ActionBind(RIGHT, BindType.HOLD_FOR_ON))
        );
        binds.add(bind);

        return binds;
    }
}
