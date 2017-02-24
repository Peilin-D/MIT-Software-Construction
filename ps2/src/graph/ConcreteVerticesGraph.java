/* Copyright (c) 2015-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * An implementation of Graph.
 * 
 * <p>
 * PS2 instructions: you MUST use the provided rep.
 */
public class ConcreteVerticesGraph<L> implements Graph<L> {

	private final List<Vertex<L>> vertices = new ArrayList<>();

	// Abstraction function:
	// TODO
	// Representation invariant:
	// TODO
	// Safety from rep exposure:
	// TODO

	public ConcreteVerticesGraph() {}

	// TODO checkRep

	@Override
	public boolean add(L vertex) {
		Vertex<L> v = new Vertex<>(vertex);
		if (vertices.contains(v)) {
			return false;
		}
		vertices.add(v);
		return true;
	}

	@Override
	public int set(L source, L target, int weight) {
		Vertex<L> sourceVertex = new Vertex<>(source);
		Vertex<L> targetVertex = new Vertex<>(target);
		int sourceIndex = vertices.indexOf(sourceVertex);
		int targetIndex = vertices.indexOf(targetVertex);
		int prevWeight = 0;
		if (weight != 0) { // add or update
			if (sourceIndex == -1 && targetIndex == -1) { // add source and
															// target
				sourceVertex.addTarget(target, weight);
				vertices.add(sourceVertex);
				targetVertex.addSource(source, weight);
				vertices.add(targetVertex);
			} else if (sourceIndex == -1) { // add source
				sourceVertex.addTarget(target, weight);
				vertices.add(sourceVertex);
				vertices.get(targetIndex).addSource(source, weight);
			} else if (targetIndex == -1) { // add target
				targetVertex.addSource(source, weight);
				vertices.add(targetVertex);
				vertices.get(sourceIndex).addTarget(target, weight);
			} else { // both vertices already existed
				if (vertices.get(sourceIndex).getTargets().containsKey(target)) {
					// edge existed, record previous weight
					prevWeight = vertices.get(sourceIndex).getTargets().get(target);
				}
				vertices.get(sourceIndex).addTarget(target, weight);
				vertices.get(targetIndex).addSource(source, weight);
			}
		} else if (sourceIndex != -1 && targetIndex != -1 && vertices.get(sourceIndex).getTargets().containsKey(target)
				&& vertices.get(targetIndex).getSources().containsKey(source)) { // remove
																					// edge
			prevWeight = vertices.get(sourceIndex).getTargets().get(target);
			vertices.get(sourceIndex).getTargets().remove(target);
			vertices.get(targetIndex).getSources().remove(source);
		}
		return prevWeight;
	}

	@Override
	public boolean remove(L vertex) {
		Vertex<L> v = new Vertex<>(vertex);
		for (int i = 0; i < vertices.size(); i++) {
			if (vertices.get(i).equals(v)) {
				// remove from v's sources
				for (L source : vertices.get(i).getSources().keySet()) {
					int index = vertices.indexOf(new Vertex<>(source));
					vertices.get(index).getTargets().remove(vertex);
				}
				// remove from v's targets
				for (L target : vertices.get(i).getTargets().keySet()) {
					int index = vertices.indexOf(new Vertex<>(target));
					vertices.get(index).getSources().remove(vertex);
				}
				// this line is safe because the immediately following return
				vertices.remove(v);
				return true;
			}
		}
		return false;
	}

	@Override
	public Set<L> vertices() {
		Set<L> set = new HashSet<>();
		for (Vertex<L> v : vertices) {
			set.add(v.getLabel());
		}
		return set;
	}

	@Override
	public Map<L, Integer> sources(L target) {
		Vertex<L> targetVertex = new Vertex<>(target);
		Map<L, Integer> map = new HashMap<>();
		for (Vertex<L> v : vertices) {
			if (v.equals(targetVertex)) {
				map = v.getSources();
				break;
			}
		}
		return map;
	}

	@Override
	public Map<L, Integer> targets(L source) {
		Vertex<L> sourceVertex = new Vertex<>(source);
		Map<L, Integer> map = new HashMap<>();
		for (Vertex<L> v : vertices) {
			if (v.equals(sourceVertex)) {
				map = v.getTargets();
				break;
			}
		}
		return map;
	}

	// TODO toString()
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append('[');
		for (Vertex<L> v : vertices) {
			sb.append(v.toString() + ", ");
		}
		sb = sb.delete(sb.length() - 2, sb.length());
		sb.append(']');
		return sb.toString();
	}
}

/**
 * TODO specification Mutable. This class is internal to the rep of
 * ConcreteVerticesGraph.
 * 
 * <p>
 * PS2 instructions: the specification and implementation of this class is up to
 * you.
 */
class Vertex<L> {

	private L label;
	private Map<L, Integer> sources;
	private Map<L, Integer> targets;

	// Abstraction function:
	// A vertex is represented by its label, its sources and its targets as 2
	// sets respectively.
	// Representation invariant:
	// TODO
	// Safety from rep exposure:
	// TODO

	public Vertex(L label) {
		this.label = label;
		sources = new HashMap<>();
		targets = new HashMap<>();
	}

	// TODO checkRep

	public Map<L, Integer> getSources() {
		return sources;
	}

	public Map<L, Integer> getTargets() {
		return targets;
	}

	public L getLabel() {
		return label;
	}

	public void addSource(L source, Integer weight) {
		sources.put(source, weight);
	}

	public void addTarget(L target, Integer weight) {
		targets.put(target, weight);
	}

	public void setLabel(L label) {
		this.label = label;
	}

	@Override
	public boolean equals(Object otherObject) {
		if (!(otherObject instanceof Vertex)) {
			return false;
		}
		Vertex<L> otherVertex = (Vertex<L>) otherObject;
		return label == otherVertex.label;
	}

	@Override
	public String toString() {
		return '(' + label.toString() + ", " + getSources().entrySet() + ", " + getTargets().entrySet() + ')';
	}

}
