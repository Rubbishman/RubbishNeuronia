package com.rubbishman.rubbishNeuronia.reducer.costValidator;

import com.google.common.collect.ImmutableList;
import com.rubbishman.rubbishNeuronia.state.cost.concept.ConceptTrace;

import java.util.Objects;

public class CostSolution {
    public final ImmutableList<ConceptTrace> costSolution;
    public final ImmutableList<ConceptTrace> remainingItems;
    public final ImmutableList<ConceptTrace> skippedItems;

    public CostSolution(ImmutableList<ConceptTrace> costSolution, ImmutableList<ConceptTrace> remainingItems, ImmutableList<ConceptTrace> skippedItems) {
        this.costSolution = costSolution;
        this.remainingItems = remainingItems;
        this.skippedItems = skippedItems;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CostSolution that = (CostSolution) o;
        return Objects.equals(costSolution, that.costSolution) &&
                Objects.equals(remainingItems, that.remainingItems) &&
                Objects.equals(skippedItems, that.skippedItems);
    }

    @Override
    public int hashCode() {
        return Objects.hash(costSolution, remainingItems, skippedItems);
    }
}
