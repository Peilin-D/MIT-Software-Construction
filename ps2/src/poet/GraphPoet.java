/* Copyright (c) 2015-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package poet;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import graph.Graph;

/**
 * A graph-based poetry generator.
 * 
 * <p>GraphPoet is initialized with a corpus of text, which it uses to derive a
 * word affinity graph.
 * Vertices in the graph are words. Words are defined as non-empty
 * case-insensitive strings of non-space non-newline characters. They are
 * delimited in the corpus by spaces, newlines, or the ends of the file.
 * Edges in the graph count adjacencies: the number of times "w1" is followed by
 * "w2" in the corpus is the weight of the edge from w1 to w2.
 * 
 * <p>For example, given this corpus:
 * <pre>    Hello, HELLO, hello, goodbye!    </pre>
 * <p>the graph would contain two edges:
 * <ul><li> ("hello,") -> ("hello,")   with weight 2
 *     <li> ("hello,") -> ("goodbye!") with weight 1 </ul>
 * <p>where the vertices represent case-insensitive {@code "hello,"} and
 * {@code "goodbye!"}.
 * 
 * <p>Given an input string, GraphPoet generates a poem by attempting to
 * insert a bridge word between every adjacent pair of words in the input.
 * The bridge word between input words "w1" and "w2" will be some "b" such that
 * w1 -> b -> w2 is a two-edge-long path with maximum-weight weight among all
 * the two-edge-long paths from w1 to w2 in the affinity graph.
 * If there are no such paths, no bridge word is inserted.
 * In the output poem, input words retain their original case, while bridge
 * words are lower case. The whitespace between every word in the poem is a
 * single space.
 * 
 * <p>For example, given this corpus:
 * <pre>    This is a test of the Mugar Omni Theater sound system.    </pre>
 * <p>on this input:
 * <pre>    Test the system.    </pre>
 * <p>the output poem would be:
 * <pre>    Test of the system.    </pre>
 * 
 * <p>PS2 instructions: this is a required ADT class, and you MUST NOT weaken
 * the required specifications. However, you MAY strengthen the specifications
 * and you MAY add additional methods.
 * You MUST use Graph in your rep, but otherwise the implementation of this
 * class is up to you.
 */
public class GraphPoet {
    
    private final Graph<String> graph = Graph.empty();
    
    // Abstraction function:
    //   TODO
    // Representation invariant:
    //   TODO
    // Safety from rep exposure:
    //   TODO
    
    /**
     * Create a new poet with the graph from corpus (as described above).
     * 
     * @param corpus text file from which to derive the poet's affinity graph
     * @throws IOException if the corpus file cannot be found or read
     */
    public GraphPoet(File corpus) throws IOException {
        Scanner sc = new Scanner(corpus);
        sc.useDelimiter("\\s|[.,]");
        List<String> words = new ArrayList<>();
        while (sc.hasNext()) {
        	String w = sc.next();
        	if (w.length() > 0) {
        		words.add(w.toLowerCase());
        	}
        }
        sc.close();
        int i = 0;
        while (i + 1 < words.size()) {
        	String startWord = words.get(i);
        	String endWord = words.get(i + 1);
        	int count = 1;
        	i++;
        	while (i + 1 < words.size() && words.get(i + 1).equals(endWord)) {
        		count++;
        		i++;
        	}
        	graph.set(startWord, endWord, count);
        }
    }
    
    // TODO checkRep
    
    /**
     * Generate a poem.
     * 
     * @param input string from which to create the poem
     * @return poem (as described above)
     */
	public String poem(String input) {
        if (input.length() == 0 || graph.vertices().size() == 0) {
        	return "";
        }
        Scanner sc = new Scanner(input);
        sc.useDelimiter("\\s");
        List<String> words = new ArrayList<>();
        while (sc.hasNext()) {
        	String w = sc.next();
        	if (w.length() > 0) {
        		words.add(w);
        	}
        }
        sc.close();
        StringBuilder result_poem = new StringBuilder();
        int i = 0;
        while (i + 1 < words.size()) {
        	String w1 = words.get(i);
        	String w2 = words.get(i + 1);
        	// find bridge word b for w1 -> b -> w2
        	String bridge = "";
        	int maxWeight = 0;
        	// first get all targets of w1
        	Map<String, Integer> targets_w1 = graph.targets(w1.toLowerCase());
        	// then see if w2 is in the targets of w1's targets
        	for (String b : targets_w1.keySet()) {
        		Map<String, Integer> targets_b = graph.targets(b);
        		int weight_w1_b = targets_w1.get(b);
        		for (String s : targets_b.keySet()) {
        			// if there's a two-edge-long path with better weight, update brigde word
        			if (s.equals(w2.toLowerCase()) && weight_w1_b + targets_b.get(s) > maxWeight) {
        				maxWeight = weight_w1_b + targets_b.get(s);
        				bridge = b;
        				break;
        			}
        		}
        	}
        	if (bridge.length() > 0) {
	        	result_poem.append(w1 + " " + bridge + " ");
        	} else {
        		result_poem.append(w1 +  " ");
        	}
        	i++;
        }
        result_poem.append(words.get(i));
        return result_poem.toString().trim();
    }
    
    @Override
    public String toString() {
    	return graph.toString();
    }
    
}
