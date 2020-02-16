package com.rubbishman.rubbishNeuronia.reducer.costValidator;

import com.google.common.collect.ImmutableList;
import com.rubbishman.rubbishNeuronia.state.cost.concept.ConceptTrace;

public class CostSolution {
    public final ImmutableList<ConceptTrace> costSolution;
    public final ImmutableList<ConceptTrace> remainingItems;

    //TODO, also store Concepts that go back to active memory (IE, skipped and stepped over).

    public CostSolution(ImmutableList<ConceptTrace> costSolution, ImmutableList<ConceptTrace> remainingItems) {
        this.costSolution = costSolution;
        this.remainingItems = remainingItems;
    }
}
