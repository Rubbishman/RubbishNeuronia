package com.rubbishman.rubbishNeuronia.action;

import com.rubbishman.rubbishNeuronia.state.brain.Concept;
import com.rubbishman.rubbishRedux.external.operational.store.Identifier;

public class PlaceConcept {
    public final Identifier brainId;
    public final Concept type;
    public final int x;
    public final int y;

    public PlaceConcept(Identifier brainId, Concept type, int x, int y) {
        this.brainId = brainId;
        this.type = type;
        this.x = x;
        this.y = y;
    }
}
