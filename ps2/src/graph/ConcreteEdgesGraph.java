/* Copyright (c) 2015-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * An implementation of Graph.
 * 
 * <p>
 * PS2 instructions: you MUST use the provided rep.
 */
public class ConcreteEdgesGraph<L> implements Graph<L> {

	private final Set<L> vertices = new HashSet<>();
	private final List<Edge<L>> edges = new ArrayList<>();

	// Abstraction function:
	// Represent a graph using a set of vertices and a list of edges.

	// Representation invariant:
	// All vertices appeared in edges representation are in vertices set.

	// Safety from rep exposure:
	// TODO

	/**
	 * Default Constructor for graph
	 */
	public ConcreteEdgesGraph() {
	}

	/**
	 * Construct a graph
	 * 
	 * @param vertices
	 *            set of vertices
	 * @param edges
	 *            list of edges
	 */
	public ConcreteEdgesGraph(Set<L> vertices, List<Edge<L>> edges) {
		for (L v : vertices) {
			this.vertices.add(v);
		}
		for (Edge<L> e : edges) {
			this.edges.add(e);
		}
	};

	// checkRep
//	private void checkRep() {
//		assert true;
//	}

	@Override
	public boolean add(L vertex) {
		if (vertices.contains(vertex)) {
			return false;
		}
		vertices.add(vertex);
		return true;
	}

	@Override
	public int set(L source, L target, int weight) {
		int prevWeight = 0;
		if (weight != 0) {
			for (Edge<L> e : edges) {
				if (e.getSource().equals(source) && e.getTarget().equals(target)) {
					prevWeight = e.getWeight();
					edges.remove(e);
					break;
				}
			}
			vertices.add(source);
			vertices.add(target);
			edges.add(new Edge<>(source, target, weight));
		} else {
			for (Edge<L> e : edges) {
				if (e.getSource().equals(source) && e.getTarget().equals(target)) {
					edges.remove(e);
					prevWeight = e.getWeight();
					break;
				}
			}
		}
		return prevWeight;
	}

	@Override
	public boolean remove(L vertex) {
		if (vertices.contains(vertex)) {
			vertices.remove(vertex);
			Iterator<Edge<L>> iter = edges.iterator();
			while (iter.hasNext()) {
				Edge<L> e = iter.next();
				if (e.getSource().equals(vertex) || e.getTarget().equals(vertex)) {
					System.out.println(edges);
					iter.remove();
				}

			}
			return true;
		} else {
			return false;
		}
	}

	@Override
	public Set<L> vertices() {
		return vertices;
	}

	@Override
	public Map<L, Integer> sources(L target) {
		Map<L, Integer> map = new HashMap<>();
		for (Edge<L> e : edges) {
			if (e.getTarget().equals(target)) {
				map.put(e.getSource(), e.getWeight());
			}
		}
		return map;
	}

	@Override
	public Map<L, Integer> targets(L source) {
		Map<L, Integer> map = new HashMap<>();
		for (Edge<L> e : edges) {
			if (e.getSource().equals(source)) {
				map.put(e.getTarget(), e.getWeight());
			}
		}
		return map;
	}

	/**
	 * @return L representation of graph
	 */
	@Override
	public String toString() {
		StringBuilder rep = new StringBuilder();
		rep.append("Vertices: " + vertices.toString() + "\nEdges: [");
		int i = 0;
		for (; i < edges.size() - 1; i++) {
			rep.append(edges.get(i).toString() + ", ");
		}
		rep.append(edges.get(i).toString() + ']');
		return rep.toString();
	}

}

/**
 * TODO specification Immutable. This class is internal to the rep of
 * ConcreteEdgesGraph.
 * 
 * <p>
 * PS2 instructions: the specification and implementation of this class is up to
 * you.
 */
class Edge<L> {

	private final L source, target;
	private final Integer weight;

	// Abstraction function:
	// Represent a directed edge using 3 fields, source vertex, target vertex,
	// and a integer weight;

	// Representation invariant:
	// source and target are not null

	// Safety from rep exposure:
	// All fields are private and final and immutable

	/**
	 * Construct an edge
	 * 
	 * @param s
	 *            source vertex
	 * @param t
	 *            target vertex
	 * @param w
	 *            weight
	 */
	public Edge(L s, L t, Integer w) {
		source = s;
		target = t;
		weight = w;
	}

	// checkRep
	// private void checkRep() {
	// assert(!source.isEmpty() && !target.isEmpty());
	// }

	/**
	 * Get the source vertex of this edge
	 * 
	 * @return source vertex
	 */
	public L getSource() {
		return source;
	}

	/**
	 * Get the target vertex of this edge
	 * 
	 * @return target vertex
	 */
	public L getTarget() {
		return target;
	}

	/**
	 * Get the weight of this edge
	 * 
	 * @return edge weight
	 */
	public Integer getWeight() {
		return weight;
	}

	/**
	 * @return L representation of Edge
	 */
	@Override
	public String toString() {
		return '(' + source.toString() + ", " + target.toString() + ", " + weight.toString() + ')';
	}
}
