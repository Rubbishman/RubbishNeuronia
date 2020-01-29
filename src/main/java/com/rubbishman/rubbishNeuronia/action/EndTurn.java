package com.rubbishman.rubbishNeuronia.action;

import com.rubbishman.rubbishRedux.external.operational.store.Identifier;

public class EndTurn {
    public final Identifier brainId;

    public EndTurn(Identifier brainId) {
        this.brainId = brainId;
    }
}
