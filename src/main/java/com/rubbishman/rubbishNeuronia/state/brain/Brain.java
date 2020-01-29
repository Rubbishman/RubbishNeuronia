package com.rubbishman.rubbishNeuronia.state.brain;

import com.google.common.collect.ImmutableList;
import com.rubbishman.rubbishRedux.internal.neuronia.state.InitialThoughtLocation;
import com.rubbishman.rubbishRedux.internal.neuronia.state.ThoughtLocationTransition;
import org.organicdesign.fp.collections.PersistentHashMap;

public class Brain {
    private final PersistentHashMap<Integer, PersistentHashMap<Integer, ImmutableList<Concept>>> inBrainConcepts;
    public final ImmutableList<Concept> activeMemory;
    public final ImmutableList<Concept> conceptReserve;
    public final ThoughtLocationTransition currentThoughtLocation;
    public final InitialThoughtLocation initialThoughtLocation;
    // public final ImmutableList<Skill> activeSkills;

    public Brain() {
        inBrainConcepts = PersistentHashMap.empty();
        activeMemory = ImmutableList.of();
        conceptReserve = ImmutableList.of();
        currentThoughtLocation = new ThoughtLocationTransition(0, 0, false, null);
        initialThoughtLocation = new InitialThoughtLocation(0, 0);
    }

    public Brain(PersistentHashMap<Integer, PersistentHashMap<Integer, ImmutableList<Concept>>> inBrainConcepts) {
        this.inBrainConcepts = inBrainConcepts;
        activeMemory = ImmutableList.of();
        conceptReserve = ImmutableList.of();
        currentThoughtLocation = new ThoughtLocationTransition(0, 0, false, null);
        initialThoughtLocation = new InitialThoughtLocation(0, 0);
    }

    public Brain(PersistentHashMap<Integer, PersistentHashMap<Integer, ImmutableList<Concept>>> inBrainConcepts,
                 ImmutableList<Concept> activeMemory,
                 ImmutableList<Concept> conceptReserve,
                 ThoughtLocationTransition currentThoughtLocation,
                 InitialThoughtLocation initialThoughtLocation) {
        this.activeMemory = activeMemory;
        this.conceptReserve = conceptReserve;
        this.inBrainConcepts = inBrainConcepts;
        this.currentThoughtLocation = currentThoughtLocation;
        this.initialThoughtLocation = initialThoughtLocation;
    }

    public Brain setCurrentLocation(ThoughtLocationTransition currentThoughtLocation) {
        return new Brain(
                inBrainConcepts,
                activeMemory,
                conceptReserve,
                currentThoughtLocation,
                initialThoughtLocation
        );
    }

    public boolean hasConcept(int x, int y) {
        ConceptTree conceptTree = traverseConceptTree(x, y);
        return conceptTree.concepts != null;
    }

    public ConceptTree traverseConceptTree(int x, int y) {
        PersistentHashMap<Integer, ImmutableList<Concept>> xCoord;
        if(inBrainConcepts.containsKey(x)) {
            xCoord = inBrainConcepts.get(x);

            return new ConceptTree(xCoord.get(y), xCoord);
        }

        return new ConceptTree(null,PersistentHashMap.empty());
    }

    public Brain pickupConcept(int x, int y) {
        ConceptTree conceptTree = traverseConceptTree(x, y);
        if(conceptTree.concepts != null) {
            Concept concept = conceptTree.concepts.get(conceptTree.concepts.size()-1);
            return new Brain(
                    inBrainConcepts.assoc(x, conceptTree.xCoord.assoc(y, conceptTree.concepts.subList(0, conceptTree.concepts.size()-1))),
                    ImmutableList.<Concept>builder().addAll(activeMemory).add(concept).build(),
                    conceptReserve,
                    currentThoughtLocation,
                    initialThoughtLocation
            );
        }

        return this;
    }

    public Brain addConcept(Concept concept, int x, int y) {
        ConceptTree conceptTree = traverseConceptTree(x, y);
        if(conceptTree.concepts != null) {
            ImmutableList<Concept> newConcepts = ImmutableList.<Concept>builder().addAll(conceptTree.concepts).add(concept).build();
            return new Brain(inBrainConcepts.assoc(x, conceptTree.xCoord.assoc(y, newConcepts)));
        }

        ImmutableList<Concept> cp = ImmutableList.of(concept);

        return new Brain(inBrainConcepts.assoc(x, conceptTree.xCoord.assoc(y, cp)));
    }

    public Brain endTurn() {
        return new Brain(
                inBrainConcepts,
                ImmutableList.of(),
                ImmutableList.<Concept>builder().addAll(conceptReserve).addAll(activeMemory).build(),
                new ThoughtLocationTransition(currentThoughtLocation.x, currentThoughtLocation.y, false, null),
                new InitialThoughtLocation(currentThoughtLocation.x, currentThoughtLocation.y)
        );
    }
}
