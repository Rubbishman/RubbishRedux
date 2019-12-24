package com.rubbishman.rubbishRedux.internal.neuronia.actions;

import com.rubbishman.rubbishRedux.internal.neuronia.state.card.Movement;

public class CardThoughtMovement {
    public final Movement move;
    public final boolean pickup;

    public CardThoughtMovement(Movement move, boolean pickup) {
        this.move = move;
        this.pickup = pickup;
    }
}
