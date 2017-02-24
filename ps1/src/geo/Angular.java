/* Copyright (c) 2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package geo;

/**
 * Methods for computing with angles.
 * 
 * <p>
 * PS1 instructions: do NOT change the method signatures or specifications of
 * these methods, but you should implement their method bodies, and you may add
 * new public or private methods or classes if you like.
 */
public class Angular {

    /**
     * Convert a degree-minutes-seconds angle to signed floating-point degrees.
     * 
     * @param dmsAngle angle in degrees-minutes-seconds
     * @return degrees in dmsAngle, accurate to within 0.0001 degrees, where
     *         angles north & east are positive and south & west are negative
     */
    public static double toDegrees(Angle dmsAngle) {
        double degrees = (double)dmsAngle.degrees() + dmsAngle.minutes() / 60.0 + dmsAngle.seconds() / 3600.0;
        if (dmsAngle.direction() == CardinalDirection.EAST || dmsAngle.direction() == CardinalDirection.NORTH) {
        	return degrees;
        } else {
        	return -degrees;
        }
    
    }

    /**
     * Angular displacement from begin to end. Returns an angle with smallest
     * absolute value, for example like this:
     * <br><img src="doc-files/displacement.svg"><br>
     * Not this:
     * <br><img src="doc-files/displacement-invert.svg"><br>
     * 
     * @param begin starting angle, must be a valid latitude or longitude as
     *            defined in {@link Angle}
     * @param end ending angle, must be a valid angle measuring the same
     *            coordinate (latitude or longitude) as begin
     * @return angle with a smallest absolute value, measuring the same
     *         coordinate as the inputs, that sweeps from begin to end
     */
    public static Angle displacement(Angle begin, Angle end) {
        int secBegin = toSeconds(begin);
        int secEnd = toSeconds(end);
        int secDisplacement = Math.abs(secEnd - secBegin);
        int total = 3600 * 360;
        CardinalDirection direction;
        if (total >= 2 * secDisplacement) {
        	direction = end.direction();
        } else {
        	secDisplacement = total - secDisplacement;
        	if (end.direction() == CardinalDirection.EAST) {
        		direction = CardinalDirection.WEST;	
        	} else if (end.direction() == CardinalDirection.WEST) {
        		direction = CardinalDirection.EAST;
        	} else if (end.direction() == CardinalDirection.NORTH) {
        		direction = CardinalDirection.SOUTH;
        	} else {
        		direction = CardinalDirection.NORTH;
        	}
        }  
        Angle angDisplacement = toAngle(secDisplacement, direction);
        return angDisplacement;
        
    }

    /**
     * Convert a degree-minutes-seconds angle to signed integer seconds.
     * 
     * @param dmsAngle angle in degrees-minutes-seconds
     * @return seconds in dmsAngle, where
     *         angles north & east are positive and south & west are negative
     */
    public static int toSeconds(Angle dmsAngle) {
    	int seconds = dmsAngle.degrees() * 3600 + dmsAngle.minutes() * 60 + dmsAngle.seconds();
    	if (dmsAngle.direction() == CardinalDirection.EAST || dmsAngle.direction() == CardinalDirection.NORTH) {
    		return seconds;
    	} else {
    		return -seconds;
    	}
    }
    
    /**
     * Convert a signed integer seconds to a degree-minutes-seconds angle.
     * 
     * @param total seconds of this angle 
     * @param the direction of this angle
     * @return a degree-minutes-seconds angle
     */
    public static Angle toAngle(int seconds, CardinalDirection direction) {
    	int sec = seconds % 60;
    	seconds /= 60;
    	int min = seconds % 60;
    	seconds /= 60;
    	int deg = seconds;
    	return new Angle(deg, min, sec, direction);
    }
}
