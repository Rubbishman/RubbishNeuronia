package com.rubbishman.rubbishNeuronia.reducer.costValidator;

import com.google.common.collect.ImmutableList;
import com.rubbishman.rubbishNeuronia.reducer.costValidator.costTypes.CostType;
import com.rubbishman.rubbishNeuronia.state.cost.concept.ConceptTrace;

import java.util.LinkedList;
import java.util.List;

public class SolutionFinder {
    public static List<CostSolution> checkSolution(CostType requiredConcepts, ImmutableList<ConceptTrace> availableConcepts) {
        SolutionChecker solChecker = new SolutionChecker(
                requiredConcepts,
                availableConcepts
        );

        if (!solChecker.isSolved()) { //If it counts as solved... We have empty input
            return processTrace(new SolutionChecker(
                    requiredConcepts,
                    availableConcepts
            ));
        }

        return new LinkedList<>();
    }

    protected static List<CostSolution> processTrace(SolutionChecker solutionChecker) {
        List<CostSolution> solutions = new LinkedList<>();

        if (solutionChecker.isSolved()) {
            solutions.add(solutionChecker.toCostSolution());

            return solutions;
        }

        if (solutionChecker.unsolvable()) {
            return solutions;
        }

        //Move to the next available and see if we can solve from that instead
        if (solutionChecker.canSkipTrace()) {
            solutions.addAll(
                SolutionFinder.processTrace(
                    solutionChecker.skipTrace()
                )
            );
        }

        List<SolutionChecker> possibleSolutionCheckers = solutionChecker.consumeTrace();

        for(SolutionChecker nestedSolutionChecker: possibleSolutionCheckers) {
            solutions.addAll(
                SolutionFinder.processTrace(
                        nestedSolutionChecker
                )
            );
        }

        return solutions;
    }
}
