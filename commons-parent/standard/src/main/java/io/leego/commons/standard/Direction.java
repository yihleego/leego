package io.leego.commons.standard;

/**
 * @author Leego Yih
 */
public enum Direction {
    ASC, DESC;

    /**
     * Returns whether the direction is ascending.
     */
    public boolean isAscending() {
        return this.equals(ASC);
    }

    /**
     * Returns whether the direction is descending.
     */
    public boolean isDescending() {
        return this.equals(DESC);
    }
}
