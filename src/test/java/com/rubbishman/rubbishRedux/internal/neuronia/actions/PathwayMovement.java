package com.rubbishman.rubbishRedux.internal.neuronia.actions;

import com.rubbishman.rubbishRedux.internal.neuronia.state.card.pathway.Movement;

public class PathwayMovement {
    public final Movement move;
    public final boolean pickup;

    public PathwayMovement(Movement move, boolean pickup) {
        this.move = move;
        this.pickup = pickup;
    }
}
