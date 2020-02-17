package com.rubbishman.rubbishNeuronia.reducer.costValidator;

import com.google.common.collect.ImmutableList;
import com.rubbishman.rubbishNeuronia.state.cost.concept.ConceptTrace;

public class SolutionChecker {
    public final ImmutableList<ConceptTrace> requiredConcepts;
    public final ImmutableList<ConceptTrace> availableConcepts;
    public final ImmutableList<ConceptTrace> partialSolution;
    public final boolean startedConsuming;
    public final ImmutableList<ConceptTrace> skippedConcepts;

    private SolutionChecker(ImmutableList<ConceptTrace> requiredConcepts,
                            ImmutableList<ConceptTrace> availableConcepts,
                            ImmutableList<ConceptTrace> partialSolution,
                            boolean startedConsuming,
                            ImmutableList<ConceptTrace> skippedConcepts) {
        this.requiredConcepts = requiredConcepts;
        this.availableConcepts = availableConcepts;
        this.partialSolution = partialSolution;
        this.startedConsuming = startedConsuming;
        this.skippedConcepts = skippedConcepts;
    }

    public SolutionChecker(ImmutableList<ConceptTrace> requiredConcepts,
                           ImmutableList<ConceptTrace> availableConcepts) {
        this.requiredConcepts = requiredConcepts;
        this.availableConcepts = availableConcepts;
        this.partialSolution = ImmutableList.of();
        this.startedConsuming = false;
        this.skippedConcepts = ImmutableList.of();
    }

    public boolean isSolved() {
        return requiredConcepts.isEmpty() && !partialSolution.isEmpty();
    }

    public boolean unsolvable() {
        return requiredConcepts.size() > availableConcepts.size();
    }

    public boolean canSkipTrace() {
        return !startedConsuming && availableConcepts.size() > 1;
    }

    public boolean consumableConceptEqual() {
        return availableConcepts.get(0).concept == requiredConcepts.get(0).concept;
    }

    public boolean pickupMatch() {
        return consumableConceptEqual()
                && requiredConcepts.get(0).pickup
                && availableConcepts.get(0).pickup;
    }

    public boolean requirePickup() {
        return requiredConcepts.get(0).pickup;
    }

    public boolean requireStepOverAndAvailableStepOver() {
        return !requiredConcepts.get(0).pickup && !availableConcepts.get(0).pickup;
    }

    public boolean availableIsStepOver() {
        return !availableConcepts.get(0).pickup;
    }

    public CostSolution toCostSolution() {
        return new CostSolution(
                partialSolution,
                availableConcepts,
                skippedConcepts
        );
    }

    public SolutionChecker stepOverConceptTrace() {
        return consumeTrace(false);
    }

    public SolutionChecker pickupConceptTrace() {
        return consumeTrace(true);
    }

    private SolutionChecker consumeTrace(boolean pickup) {
        return new SolutionChecker(
                requiredConcepts.subList(1, requiredConcepts.size()),
                availableConcepts.subList(1, availableConcepts.size()),
                ImmutableList.<ConceptTrace>builder()
                        .addAll(partialSolution)
                        .add(availableConcepts.get(0))
                        .build(),
                true,
                pickup ?
                    skippedConcepts
                    : ImmutableList.<ConceptTrace>builder()
                        .addAll(skippedConcepts)
                        .add(availableConcepts.get(0))
                        .build()
        );
    }



    public SolutionChecker skipTrace() {
        return new SolutionChecker(
            requiredConcepts,
            availableConcepts.subList(1, availableConcepts.size()),
            partialSolution,
            startedConsuming,
            ImmutableList.<ConceptTrace>builder()
                    .addAll(skippedConcepts)
                    .add(availableConcepts.get(0))
                    .build());
    }
}
