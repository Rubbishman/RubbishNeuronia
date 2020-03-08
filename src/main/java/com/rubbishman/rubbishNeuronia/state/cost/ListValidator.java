package com.rubbishman.rubbishNeuronia.state.cost;

import com.google.common.collect.ImmutableList;
import com.rubbishman.rubbishNeuronia.reducer.costValidator.costTypes.CostType;

public abstract class ListValidator implements CostType {
    public final ImmutableList<CostType> requiredConcepts;

    public ListValidator(ImmutableList<CostType> requiredConcepts) {
        this.requiredConcepts = requiredConcepts;
    }
}
