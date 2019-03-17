package s2imf35.graph;

import java.util.*;

import static s2imf35.graph.NodeSpecification.*;
import static s2imf35.graph.NodeSpecification.Owner.*;

public class ParityGame {
    // The raw nodes read from the PGSolver format.
    private final NodeSpecification[] specifications;

    // The vertices belonging to the diamond player.
    private final Set<Integer> V_d;

    // The vertices belonging to the box player.
    private final Set<Integer> V_b;

    // The maximum priority d.
    private int d = Integer.MIN_VALUE;

    // Track which vertices belong to a certain priority.
    private final HashMap<Integer, Set<Integer>> priorityMap = new HashMap<>();

    // The maximum value T.
    private final ArrayList<Integer> T = new ArrayList<>();

    public ParityGame(String input) throws NumberFormatException {
        // Parse the input and split in the semicolon symbol, which terminates entries.
        String[] lines = Arrays.stream(input.split(";")).map(String::trim).toArray(String[]::new);

        // Find the maximum identifier and determine the total size of the specification array.
        int n = Integer.parseInt(lines[0].split("\\s+")[1]) + 1;
        specifications = new NodeSpecification[n];

        // Initialize the vertex lists.
        V_d = new HashSet<>();
        V_b = new HashSet<>();

        // Load the specification.
        for (int i = 1; i < lines.length; i++) {
            String line = lines[i];
            // Skip the empty line at the end.
            if (line.equals("")) continue;

            NodeSpecification spec = new NodeSpecification(line);
            specifications[spec.identifier] = spec;

            // Check which player the vertex belongs to.
            (spec.owner == Diamond ?  V_d : V_b).add(spec.identifier);

            // Update the max priority and priority lists.
            if(d < spec.priority) {
                d = spec.priority + 1;
            }
            priorityMap.computeIfAbsent(spec.priority, e -> new HashSet<>()).add(spec.identifier);
        }

        // Create the maximum value T.
        for(int i = 0; i < d; i++) {
            if(i % 2 == 0) {
                T.add(0);
            } else {
                T.add(priorityMap.getOrDefault(i, new HashSet<>()).size());
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
}
