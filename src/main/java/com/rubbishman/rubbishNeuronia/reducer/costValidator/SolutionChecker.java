package com.rubbishman.rubbishNeuronia.reducer.costValidator;

import com.google.common.collect.ImmutableList;
import com.rubbishman.rubbishNeuronia.reducer.costValidator.costTypes.CostType;
import com.rubbishman.rubbishNeuronia.reducer.costValidator.costTypes.NullCostType;
import com.rubbishman.rubbishNeuronia.state.cost.ArrayValidator;
import com.rubbishman.rubbishNeuronia.state.cost.ListValidator;
import com.rubbishman.rubbishNeuronia.state.cost.SetValidator;
import com.rubbishman.rubbishNeuronia.state.cost.concept.ConceptTrace;

import java.util.ArrayList;

public class SolutionChecker {
    public final CostType requiredConcepts;
    public final ImmutableList<ConceptTrace> availableConcepts;
    public final ImmutableList<ConceptTrace> partialSolution;
    public final boolean startedConsuming;
    public final ImmutableList<ConceptTrace> skippedConcepts;

    private SolutionChecker(CostType requiredConcepts,
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

    public SolutionChecker(CostType requiredConcepts,
                           ImmutableList<ConceptTrace> availableConcepts) {
        this.requiredConcepts = requiredConcepts;
        this.availableConcepts = availableConcepts;
        this.partialSolution = ImmutableList.of();
        this.startedConsuming = false;
        this.skippedConcepts = ImmutableList.of();
    }

    public boolean isSolved() {
        return sizeOf(requiredConcepts) == 0 && !partialSolution.isEmpty();
    }

    public boolean unsolvable() {
        return sizeOf(requiredConcepts) > availableConcepts.size();
    }

    private int sizeOf(CostType costType) {
        if(costType instanceof NullCostType) {
           return 0;
        } if(costType instanceof ConceptTrace) {
            return 1;
        } else if(costType instanceof ListValidator) {
            int size = 0;

            for(CostType costType1: ((ListValidator)costType).requiredConcepts) {
                size += sizeOf(costType1);
            }

            return size;
        }

        return 0;
    }

    public boolean canSkipTrace() {
        return !startedConsuming && availableConcepts.size() > 1 || canSkipTrace(requiredConcepts);
    }

    private boolean canSkipTrace(CostType costType) {
        if(costType instanceof ConceptTrace) {
            return !((ConceptTrace)costType).pickup;
        } else if(costType instanceof ArrayValidator) {
            return canSkipTrace(((ArrayValidator)costType).requiredConcepts.get(0));
        } else if(costType instanceof SetValidator) {
            return true;
        }
        return false;
    }

    public boolean consumableConceptEqual() {
        return availableConcepts.get(0).concept == getFirstConceptTrace(requiredConcepts).concept;
    }

    private ConceptTrace getFirstConceptTrace(CostType costType) {
        if(costType instanceof ConceptTrace) {
            return (ConceptTrace)requiredConcepts;
        }

        return getFirstConceptTrace(((ListValidator)requiredConcepts).requiredConcepts.get(0));
    }

    public boolean pickupMatch() {
        return consumableConceptEqual()
                && getFirstConceptTrace(requiredConcepts).pickup
                && availableConcepts.get(0).pickup;
    }

    public boolean requirePickup() {
        return getFirstConceptTrace(requiredConcepts).pickup;
    }

    public boolean requireStepOverAndAvailableStepOver() {
        return !getFirstConceptTrace(requiredConcepts).pickup && !availableConcepts.get(0).pickup;
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
        if(requiredConcepts instanceof ConceptTrace) {
            // Probably don't want to get here...?
            return new SolutionChecker(
                    NullCostType.val,
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
        } else if(requiredConcepts instanceof ArrayValidator) {
            ArrayValidator arrayVal = (ArrayValidator)requiredConcepts;
            return new SolutionChecker(
                    new ArrayValidator(arrayVal.requiredConcepts.subList(1, arrayVal.requiredConcepts.size())),
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
        } else if(requiredConcepts instanceof SetValidator) {
            SetValidator setVal = (SetValidator)requiredConcepts;
            ArrayList<CostType> nonMatchingRequired = new ArrayList<>();
            // Find matching and remove it... Then return the rest.
        }

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
