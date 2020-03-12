package com.rubbishman.rubbishNeuronia.state.cost.concept;

import com.rubbishman.rubbishNeuronia.reducer.costValidator.costTypes.CostType;
import com.rubbishman.rubbishNeuronia.state.brain.Concept;

import java.util.Objects;

public class ConceptTrace implements CostType {
    public final Concept concept;
    public final boolean pickup;

    public ConceptTrace(Concept concept, boolean pickup) {
        this.concept = concept;
        this.pickup = pickup;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ConceptTrace that = (ConceptTrace) o;
        return pickup == that.pickup &&
                concept == that.concept;
    }

    @Override
    public int hashCode() {
        return Objects.hash(concept, pickup);
    }
}
