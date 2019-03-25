package com.example.explorer;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Class to represent Toy Explorer Position
 */
public class Position {
    /**
     * X - Coordinate
     */
    private int x;
    /**
     * Y Coordinate
     */
    private int y;

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }


    @Override
    public boolean equals(Object obj) {
        return new EqualsBuilder().equals(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(this.x).append(y).toHashCode();
    }
}
