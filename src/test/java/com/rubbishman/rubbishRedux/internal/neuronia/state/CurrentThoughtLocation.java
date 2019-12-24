package com.rubbishman.rubbishRedux.internal.neuronia.state;

import com.rubbishman.rubbishRedux.external.operational.store.Identifier;

public class CurrentThoughtLocation {
    public final int x;
    public final int y;
    public final Identifier thoughtLocationTransition;

    public CurrentThoughtLocation(int x, int y, Identifier thoughtLocationTransition) {
        this.x = x;
        this.y = y;
        this.thoughtLocationTransition = thoughtLocationTransition;
    }
}
