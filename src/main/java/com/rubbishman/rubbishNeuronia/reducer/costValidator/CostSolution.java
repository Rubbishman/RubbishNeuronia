package com.rubbishman.rubbishNeuronia.reducer.costValidator;

import com.google.common.collect.ImmutableList;
import com.rubbishman.rubbishNeuronia.state.cost.concept.ConceptTrace;

public class CostSolution {
    public final ImmutableList<ConceptTrace> costSolution;
    public final ImmutableList<ConceptTrace> remainingItems;

    public CostSolution(ImmutableList<ConceptTrace> costSolution, ImmutableList<ConceptTrace> remainingItems) {
        this.costSolution = costSolution;
        this.remainingItems = remainingItems;
    }
}
