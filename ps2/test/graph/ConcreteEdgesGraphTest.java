/* Copyright (c) 2015-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package graph;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * Tests for ConcreteEdgesGraph.
 * 
 * This class runs the GraphInstanceTest tests against ConcreteEdgesGraph, as
 * well as tests for that particular implementation.
 * 
 * Tests against the Graph spec should be in GraphInstanceTest.
 */
public class ConcreteEdgesGraphTest extends GraphInstanceTest {
    
    /*
     * Provide a ConcreteEdgesGraph for tests in GraphInstanceTest.
     */
    @Override public Graph<String> emptyInstance() {
        return new ConcreteEdgesGraph<>();
    }
    
    /*
     * Testing ConcreteEdgesGraph...
     */
    
    // Testing strategy for ConcreteEdgesGraph.toString()
    @Test
    public void testToString() {
    		Graph<String> graph = new ConcreteEdgesGraph<>();
    		graph.set("a", "b", 1);
    		graph.set("a", "c", 2);
    		assertEquals("Vertices: [a, b, c]\nEdges: [(a, b, 1), (a, c, 2)]", graph.toString());
    		graph.set("a", "b", 0);
    		assertEquals("Vertices: [a, b, c]\nEdges: [(a, c, 2)]", graph.toString());
    }
    
    /*
     * Testing Edge...
     */
    
    // Testing strategy for Edge
    @Test
    public void testEdgeFuncs() {
    		Edge<String> e = new Edge<>("a", "b", 1);
    		assertEquals("a", e.getSource());
    		assertEquals("b", e.getTarget());
    		assertEquals((Integer)1, e.getWeight());
    		assertEquals("(a, b, 1)", e.toString());
    }
    
}
