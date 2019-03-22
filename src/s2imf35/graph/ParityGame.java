package s2imf35.graph;

import java.util.*;

import static s2imf35.graph.NodeSpecification.*;
import static s2imf35.graph.NodeSpecification.Owner.*;

public class ParityGame {
    // The raw nodes read from the PGSolver format.
    private final NodeSpecification[] specifications;

    // The vertices belonging to the diamond player.
    private final Set<Integer> V_d;

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

    public ParityGame(String input, String name) throws NumberFormatException {
        // Set the name.
        this.name = name;

        // Parse the input and split in the semicolon symbol, which terminates entries.
        String[] lines = Arrays.stream(input.split(";")).map(String::trim).toArray(String[]::new);

        // Find the maximum identifier and determine the total size of the specification array.
        n = Integer.parseInt(lines[0].split("\\s+")[1]) + 1;
        specifications = new NodeSpecification[n];

        // Initialize the vertex lists.
        V_d = new HashSet<>();
        int d = Integer.MIN_VALUE;
        originalOrder = new int[n];

        // Load the specification.
        HashMap<Integer, Set<Integer>> priorityMap = new HashMap<>();
        for (int i = 1; i < lines.length; i++) {
            String line = lines[i];
            // Skip the empty line at the end.
            if (line.equals("")) continue;

            NodeSpecification spec = new NodeSpecification(line);
            specifications[spec.identifier] = spec;

            // Check which player the vertex belongs to.
            if(spec.owner == Diamond) {
                V_d.add(spec.identifier);
            }

            // Update the max priority and priority lists.
            if(d < spec.priority) {
                d = spec.priority;
            }
            priorityMap.computeIfAbsent(spec.priority, e -> new HashSet<>()).add(spec.identifier);

            // Make sure that we track the original order of the vertices.
            originalOrder[i - 1] = spec.identifier;
        }
        this.d = d + 1;

        // Create the maximum value T.
        M = new int[this.d];
        for(int i = 0; i < this.d; i++) {
            if(i % 2 != 0) {
                M[i] = priorityMap.getOrDefault(i, new HashSet<>()).size();
            }
        }
    }

    public Owner getOwner(int v) {
        return V_d.contains(v) ? Diamond : Box;
    }

    public int[] getSuccessors(int v) {
        return specifications[v].successors;
    }

    public int getPriority(int v) {
        return specifications[v].priority;
    }

    public String getName(int v) {
        return specifications[v].name;
    }
}
