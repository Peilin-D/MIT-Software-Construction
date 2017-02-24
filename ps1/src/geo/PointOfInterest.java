/* Copyright (c) 2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package geo;

import static geo.CardinalDirection.EAST;
import static geo.CardinalDirection.NORTH;
import static geo.CardinalDirection.SOUTH;
import static geo.CardinalDirection.WEST;

/**
 * Immutable datatype representing a place with a name, description, and
 * location.
 * 
 * <p>
 * PS1 instructions: do NOT change this class.
 */
public class PointOfInterest {

    private final Angle latitude;
    private final Angle longitude;
    private final String name;
    private final String description;

    /*
     * Rep invariant:
     * latitude & longitude are valid for measuring latitude & longitude,
     *   respectively
     * name is not empty
     * latitude, longitude, name, description != null
     */

    /**
     * Make a new point of interest (POI).
     * 
     * @param latitude latitude of the POI, must be a valid latitude as defined
     *            in {@link Angle}
     * @param longitude longitude of the POI, must be a valid longitude as
     *            defined in {@link Angle}
     * @param name name of the POI, must be nonempty
     * @param description description of the POI
     */
    public PointOfInterest(Angle latitude, Angle longitude, String name, String description) {
        assert latitude.direction() == NORTH || latitude.direction() == SOUTH : "not a latitude: " + latitude;
        assert longitude.direction() == EAST || longitude.direction() == WEST : "not a longitude: " + longitude;
        assert ! name.isEmpty() : "empty name";
        this.latitude = latitude;
        this.longitude = longitude;
        this.name = name;
        this.description = description;
    }

    /**
     * @return latitude of this point of interest
     */
    public Angle latitude() {
        return latitude;
    }

    /**
     * @return longitude of this point of interest
     */
    public Angle longitude() {
        return longitude;
    }

    /**
     * @return nonempty name of this point of interest
     */
    public String name() {
        return name;
    }

    /**
     * @return possibly-empty description of this point of interest
     */
    public String description() {
        return description;
    }

    @Override
    public String toString() {
        return name + " @ (" + latitude + ", " + longitude + ")";
    }

    @Override
    public boolean equals(Object obj) {
        if ( ! (obj instanceof PointOfInterest)) { return false; }
        final PointOfInterest that = (PointOfInterest) obj;
        return this.latitude().equals(that.latitude())
                && this.longitude().equals(that.longitude())
                && this.name().equals(that.name())
                && this.description().equals(that.description());
    }

    @Override
    public int hashCode() {
        return latitude().hashCode() + longitude().hashCode() + name().hashCode() + description().hashCode();
    }
}
