package com.rubbishman.rubbishNeuronia.state.cost.concept;

import com.rubbishman.rubbishNeuronia.state.brain.Concept;

public class ConceptTrace {
    public final Concept concept;
    public final boolean pickup;

    public ConceptTrace(Concept concept, boolean pickup) {
        this.concept = concept;
        this.pickup = pickup;
    }
}
