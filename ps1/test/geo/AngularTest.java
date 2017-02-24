/* Copyright (c) 2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package geo;

import static org.junit.Assert.*;

import org.junit.Test;

public class AngularTest {

    /*
     * TODO: your testing strategies for these methods should go here.
     * 
     * See the ic03-testing exercise for examples of what a testing strategy
     * comment looks like. Make sure you have partitions.
     */

    private static final Angle EQUATOR = new Angle(0, 0, 0, CardinalDirection.NORTH);
    private static final Angle NORTH_POLE = new Angle(90, 0, 0, CardinalDirection.NORTH);
    private static final Angle SOUTH_POLE = new Angle(90, 0, 0, CardinalDirection.SOUTH);
    private static final Angle SOMEWHERE_EAST = new Angle(135, 0, 0, CardinalDirection.EAST);
    private static final Angle SOMEWHERE_WEST = new Angle(135, 0, 0, CardinalDirection.WEST);

    @Test(expected = AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; // make sure assertions are enabled with VM argument: -ea
    }

    @Test
    public void testToDegreesEquator() {
        assertEquals("expected equator at 0 degrees", 0.0, Angular.toDegrees(EQUATOR), 0.0001);
        assertEquals("expected north pole at 90 degrees", 90.0, Angular.toDegrees(NORTH_POLE), 0.0001);
        assertEquals("expected south pole at 90 degrees", -90.0, Angular.toDegrees(SOUTH_POLE), 0.0001);
        assertEquals("expected somewhere east at 135 degrees", 135.0, Angular.toDegrees(SOMEWHERE_EAST), 0.0001);
        assertEquals("expected somewhere west at 135 degrees", -135.0, Angular.toDegrees(SOMEWHERE_WEST), 0.0001);
    }

    @Test
    public void testDisplacementEquatorNorthPole() {
        Angle displacement = Angular.displacement(EQUATOR, NORTH_POLE);
        assertEquals("expected 90 degrees", 90, displacement.degrees());
        assertEquals("expected 0 minutes", 0, displacement.minutes());
        assertEquals("expected 0 seconds", 0, displacement.seconds());
        assertEquals("expected north", CardinalDirection.NORTH, displacement.direction());
    }
    
    @Test
    public void testDisplacementEquatorSouthPole() {
    	Angle displacement = Angular.displacement(EQUATOR, SOUTH_POLE);
    	assertEquals("expected 90 degrees", 90, displacement.degrees());
        assertEquals("expected 0 minutes", 0, displacement.minutes());
        assertEquals("expected 0 seconds", 0, displacement.seconds());
        assertEquals("expected south", CardinalDirection.SOUTH, displacement.direction());
    }

    @Test
    public void testDisplacementWestEast() {
    	Angle displacement = Angular.displacement(SOMEWHERE_WEST, SOMEWHERE_EAST);
    	assertEquals("expected 90 degrees", 90, displacement.degrees());
        assertEquals("expected 0 minutes", 0, displacement.minutes());
        assertEquals("expected 0 seconds", 0, displacement.seconds());
        assertEquals("expected west", CardinalDirection.WEST, displacement.direction());
    }
    
    @Test
    public void testDisplacementEastWest() {
    	Angle displacement = Angular.displacement(SOMEWHERE_EAST, SOMEWHERE_WEST);
    	assertEquals("expected 90 degrees", 90, displacement.degrees());
        assertEquals("expected 0 minutes", 0, displacement.minutes());
        assertEquals("expected 0 seconds", 0, displacement.seconds());
        assertEquals("expected east", CardinalDirection.EAST, displacement.direction());
    }
    /*
     * Warning: all the tests you write here must be runnable against any
     * Angular class that follows the spec. It will be run against several staff
     * implementations of Angular, which will be done by overwriting
     * (temporarily) your version of Angular with the staff's version.
     * 
     * DO NOT strengthen the spec of Angular or its methods.
     * 
     * In particular, your test cases must not call helper methods of your own
     * that you have put in Angular, because that means you're testing a
     * stronger spec than Angular says. If you need such helper methods, define
     * them in a different class. If you only need them in this test class, then
     * keep them in this test class.
     */
}
