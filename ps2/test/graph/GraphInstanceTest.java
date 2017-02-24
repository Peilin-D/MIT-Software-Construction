/* Copyright (c) 2015-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package graph;

import static org.junit.Assert.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

/**
 * Tests for instance methods of Graph.
 * 
 * <p>PS2 instructions: you MUST NOT add constructors, fields, or non-@Test
 * methods to this class, or change the spec of {@link #emptyInstance()}.
 * Your tests MUST only obtain Graph instances by calling emptyInstance().
 * Your tests MUST NOT refer to specific concrete implementations.
 */
public abstract class GraphInstanceTest {
    
    // Testing strategy
	@Test
	public void testAddVertices() {
		Graph<String> graph = emptyInstance();
		Set<String> set = new HashSet<>();
		set.add("a");
		set.add("b");
		set.add("c");
		graph.add("a");
		graph.add("b");
		graph.set("a", "c", 1);
		assertEquals(set, graph.vertices());
	}
    
	@Test
	public void testRemoveVertices() {
		Graph<String> graph = emptyInstance();
		graph.add("a");
		graph.remove("a");
		assertEquals("expected current graph to have no vertices",
                Collections.emptySet(), emptyInstance().vertices());
	}
	
	@Test
	public void testRemoveSourceVertex() {
		Graph<String> graph = emptyInstance();
		graph.set("a", "b", 1);
		graph.set("a", "c", 2);
		graph.remove("a");
		assertEquals(Collections.emptyMap(), graph.sources("b"));
		assertEquals(Collections.emptyMap(), graph.sources("c"));
	}
	
	@Test
	public void testRemoveTargetVertex() {
		Graph<String> graph = emptyInstance();
		graph.set("a", "b", 1);
		graph.set("c", "b", 2);
		graph.remove("b");
		assertEquals(Collections.emptyMap(), graph.targets("a"));
		assertEquals(Collections.emptyMap(), graph.targets("c"));
	}
	
	@Test
	public void testSetEdgesTargets() {
		Graph<String> graph = emptyInstance();
		Map<String, Integer> map = new HashMap<>();
		map.put("b", 1);
		map.put("c", 2);
		graph.set("a", "b", 1);
		graph.set("a", "c", 2);
		assertEquals(map, graph.targets("a"));
	}
	
	@Test
	public void testSetEdgesSources() {
		Graph<String> graph = emptyInstance();
		Map<String, Integer> map = new HashMap<>();
		map.put("b", 1);
		map.put("c", 2);
		graph.set("b", "a", 1);
		graph.set("c", "a", 2);
		assertEquals(map, graph.sources("a"));
	}
	
	@Test
	public void testSetEdgeWeightZero() {
		Graph<String> graph = emptyInstance();
		Set<String> set = new HashSet<>();
		set.add("a");
		set.add("b");
		set.add("c");
		graph.set("a", "b", 1);
		graph.set("c", "b", 2);
		graph.set("a", "b", 0);
		assertEquals(set, graph.vertices());
		assertFalse(graph.sources("b").containsKey("a"));
		assertTrue(graph.sources("b").containsKey("c"));
	}
	
	
    /**
     * Overridden by implementation-specific test classes.
     * 
     * @return a new empty graph of the particular implementation being tested
     */
    public abstract Graph<String> emptyInstance();
    
    @Test(expected=AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; // make sure assertions are enabled with VM argument: -ea
    }
    
    @Test
    public void testInitialVerticesEmpty() {
        // TODO you may use, change, or remove this test
        assertEquals("expected new graph to have no vertices",
                Collections.emptySet(), emptyInstance().vertices());
    }
    
    // TODO other tests for instance methods of Graph
    
}
