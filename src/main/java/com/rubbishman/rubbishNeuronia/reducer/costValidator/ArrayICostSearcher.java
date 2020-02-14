package com.rubbishman.rubbishNeuronia.reducer.costValidator;

import com.google.common.collect.ImmutableList;
import com.rubbishman.rubbishNeuronia.state.cost.concept.ConceptTrace;

import java.util.List;

public class ArrayICostSearcher implements ICostSearcher {
    public final ImmutableList<ConceptTrace> requiredConcepts;
    public final ImmutableList<ConceptTrace> availableConcepts;
    public final ImmutableList<CostSolution> possibleSolutions;

    public ArrayICostSearcher(ImmutableList<ConceptTrace> requiredConcepts, ImmutableList<ConceptTrace> availableConcepts, ImmutableList<CostSolution> possibleSolutions) {
        this.requiredConcepts = requiredConcepts;
        this.availableConcepts = availableConcepts;
        this.possibleSolutions = possibleSolutions;
    }

    @Override
    public ImmutableList<CostSolution> getSolutions() {
        return null;
    }
    public boolean isTerminated() {
        return false;
    }

    @Override
    public List<ICostSearcher> consumeTrace() {
        return null;
    }
}
