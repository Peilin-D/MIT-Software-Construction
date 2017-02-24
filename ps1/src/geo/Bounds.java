/* Copyright (c) 2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package geo;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Methods for computing with latitude-longitude bounding rectangles.
 * 
 * <p>
 * A latitude-longitude bounding rectangle is represented by a four-element
 * {@code List<Angle>}, where every angle is valid for measuring its coordinate:
 * <ul>
 * <li> list[0] is the northern boundary latitude,
 * <li> list[1] is the eastern boundary longitude,
 * <li> list[2] is the southern boundary latitude, and
 * <li> list[3] is the western boundary longitude.
 * </ul>
 * Note: "northern," for example, does not mean "north latitude;" the northern
 * boundary might still be a south latitude, and similarly for other boundaries.
 * <p>
 * The bounding rectangle contains a point (lat, long) if:
 * <ul>
 * <li> sweeping north from the southern to northern bound goes through lat, and
 * <li> sweeping east from the western to eastern bound goes through long.
 * </ul>
 * Sweeping east may cross the antimeridian, and a bounding rectangle may span
 * the antimeridian.
 * A bounding rectangle includes the points on its boundaries.
 * 
 * <p>
 * PS1 instructions: do NOT change the method signatures or specifications of
 * these methods, but you should implement their method bodies, and you may add
 * new public or private methods or classes if you like.
 */
public class Bounds {

    /**
     * Find latitude-longitude bounds for a set of points of interest (POIs).
     * Returns a smallest bounding rectangle, for example like this:
     * <br><img src="doc-files/bounds.svg"><br>
     * Not this:
     * <br><img src="doc-files/bounds-wrap.svg"><br>
     * unless POIs on the far side of the globe require wrapping around.
     * 
     * @param pointsOfInterest set of POIs, not modified by this method
     * @return a smallest latitude-longitude bounding rectangle, as defined in
     *         the documentation for this class, containing every POI in the
     *         input
     */
    public static List<Angle> boundingBox(Set<PointOfInterest> pointsOfInterest) {
    	if (pointsOfInterest.isEmpty()) {
    		return Collections.emptyList();
    	}
    	
        Angle northernLatitude = null;
        Angle southernLatitude = null;
        Angle westernLongitude = null;
        Angle easternLongitude = null;
        
        for (PointOfInterest pt : pointsOfInterest) {
    		if (northernLatitude == null || Angular.toDegrees(pt.latitude()) > Angular.toDegrees(northernLatitude)) {
    			northernLatitude = pt.latitude();
    		}
    		if (southernLatitude == null || Angular.toDegrees(pt.latitude()) < Angular.toDegrees(southernLatitude)) {
    			southernLatitude = pt.latitude();
    		}
        	if (easternLongitude == null || Angular.toDegrees(pt.longitude()) > Angular.toDegrees(easternLongitude)) {
        		easternLongitude = pt.longitude();
        	}
        	if (westernLongitude == null || Angular.toDegrees(pt.longitude()) < Angular.toDegrees(westernLongitude)) {
        		westernLongitude = pt.longitude();
        	}
        }
    	
        return Arrays.asList(northernLatitude, easternLongitude, southernLatitude, westernLongitude);
    }

    /**
     * Find points of interest (POIs) in a latitude-longitude bounding
     * rectangle.
     * 
     * @param pointsOfInterest set of POIs, not modified by this method
     * @param bounds a latitude-longitude bounding rectangle as defined in the
     *            documentation for this class, not modified by this method
     * @return all and only the POIs in the input that are contained in the
     *         given bounding rectangle
     */
    public static Set<PointOfInterest> inBoundingBox(Set<PointOfInterest> pointsOfInterest, List<Angle> bounds) {
        Set<PointOfInterest> retSet = new HashSet<PointOfInterest>();
        for (PointOfInterest pt : pointsOfInterest) {
        	if (Angular.toDegrees(pt.latitude()) <= Angular.toDegrees(bounds.get(0)) && Angular.toDegrees(pt.latitude()) >= Angular.toDegrees(bounds.get(2))
        		&& Angular.toDegrees(pt.longitude()) <= Angular.toDegrees(bounds.get(1)) && Angular.toDegrees(pt.longitude()) >= Angular.toDegrees(bounds.get(3))) {
        		retSet.add(pt);
        	}
        }
        return retSet;
    }

}
