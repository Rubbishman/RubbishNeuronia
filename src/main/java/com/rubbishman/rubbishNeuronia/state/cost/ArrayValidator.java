package com.rubbishman.rubbishNeuronia.state.cost;

import com.google.common.collect.ImmutableList;
import com.rubbishman.rubbishNeuronia.reducer.costValidator.costTypes.CostType;

public class ArrayValidator extends ListValidator {
    public ArrayValidator(ImmutableList<CostType> requiredConcepts) {
        super(requiredConcepts);
    }
}
