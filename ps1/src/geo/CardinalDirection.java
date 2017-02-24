/* Copyright (c) 2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package geo;

/**
 * Immutable enumeration of cardinal directions.
 * 
 * <p>
 * PS1 instructions: do NOT change this enumeration.
 */
public enum CardinalDirection {

    NORTH, EAST, SOUTH, WEST;

    /**
     * @return upper-case first letter of this cardinal direction
     */
    public char toChar() {
        return toString().charAt(0);
    }

    /**
     * @param letter the upper-case first letter of a cardinal direction
     * @return the cardinal direction whose first letter is letter
     */
    public static CardinalDirection fromChar(char letter) {
        for (CardinalDirection direction : values()) {
            if (direction.toChar() == letter) {
                return direction;
            }
        }
        throw new IllegalArgumentException("invalid direction letter: " + letter);
    }
}
