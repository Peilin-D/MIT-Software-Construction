/* Copyright (c) 2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package geo;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Test;

public class BoundsTest {

    /*
     * TODO: your testing strategies for these methods should go here.
     * 
     * See the ic03-testing exercise for examples of what a testing strategy
     * comment looks like. Make sure you have partitions.
     */

    private static final Angle LATITUDE_N = new Angle(42, 21, 35, CardinalDirection.NORTH);
    private static final Angle LONGITUDE_W = new Angle(71, 5, 31, CardinalDirection.WEST);
    
    private static final Angle LATITUDE_S = new Angle(45, 35, 59, CardinalDirection.SOUTH);
    private static final Angle LONGITUDE_E = new Angle(60, 30, 48, CardinalDirection.EAST);
    
    private static final Angle LATITUDE_N2 = new Angle(30, 28, 51, CardinalDirection.NORTH);

    private static final PointOfInterest GREAT_DOME = new PointOfInterest(LATITUDE_N, LONGITUDE_W, "Great Dome", "");
    private static final PointOfInterest SE_POI = new PointOfInterest(LATITUDE_S, LONGITUDE_E, "SE_POI", "South-East POI");
    private static final PointOfInterest NE_POI = new PointOfInterest(LATITUDE_N2, LONGITUDE_E, "NE_POI", "North-East POI");
    
    @Test(expected = AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; // make sure assertions are enabled with VM argument: -ea
    }

    @Test
    public void testBoundingBoxSingleton() {
        List<Angle> expected = Arrays.asList(LATITUDE_N, LONGITUDE_W, LATITUDE_N, LONGITUDE_W);
        List<Angle> bounds = Bounds.boundingBox(Collections.singleton(GREAT_DOME));
        assertEquals("expected correct length", expected.size(), bounds.size());
        assertEquals("expected correct north bound", expected.get(0), bounds.get(0));
        assertEquals("expected correct east bound", expected.get(1), bounds.get(1));
        assertEquals("expected correct south bound", expected.get(2), bounds.get(2));
        assertEquals("expected correct west bound", expected.get(3), bounds.get(3));
    }
    
    @Test
    public void testBoundingBoxMultiple() {
    	List<Angle> expected = Arrays.asList(LATITUDE_N, LONGITUDE_E, LATITUDE_S, LONGITUDE_W);
    	Set<PointOfInterest> poiSet = new HashSet<PointOfInterest>();
    	poiSet.add(GREAT_DOME);
    	poiSet.add(SE_POI);
    	List<Angle> bounds = Bounds.boundingBox(poiSet);
    	assertEquals("expected correct length", expected.size(), bounds.size());
        assertEquals("expected correct north bound", expected.get(0), bounds.get(0));
        assertEquals("expected correct east bound", expected.get(1), bounds.get(1));
        assertEquals("expected correct south bound", expected.get(2), bounds.get(2));
        assertEquals("expected correct west bound", expected.get(3), bounds.get(3));	
    }
    
    @Test
    public void testBoundingBoxInNorth() {
    	List<Angle> expected = Arrays.asList(LATITUDE_N, LONGITUDE_E, LATITUDE_N2, LONGITUDE_W);
    	Set<PointOfInterest> poiSet = new HashSet<PointOfInterest>();
    	poiSet.add(GREAT_DOME);
    	poiSet.add(NE_POI);
    	List<Angle> bounds = Bounds.boundingBox(poiSet);
    	assertEquals("expected correct length", expected.size(), bounds.size());
        assertEquals("expected correct north bound", expected.get(0), bounds.get(0));
        assertEquals("expected correct east bound", expected.get(1), bounds.get(1));
        assertEquals("expected correct south bound", expected.get(2), bounds.get(2));
        assertEquals("expected correct west bound", expected.get(3), bounds.get(3));	
    }
    
    @Test
    public void testBoundingBoxEmpty() {
    	List<Angle> bounds = Bounds.boundingBox(Collections.emptySet());
    	assertTrue("expected empty list", bounds.isEmpty());
    }

    @Test
    public void testInBoundingBoxEmptySet() {
        Set<PointOfInterest> inBounds = Bounds.inBoundingBox(Collections.emptySet(),
                Arrays.asList(LATITUDE_N, LONGITUDE_W, LATITUDE_N, LONGITUDE_W));
        assertTrue("expected empty set", inBounds.isEmpty());
    }
    
    @Test
    public void testInBoundingBox() {
    	Set<PointOfInterest> poiSet = new HashSet<PointOfInterest>();
    	poiSet.add(GREAT_DOME);
    	poiSet.add(SE_POI);
    	poiSet.add(NE_POI);
    	Set<PointOfInterest> inBounds = Bounds.inBoundingBox(poiSet,
    			Arrays.asList(LATITUDE_N, LONGITUDE_E, LATITUDE_N2, LONGITUDE_W));
    	assertTrue("expect GREAT_DOME in box", inBounds.contains(GREAT_DOME));
    	assertTrue("expect NE POI in box", inBounds.contains(NE_POI));
    	assertFalse("expect SE POI NOT in box", inBounds.contains(SE_POI));
    }

    /*
     * Warning: all the tests you write here must be runnable against any
     * Bounds class that follows the spec. It will be run against several staff
     * implementations of Bounds, which will be done by overwriting
     * (temporarily) your version of Bounds with the staff's version.
     * 
     * DO NOT strengthen the spec of Bounds or its methods.
     * 
     * In particular, your test cases must not call helper methods of your own
     * that you have put in Bounds, because that means you're testing a
     * stronger spec than Bounds says. If you need such helper methods, define
     * them in a different class. If you only need them in this test class, then
     * keep them in this test class.
     */
}
