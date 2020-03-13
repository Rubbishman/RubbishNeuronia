package com.rubbishman.rubbishNeuronia.reducer.costValidator;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.rubbishman.rubbishNeuronia.reducer.costValidator.costTypes.CostType;
import com.rubbishman.rubbishNeuronia.reducer.costValidator.costTypes.NullCostType;
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
        if(requiredConcepts instanceof ConceptTrace && unsolvable((ConceptTrace)requiredConcepts)) {
            return true;
        }
        return sizeOf(requiredConcepts) > availableConcepts.size();
    }

    public boolean unsolvable(ConceptTrace conceptTrace) {
        if(conceptTrace.pickup
                && availableConcepts.get(0).pickup
                && conceptTrace.concept != availableConcepts.get(0).concept) {
            return true;
        } else if(!conceptTrace.pickup && availableConcepts.get(0).pickup){
            return true;
        }

        return false;
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
            return !((ConceptTrace)costType).pickup
                    || availableIsStepOver();
        } else if(costType instanceof ArrayValidator) {
            return canSkipTrace(((ArrayValidator)costType).requiredConcepts.get(0));
        } else if(costType instanceof SetValidator) {
            return true;
        }
        return false;
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

    public boolean shouldConsume() {
        if(requiredConcepts instanceof ConceptTrace) {
            return requiredConcepts.equals(availableConcepts.get(0));
        } else if(requiredConcepts instanceof ArrayValidator) {
            return true; //These nest, so try consuming them...?
        } else if(requiredConcepts instanceof SetValidator) {
            return true; //These nest, so try consuming them...?
        }

        return false;
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

        ImmutableSet.Builder<CostSolution> solutions = ImmutableSet.<CostSolution>builder();

        solutions.addAll(SolutionFinder.processTrace(
                new SolutionChecker(
                        arrayVal.requiredConcepts.get(0),
                        availableConcepts,
                        ImmutableList.of(),
                        startedConsuming,
                        ImmutableList.of()
                )
        ));

        for(CostSolution costSolution: solutions.build()) {
            possibleSolutionCheckers.add(
                    new SolutionChecker(
                            new ArrayValidator(arrayVal.requiredConcepts.subList(1, arrayVal.requiredConcepts.size())),
                            costSolution.remainingItems,
                            ImmutableList.<ConceptTrace>builder()
                                    .addAll(partialSolution)
                                    .addAll(costSolution.costSolution)
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

        for(int i = 0; i < setValidator.requiredConcepts.size(); i++) {
            ImmutableList.Builder<CostType> builder = ImmutableList.builder();
            for(int pre = 0; pre < i; pre++) {
                builder.add(setValidator.requiredConcepts.get(pre));
            }

            ImmutableSet.Builder<CostSolution> solutions = ImmutableSet.<CostSolution>builder();

            solutions.addAll(SolutionFinder.processTrace(
                    new SolutionChecker(
                            setValidator.requiredConcepts.get(i),
                            availableConcepts,
                            ImmutableList.of(),
                            startedConsuming,
                            ImmutableList.of()
                    )
            ));

            for(int post = i+1; post < setValidator.requiredConcepts.size(); post++) {
                builder.add(setValidator.requiredConcepts.get(post));
            }

            for(CostSolution costSolution: solutions.build()) {
                possibleSolutionCheckers.add(new SolutionChecker(
                        new SetValidator(builder.build()),
                        costSolution.remainingItems,
                        ImmutableList.<ConceptTrace>builder()
                                .addAll(partialSolution)
                                .addAll(costSolution.costSolution)
                                .build(),
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
