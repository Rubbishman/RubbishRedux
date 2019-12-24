package com.rubbishman.rubbishRedux.internal.neuronia.state.card;

public class CardMovementComponent {
    public final Movement movement;
    public final boolean pickup;
    public final int costTeir;

    public CardMovementComponent(Movement movement, boolean pickup, int costTeir) {
        this.movement = movement;
        this.pickup = pickup;
        this.costTeir = costTeir;
    }
}
