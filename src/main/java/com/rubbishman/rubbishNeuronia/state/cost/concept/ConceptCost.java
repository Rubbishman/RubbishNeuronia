package com.rubbishman.rubbishNeuronia.state.cost.concept;

import com.google.common.collect.ImmutableList;
import com.rubbishman.rubbishRedux.internal.neuronia.state.brain.Concept;

public class ConceptCost {
    public final Concept cost;
    public final int count;
    public final boolean pickup;

    public ConceptCost(Concept cost, int count, boolean pickup) {
        this.cost = cost;
        this.count = count;
        this.pickup = pickup;
    }

    public boolean evaluate(ImmutableList<ConceptTrace> availableConcepts) {
        if(availableConcepts.size() < count) {
            return false;
        }

        for(int i = 0; i < count; i++) {
            if(cost != availableConcepts.get(i).concept || pickup != availableConcepts.get(i).pickup) {
                return false;
            }
        }

        return true;
    }
}
