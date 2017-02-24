/* Copyright (c) 2015-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package graph;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * Tests for ConcreteVerticesGraph.
 * 
 * This class runs the GraphInstanceTest tests against ConcreteVerticesGraph, as
 * well as tests for that particular implementation.
 * 
 * Tests against the Graph spec should be in GraphInstanceTest.
 */
public class ConcreteVerticesGraphTest extends GraphInstanceTest {
    
    /*
     * Provide a ConcreteVerticesGraph for tests in GraphInstanceTest.
     */
    @Override public Graph<String> emptyInstance() {
        return new ConcreteVerticesGraph<>();
    }
    
    /*
     * Testing ConcreteVerticesGraph...
     */
    
    // Tests for ConcreteVerticesGraph.toString()
    @Test
    public void testGraphToString() {
    	Graph<String> graph = emptyInstance();
    	graph.set("a", "b", 1);
    	graph.set("b", "c", 2);
    	assertEquals("[(a, [], [b=1]), (b, [a=1], [c=2]), (c, [b=2], [])]", graph.toString());
    }
    
    
    /*
     * Testing Vertex...
     */
        
    // Tests for operations of Vertex
    @Test
    public void testVertexToString() {
    	Vertex<String> v = new Vertex<>("a");
    	v.addSource("b", 1);
    	v.addSource("c", 1);
    	v.addTarget("d", 1);
    	v.addTarget("e", 1);
    	assertEquals(v.toString(), "(a, [b=1, c=1], [d=1, e=1])");
    }
}
