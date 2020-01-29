package com.rubbishman.rubbishNeuronia.state.card.experience;

import com.rubbishman.rubbishRedux.external.operational.store.Identifier;

public class ExperienceCard {
    public final Identifier brainId;
    public final ConceptPlacement[] conceptPlacements;

    public ExperienceCard(Identifier brainId,ConceptPlacement[] conceptPlacements) {
        this.brainId = brainId;
        this.conceptPlacements = conceptPlacements;
    }
}
