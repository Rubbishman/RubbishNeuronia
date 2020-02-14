package com.rubbishman.rubbishNeuronia.reducer.costValidator;

import com.google.common.collect.ImmutableList;

import java.util.List;

public interface ICostSearcher {
    ImmutableList<CostSolution> getSolutions();
    boolean isTerminated();
    List<ICostSearcher> consumeTrace();
}
