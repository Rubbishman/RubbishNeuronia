package com.rubbishman.rubbishNeuronia.action;

import com.rubbishman.rubbishNeuronia.state.card.pathway.Movement;
import com.rubbishman.rubbishRedux.external.operational.store.Identifier;

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
