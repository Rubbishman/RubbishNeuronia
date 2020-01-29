package com.rubbishman.rubbishNeuronia.state;

import com.rubbishman.rubbishRedux.external.operational.store.Identifier;

public class ThoughtMovement {
    public final int x;
    public final int y;
    public final Identifier next;
    public final Identifier prev;

    public ThoughtMovement(int x, int y, Identifier next, Identifier prev) {
        this.x = x;
        this.y = y;
        this.next = next;
        this.prev = prev;
    }
}
