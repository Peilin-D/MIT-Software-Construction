/* Copyright (c) 2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package geo;

/**
 * Immutable datatype representing an angle in a geographic coordinate system,
 * measured in degrees, minutes (1/60ths of a degree), and seconds (1/60ths of a
 * minute).
 * <ul>
 * <li> Valid angles for latitude range from 0 to 90 deg N or S, inclusive.
 *      Note that 0 deg N & S are different representations of the same latitude
 *      (equator).
 * <li> Valid angles for longitude range from 0 to 180 deg E or W, inclusive.
 *      0 deg E & W are different representations of the same longitude (prime
 *      meridian), and 180 deg E & W are also different representations of the
 *      same longitude (antimeridian).
 * <li> Valid angles for displacement range from 0 to 180 deg, inclusive.
 * </ul>
 * 
 * <p>
 * PS1 instructions: do NOT change this class.
 */
public class Angle {

    /** Degree symbol. */
    private static final String DEGREE = "\u00B0";
    /** Minute symbol. */
    private static final String PRIME = "\u2032";
    /** Second symbol. */
    private static final String DOUBLE_PRIME = "\u2033";

    /** Sexagesimal base. Minutes per degree, and seconds per minute. */
    private static final int BASE_60 = 60;

    private final int degrees;
    private final int minutes;
    private final int seconds;
    private final CardinalDirection direction;

    /*
     * Rep invariant:
     * 0 <= degrees
     * 0 <= minutes, seconds < 60
     * direction != null
     */

    /**
     * Make a new angle of degrees degrees plus minutes arc minutes plus seconds
     * arc seconds. Must be at most {@link Integer#MAX_VALUE} degrees total.
     * 
     * @param degrees degrees of arc, must be nonnegative
     * @param minutes minutes of arc, must be nonnegative
     * @param seconds seconds of arc, must be nonnegative
     * @param direction cardinal direction
     */
    public Angle(int degrees, int minutes, int seconds, CardinalDirection direction) {
        this.degrees = degrees + (minutes + seconds / BASE_60) / BASE_60;
        this.minutes = (minutes + seconds / BASE_60) % BASE_60;
        this.seconds = seconds % BASE_60;
        assert this.degrees >= 0 : "degrees < 0";
        assert this.minutes >= 0 : "minutes < 0";
        assert this.seconds >= 0 : "seconds < 0";
        this.direction = direction;
    }

    /**
     * @return positive whole degrees of arc in this angle
     */
    public int degrees() {
        return degrees;
    }

    /**
     * @return positive whole arc minutes in this angle, after whole degrees
     */
    public int minutes() {
        return minutes;
    }

    /**
     * @return positive arc seconds in this angle, after whole arc minutes
     */
    public int seconds() {
        return seconds;
    }

    /**
     * @return cardinal direction of this angle
     */
    public CardinalDirection direction() {
        return direction;
    }

    @Override
    public String toString() {
        return degrees + DEGREE + " " + minutes + PRIME + " " + seconds + DOUBLE_PRIME + " " + direction.toChar();
    }

    @Override
    public boolean equals(Object obj) {
        if ( ! (obj instanceof Angle)) { return false; }
        final Angle that = (Angle) obj;
        return this.direction().equals(that.direction())
                && this.degrees() == that.degrees()
                && this.minutes() == that.minutes()
                && this.seconds() == that.seconds();
    }

    @Override
    public int hashCode() {
        return direction().hashCode() + degrees() + minutes() + seconds();
    }
}
