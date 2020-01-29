package com.rubbishman.rubbishNeuronia.state.brain;

import com.google.common.collect.ImmutableList;
import org.organicdesign.fp.collections.PersistentHashMap;

public class ConceptTree {
    public final ImmutableList<Concept> concepts;
    public final PersistentHashMap<Integer, ImmutableList<Concept>> xCoord;

    public ConceptTree(ImmutableList<Concept> concepts, PersistentHashMap<Integer, ImmutableList<Concept>> xCoord) {
        this.concepts = concepts;
        this.xCoord = xCoord;
    }
}
