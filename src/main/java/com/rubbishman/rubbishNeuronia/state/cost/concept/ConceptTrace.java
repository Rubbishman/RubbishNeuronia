package com.rubbishman.rubbishNeuronia.state.cost.concept;

import com.rubbishman.rubbishNeuronia.reducer.costValidator.costTypes.CostType;
import com.rubbishman.rubbishNeuronia.state.brain.Concept;

public class ConceptTrace implements CostType {
    public final Concept concept;
    public final boolean pickup;

    public ConceptTrace(Concept concept, boolean pickup) {
        this.concept = concept;
        this.pickup = pickup;
    }
}
