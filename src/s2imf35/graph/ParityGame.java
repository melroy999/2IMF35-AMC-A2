package s2imf35.graph;

import java.util.*;

/**
 * Representation of a parity game graph.
 */
public class ParityGame {
    // The raw nodes read from the PGSolver format.
    private final NodeSpecification[] specifications;

    // The maximum priority d.
    public final int d;

    // The number of vertices.
    public final int n;

    // The maximum value in M.
    public final int[] M;

    // Get the original order of the vertices.
    public final int[] originalOrder;

    // A name which we can use to recognize the game.
    public final String name;

    // The number of edges in the parity game.
    private long noEdges = 0;

    /**
     * Create a parity game graph, given the contents of a file and the name of the graph.
     *
     * @param input The complete contents of the desired graph.
     * @param name The name given to the graph, which more often than not is the graph file name.
     * @throws NumberFormatException If the input is malformed.
     */
    public ParityGame(String input, String name) throws NumberFormatException {
        // Set the name.
        this.name = name;

        // Parse the input and split in the semicolon symbol, which terminates entries.
        String[] lines = Arrays.stream(input.split(";")).map(String::trim).toArray(String[]::new);

        // Find the maximum identifier and determine the total size of the specification array.
        n = Integer.parseInt(lines[0].split("\\s+")[1]) + 1;
        specifications = new NodeSpecification[n];

        // Initialize the vertex lists.
        int d = Integer.MIN_VALUE;
        originalOrder = new int[n];

        // Load the specification.
        HashMap<Integer, Set<Integer>> priorityMap = new HashMap<>();

        // Keep a predecessor list.
        List<List<Integer>> predecessors = new ArrayList<>();
        for(int i = 1; i < lines.length; i++) {
            predecessors.add(new ArrayList<>());
        }

        for (int i = 1; i < lines.length; i++) {
            String line = lines[i];
            // Skip the empty line at the end.
            if (line.equals("")) continue;

            NodeSpecification spec = new NodeSpecification(line);
            specifications[spec.identifier] = spec;

            // Update the max priority and priority lists.
            if(d < spec.priority) {
                d = spec.priority;
            }
            priorityMap.computeIfAbsent(spec.priority, e -> new HashSet<>()).add(spec.identifier);

            // Set the predecessors.
            for(int w : spec.successors) {
                predecessors.get(w).add(spec.identifier);
            }

            // Update the number of edges.
            noEdges += spec.successors.length;

            // Make sure that we track the original order of the vertices.
            originalOrder[i - 1] = spec.identifier;
        }
        this.d = d + 1;

        // Add the predecessors to the node specification.
        for(int i = 0; i < specifications.length; i++) {
            specifications[i].setPredecessors(predecessors.get(i));
        }

        // Create the maximum value T.
        M = new int[this.d];
        for(int i = 0; i < this.d; i++) {
            if(i % 2 != 0) {
                M[i] = priorityMap.getOrDefault(i, new HashSet<>()).size();
            }
        }
    }

    /**
     * Get the priority of the given node.
     *
     * @param v The node to get the priority of.
     * @return The priority p(v) of node v.
     */
    public int getPriority(int v) {
        return specifications[v].priority;
    }

    /**
     * Get the name of the given node.
     *
     * @param v The node to get the name of.
     * @return The name of node v, which might be null, since it is optional.
     */
    public String getName(int v) {
        return specifications[v].name;
    }

    /**
     * Get the node specification of the given node.
     *
     * @param v The node to get the specification of.
     * @return The full node specification object of the given node.
     */
    public NodeSpecification get(int v) {
        return specifications[v];
    }

    /**
     * Get the owner of the given node.
     *
     * @param v The node to get the owner of.
     * @return The owner o(v) of node v.
     */
    public NodeSpecification.Owner getOwner(int v) {
        return specifications[v].owner;
    }

    /**
     * Get the number of edges in the graph.
     *
     * @return The sum of all successor array lengths.
     */
    public long getNoEdges() {
        return noEdges;
    }
}
