package com.rubbishman.rubbishNeuronia.reducer.costValidator;

import com.google.common.collect.ImmutableList;
import com.rubbishman.rubbishNeuronia.reducer.costValidator.costTypes.CostType;
import com.rubbishman.rubbishNeuronia.reducer.costValidator.costTypes.NullCostType;
import com.rubbishman.rubbishNeuronia.state.brain.Concept;
import com.rubbishman.rubbishNeuronia.state.cost.ArrayValidator;
import com.rubbishman.rubbishNeuronia.state.cost.ListValidator;
import com.rubbishman.rubbishNeuronia.state.cost.SetValidator;
import com.rubbishman.rubbishNeuronia.state.cost.concept.ConceptTrace;

import java.util.ArrayList;
import java.util.List;

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
        return consumableConceptEqual(availableConcepts.get(0).concept, requiredConcepts);
    }

    private boolean consumableConceptEqual(Concept concept, CostType costType) {
        if(costType instanceof ConceptTrace) {
            return concept == ((ConceptTrace) costType).concept;
        } else if(costType instanceof ArrayValidator) {
            return concept == getFirstConceptTrace(requiredConcepts).concept;
        } else if(costType instanceof SetValidator){
            SetValidator setVal = (SetValidator)costType;
            for(CostType setCost: setVal.requiredConcepts) {
                if(consumableConceptEqual(concept, setCost)) {
                    return true;
                }
            }
        }

        return false;
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
        return require(true, requiredConcepts);
    }

    private boolean require(boolean pickup, CostType costType) {
        if(costType instanceof ConceptTrace) {
            return pickup == ((ConceptTrace) costType).pickup;
        } else if(costType instanceof ArrayValidator) {
            return pickup == getFirstConceptTrace(requiredConcepts).pickup;
        } else if(costType instanceof SetValidator){
            SetValidator setVal = (SetValidator)costType;
            for(CostType setCost: setVal.requiredConcepts) {
                if(require(pickup, setCost)) {
                    return true;
                }
            }
        }

        return false;
    }

    public boolean requireStepOverAndAvailableStepOver() {
        return !require(false, requiredConcepts) && availableIsStepOver();
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

    public SolutionChecker consumeTrace(ConceptTrace conceptTrace) {
        return new SolutionChecker(
                NullCostType.val,
                availableConcepts.subList(1, availableConcepts.size()),
                ImmutableList.<ConceptTrace>builder()
                        .addAll(partialSolution)
                        .add(availableConcepts.get(0))
                        .build(),
                true,
                conceptTrace.pickup ?
                        skippedConcepts
                        : ImmutableList.<ConceptTrace>builder()
                        .addAll(skippedConcepts)
                        .add(availableConcepts.get(0))
                        .build()
        );
    }

    public ArrayList<SolutionChecker> consumeTrace(ArrayValidator arrayVal) {
        ArrayList<SolutionChecker> possibleSolutionCheckers = new ArrayList<>();

        List<CostSolution> solutions = ArraySolutionChecker.processTrace(
                new SolutionChecker(
                        arrayVal.requiredConcepts.get(0),
                        availableConcepts,
                        partialSolution,
                        startedConsuming,
                        skippedConcepts
                )
        );

        for(CostSolution costSolution: solutions) {
            possibleSolutionCheckers.add(
                    new SolutionChecker(
                            new ArrayValidator(arrayVal.requiredConcepts.subList(1, arrayVal.requiredConcepts.size())),
                            availableConcepts.subList(1, availableConcepts.size()),
                            ImmutableList.<ConceptTrace>builder()
                                    .addAll(partialSolution)
                                    .addAll(costSolution.remainingItems)
                                    .build(),
                            true,
                            ImmutableList.<ConceptTrace>builder()
                                    .addAll(skippedConcepts)
                                    .addAll(costSolution.skippedItems)
                                    .build()
                    )
            );
        }

        return possibleSolutionCheckers;
    }

    public ArrayList<SolutionChecker> consumeTrace(SetValidator setValidator) {
        ArrayList<SolutionChecker> possibleSolutionCheckers = new ArrayList<>();

        ArrayList<CostType> nonMatchingRequired = new ArrayList<>();
        ArrayList<CostType> matchingRequired = new ArrayList<>();
        // Find matching and remove it... Then return the rest.
        for(CostType costType: setValidator.requiredConcepts) {
            if(consumableConceptEqual(availableConcepts.get(0).concept, costType)) {
                matchingRequired.add(costType);
            } else {
                nonMatchingRequired.add(costType);
            }
        }

        for(int i = 0; i < matchingRequired.size(); i++) {
            ImmutableList.Builder<CostType> builder = ImmutableList.builder();
            for(int pre = 0; pre < i; pre++) {
                builder.add(matchingRequired.get(pre));
            }

            List<CostSolution> solutions = ArraySolutionChecker.processTrace(
                    new SolutionChecker(
                            matchingRequired.get(i),
                            availableConcepts,
                            partialSolution,
                            startedConsuming,
                            skippedConcepts
                    )
            );

            for(int post = i+1; post < matchingRequired.size(); post++) {
                builder.add(matchingRequired.get(post));
            }

            for(CostSolution costSolution: solutions) {
                possibleSolutionCheckers.add(new SolutionChecker(
                        new SetValidator(builder.build()),
                        costSolution.remainingItems,
                        costSolution.costSolution,
                        true,
                        ImmutableList.<ConceptTrace>builder()
                                .addAll(skippedConcepts)
                                .addAll(costSolution.skippedItems)
                                .build()
                ));
            }
        }

        return possibleSolutionCheckers;
    }

    public ArrayList<SolutionChecker> consumeTrace() {
        ArrayList<SolutionChecker> possibleSolutionCheckers = new ArrayList<>();

        if(requiredConcepts instanceof ConceptTrace) {
            possibleSolutionCheckers.add(consumeTrace((ConceptTrace)requiredConcepts));
        } else if(requiredConcepts instanceof ArrayValidator) {
            possibleSolutionCheckers.addAll(consumeTrace((ArrayValidator)requiredConcepts));
        } else if(requiredConcepts instanceof SetValidator) {
            possibleSolutionCheckers.addAll(consumeTrace((SetValidator)requiredConcepts));
        }

        return possibleSolutionCheckers;
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
