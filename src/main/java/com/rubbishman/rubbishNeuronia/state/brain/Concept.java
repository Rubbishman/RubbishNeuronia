package com.rubbishman.rubbishNeuronia.state.brain;

import com.rubbishman.rubbishRedux.internal.neuronia.state.cost.concept.ConceptTrace;

public enum Concept {
    RED,
    GREEN,
    BLUE,
    ORANGE,
    PURPLE,
    TEAL;

    public ConceptTrace pickup() {
        return new ConceptTrace(this, true);
    }

    public ConceptTrace stepOver() {
        return new ConceptTrace(this, false);
    }
}
