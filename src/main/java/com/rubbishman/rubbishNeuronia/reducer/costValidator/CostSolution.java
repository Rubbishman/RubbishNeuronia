package com.rubbishman.rubbishNeuronia.reducer.costValidator;

import com.google.common.collect.ImmutableList;
import com.rubbishman.rubbishNeuronia.state.cost.concept.ConceptTrace;

public class CostSolution {
    public final ImmutableList<ConceptTrace> costSolution;
    public final ImmutableList<ConceptTrace> remainingItems;
    public final ImmutableList<ConceptTrace> skippedItems;

    public CostSolution(ImmutableList<ConceptTrace> costSolution, ImmutableList<ConceptTrace> remainingItems, ImmutableList<ConceptTrace> skippedItems) {
        this.costSolution = costSolution;
        this.remainingItems = remainingItems;
        this.skippedItems = skippedItems;
    }
}
