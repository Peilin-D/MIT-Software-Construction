/* Copyright (c) 2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package geo;

import static org.junit.Assert.*;

//import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

public class MappingTest {

    /*
     * TODO: your testing strategies for these methods should go here.
     * 
     * See the ic03-testing exercise for examples of what a testing strategy
     * comment looks like. Make sure you have partitions.
     */

    private static final Angle EQUATOR = new Angle(0, 0, 0, CardinalDirection.NORTH);
    private static final Angle PRIME_MERIDIAN = new Angle(0, 0, 0, CardinalDirection.EAST);

    private static final PointOfInterest NULL_ISLAND = new PointOfInterest(EQUATOR, PRIME_MERIDIAN, "Null Island", "");
    private static final PointOfInterest HARVARD = new PointOfInterest(new Angle(42,22,37,CardinalDirection.NORTH), new Angle(71, 7, 0, CardinalDirection.WEST), "Harvard", "A top university");
    private static final PointOfInterest CENTRAL_BARBER = new PointOfInterest(new Angle(42,22,49,CardinalDirection.NORTH), new Angle(71,7,10,CardinalDirection.WEST), "Central Barbershop","A famous barbershop");
    private static final PointOfInterest CHILD_HALL = new PointOfInterest(new Angle(42,22,44,CardinalDirection.NORTH), new Angle(71,7,4,CardinalDirection.WEST), "Child Hall", "Harvard GSAS residence hall");
    
    
    @Test(expected = AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; // make sure assertions are enabled with VM argument: -ea
    }

    @Test
    public void testFindCategoriesEmptyKeywords() {
        Set<String> categories = Mapping.findCategories(NULL_ISLAND, Collections.emptyMap());
        assertTrue("expected empty set", categories.isEmpty());
    }
    
    @Test
    public void testCategories() {
    	Map<String, Set<String>> categories = new HashMap<String, Set<String>>();
    	Set<String> higherEducation = new HashSet<String>();
    	Set<String> services = new HashSet<String>();
    	Set<String> residences = new HashSet<String>();
    	higherEducation.addAll(Arrays.asList("University", "College"));
    	services.addAll(Arrays.asList("Barbershop", "Food", "Super market"));
    	residences.addAll(Arrays.asList("residence hall", "apartment"));
    	categories.put("Higher Education", higherEducation);
    	categories.put("Services", services);
    	categories.put("Residences", residences);
    	
    	Set<String> cat = Mapping.findCategories(HARVARD, categories);
    	Set<String> expect = new HashSet<String>();
    	expect.add("Higher Education");
    	assertEquals("expected Higher Education", cat, expect);
    	
    	cat = Mapping.findCategories(CENTRAL_BARBER, categories);
    	expect.clear();
    	expect.add("Services");
    	assertEquals("expected Services", cat, expect);
    	
    	cat = Mapping.findCategories(CHILD_HALL, categories);
    	expect.clear();
    	expect.add("Residences");
    	assertEquals("expected Residences", cat, expect);
    }
    

    @Test
    public void testReduceDuplicatesEmpty() {
        Map<PointOfInterest, List<PointOfInterest>> deduplicated = Mapping.reduceDuplicates(Collections.emptyList());
        assertTrue("expected empty map", deduplicated.isEmpty());
    }

    /*
     * Warning: all the tests you write here must be runnable against any
     * Mapping class that follows the spec. It will be run against several staff
     * implementations of Mapping, which will be done by overwriting
     * (temporarily) your version of Mapping with the staff's version.
     * 
     * DO NOT strengthen the spec of Mapping or its methods.
     * 
     * In particular, your test cases must not call helper methods of your own
     * that you have put in Mapping, because that means you're testing a
     * stronger spec than Mapping says. If you need such helper methods, define
     * them in a different class. If you only need them in this test class, then
     * keep them in this test class.
     */
}
