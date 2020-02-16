package com.rubbishman.rubbishNeuronia.reducer.costValidator;

import com.google.common.collect.ImmutableList;
import com.rubbishman.rubbishNeuronia.state.cost.concept.ConceptTrace;

import java.util.LinkedList;
import java.util.List;

public class ArraySolutionChecker {
    public static List<CostSolution> checkSolution(ImmutableList<ConceptTrace> requiredConcepts, ImmutableList<ConceptTrace> availableConcepts) {
        if(!requiredConcepts.isEmpty() && !availableConcepts.isEmpty()) {
            return consumeTrace(
                    requiredConcepts,
                    availableConcepts,
                    ImmutableList.of(),
                    false,
                    ImmutableList.of()
            );
        }
        return new LinkedList<>();
    }

    private boolean failedTermination(ImmutableList<ConceptTrace> requiredConcepts, ImmutableList<ConceptTrace> availableConcepts) {
        if(requiredConcepts.get(0).pickup
                && availableConcepts.get(0).concept != requiredConcepts.get(0).concept) {
            return true;
        }

        if(requiredConcepts.get(0).pickup && !availableConcepts.get(0).pickup) {
            return true;
        }

        return false;
    }

    public boolean isTerminated(ImmutableList<ConceptTrace> requiredConcepts, ImmutableList<ConceptTrace> availableConcepts) {
        if(requiredConcepts.isEmpty() || availableConcepts.isEmpty()) {
            return true;
        }

        if(failedTermination(requiredConcepts, availableConcepts)) {
            return true;
        }

        return false;
    }

    private static List<CostSolution> consumeTrace(
            ImmutableList<ConceptTrace> requiredConcepts,
            ImmutableList<ConceptTrace> availableConcepts,
            ImmutableList<ConceptTrace> partialSolution,
            boolean startedConsuming,
            ImmutableList<ConceptTrace> skippedConcepts) {
        List<CostSolution> solutions = new LinkedList<>();

        //Solved
        if(requiredConcepts.isEmpty() && !partialSolution.isEmpty()) {
            solutions.add(new CostSolution(
                partialSolution,
                availableConcepts
            ));

            return solutions;
        }

        //Could never solve
        if(requiredConcepts.size() > availableConcepts.size()) {
            return solutions;
        }

        //Move to the next available and see if we can solve from that instead
        if(!startedConsuming && availableConcepts.size() > 1) {
            solutions.addAll(ArraySolutionChecker.consumeTrace(
                    requiredConcepts,
                    availableConcepts.subList(1, availableConcepts.size()),
                    ImmutableList.of(),
                    false,
                    ImmutableList.<ConceptTrace>builder()
                            .addAll(skippedConcepts)
                            .add(availableConcepts.get(0))
                            .build()
            ));
        }

        boolean conceptEqual = availableConcepts.get(0).concept == requiredConcepts.get(0).concept;
        boolean pickupAndEqual = availableConcepts.get(0).pickup
                && conceptEqual;

        if(requiredConcepts.get(0).pickup
        ) {
            if(pickupAndEqual) {
                solutions.addAll(ArraySolutionChecker.consumeTrace(
                        requiredConcepts.subList(1, requiredConcepts.size()),
                        availableConcepts.subList(1, availableConcepts.size()),
                        ImmutableList.<ConceptTrace>builder()
                                .addAll(partialSolution)
                                .add(availableConcepts.get(0))
                                .build(),
                        true,
                        skippedConcepts
                ));
            } else if (!availableConcepts.get(0).pickup) {
                solutions.addAll(ArraySolutionChecker.consumeTrace(
                        requiredConcepts,
                        availableConcepts.subList(1, availableConcepts.size()),
                        ImmutableList.<ConceptTrace>builder()
                                .addAll(partialSolution)
                                .add(availableConcepts.get(0))
                                .build(),
                        startedConsuming,
                        skippedConcepts
                ));
            }
        } else if(!requiredConcepts.get(0).pickup && !availableConcepts.get(0).pickup) {
            if(conceptEqual) { // Try consuming
                solutions.addAll(ArraySolutionChecker.consumeTrace(
                        requiredConcepts.subList(1, requiredConcepts.size()),
                        availableConcepts.subList(1, availableConcepts.size()),
                        ImmutableList.<ConceptTrace>builder()
                                .addAll(partialSolution)
                                .add(availableConcepts.get(0))
                                .build(),
                        true,
                        skippedConcepts
                ));
            }

            // Even if it matches, can choose to skip stepOver matches
            solutions.addAll(ArraySolutionChecker.consumeTrace(
                    requiredConcepts,
                    availableConcepts.subList(1, availableConcepts.size()),
                    ImmutableList.<ConceptTrace>builder()
                            .addAll(partialSolution)
                            .add(availableConcepts.get(0))
                            .build(),
                    startedConsuming,
                    skippedConcepts
            ));
        }

        return solutions;
    }
}
