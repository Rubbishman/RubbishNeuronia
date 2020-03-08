package com.rubbishman.rubbishNeuronia.state.cost;

import com.google.common.collect.ImmutableList;
import com.rubbishman.rubbishNeuronia.reducer.costValidator.costTypes.CostType;

public class SetValidator extends ListValidator {
    public SetValidator(ImmutableList<CostType> requiredConcepts) {
        super(requiredConcepts);
    }
}
