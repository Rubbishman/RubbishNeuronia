package com.rubbishman.rubbishNeuronia.state;

public class CurrentThoughtLocation {
    public final int x;
    public final int y;
    public final CurrentThoughtLocation thoughtLocationTransition;

    public CurrentThoughtLocation(int x, int y, CurrentThoughtLocation thoughtLocationTransition) {
        this.x = x;
        this.y = y;
        this.thoughtLocationTransition = thoughtLocationTransition;
    }
}
