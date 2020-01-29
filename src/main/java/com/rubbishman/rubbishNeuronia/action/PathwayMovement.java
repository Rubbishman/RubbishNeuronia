package com.rubbishman.rubbishNeuronia.action;

import com.rubbishman.rubbishRedux.external.operational.store.Identifier;
import com.rubbishman.rubbishRedux.internal.neuronia.state.card.pathway.Movement;

public class PathwayMovement {
    public final Identifier brainId;
    public final Movement move;
    public final boolean pickup;

    public PathwayMovement(Identifier brainId, Movement move, boolean pickup) {
        this.brainId = brainId;
        this.move = move;
        this.pickup = pickup;
    }
}
