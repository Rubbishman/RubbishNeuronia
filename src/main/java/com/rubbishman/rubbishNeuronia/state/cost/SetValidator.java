package com.rubbishman.rubbishNeuronia.state.cost;

import com.google.common.collect.ImmutableList;
import com.rubbishman.rubbishNeuronia.state.brain.Concept;

public class SetValidator {
    public final ImmutableList<Concept> requiredConcepts;

    public SetValidator(ImmutableList<Concept> requiredConcepts) {
        this.requiredConcepts = requiredConcepts;
    }
}