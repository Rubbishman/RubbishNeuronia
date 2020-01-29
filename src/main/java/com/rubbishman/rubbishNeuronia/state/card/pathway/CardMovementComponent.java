package com.rubbishman.rubbishNeuronia.state.card.pathway;

public class CardMovementComponent {
    public final Movement movement;
    public final boolean pickup;
    public final int costTier;

    public CardMovementComponent(Movement movement, boolean pickup, int costTier) {
        this.movement = movement;
        this.pickup = pickup;
        this.costTier = costTier;
    }
}
