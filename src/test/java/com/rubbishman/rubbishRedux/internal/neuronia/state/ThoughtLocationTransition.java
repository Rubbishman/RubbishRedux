package com.rubbishman.rubbishRedux.internal.neuronia.state;

import com.rubbishman.rubbishRedux.external.operational.store.Identifier;

public class ThoughtLocationTransition {
    public final int x;
    public final int y;
    public final boolean pickup;
    public final Identifier prev;

    public ThoughtLocationTransition(int x, int y, boolean pickup, Identifier prev) {
        this.x = x;
        this.y = y;
        this.pickup = pickup;
        this.prev = prev;
    }
}