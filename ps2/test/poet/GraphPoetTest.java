/* Copyright (c) 2015-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package poet;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

/**
 * Tests for GraphPoet.
 */
public class GraphPoetTest {
    
    // Testing strategy
    // 1. empty graph
	// 2. empty input
	// 3. input with multiple two-edge-long paths 
    
    @Test(expected=AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; // make sure assertions are enabled with VM argument: -ea
    }
    
    @Test
    public void testEmptyGraph() throws IOException {
    	GraphPoet graph = new GraphPoet(new File("test/poet/empty_text.txt"));
    	assertEquals("", graph.poem("A non empty input"));
    }
    
    @Test
    public void testEmptyInput() throws IOException {
    	GraphPoet graph = new GraphPoet(new File("test/poet/text1.txt"));
    	assertEquals("", graph.poem(""));
    }
    
    @Test
    public void testMultiplePath() throws IOException {
    	GraphPoet graph = new GraphPoet(new File("test/poet/text1.txt"));
//    	System.out.println(graph.toString());
    	assertEquals("A b D", graph.poem("A D"));
    }
    
    @Test
    public void testLengthOneInput() throws IOException {
    	GraphPoet graph = new GraphPoet(new File("test/poet/text1.txt"));
    	assertEquals("A", graph.poem("A"));
    }
    
}
