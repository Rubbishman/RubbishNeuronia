package com.rubbishman.rubbishNeuronia.reducer.costValidator;

import com.google.common.collect.ImmutableList;

import java.util.LinkedList;

public class CostSearchProcessor {//ImmutableList<CostSolution> getSolutions()
    public static ImmutableList<CostSolution> processCostSearch(ICostSearcher costSearcher) {
        LinkedList<ICostSearcher> costSearchers = new LinkedList<>();
        ImmutableList<CostSolution> solutions = ImmutableList.of();

        costSearchers.addAll(costSearcher.consumeTrace());

        ICostSearcher nextCostSearcher;

        while(costSearchers.size() > 0) {
            nextCostSearcher = costSearchers.pop();
            if(nextCostSearcher.isTerminated()) {
                solutions = ImmutableList.<CostSolution>builder().addAll(solutions).addAll(nextCostSearcher.getSolutions()).build();
            } else {
                costSearchers.addAll(nextCostSearcher.consumeTrace());
            }
        }

        return solutions;
    }
}
