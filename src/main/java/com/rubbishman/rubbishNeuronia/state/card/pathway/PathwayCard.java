package com.rubbishman.rubbishNeuronia.state.card.pathway;

import com.rubbishman.rubbishRedux.external.operational.store.Identifier;

public class PathwayCard {
    public final Identifier brainId;
    public final CardMovementComponent[] movements;

    public PathwayCard(Identifier brainId, CardMovementComponent[] movements) {
        this.brainId = brainId;
        this.movements = movements;
    }
}
