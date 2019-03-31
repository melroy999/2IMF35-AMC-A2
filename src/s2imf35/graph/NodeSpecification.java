package s2imf35.graph;

import java.util.Arrays;
import java.util.List;

/**
 * All the data present in the parity game for a single node.
 */
public class NodeSpecification {
    public final int identifier;
    public final int priority;
    public final Owner owner;
    public final String name;

    // The identifiers of the nodes that are successors and predecessors.
    public final int[] successors;
    public int[] predecessors;

    /**
     * Create a node specification, given a string to parse.
     *
     * @param line The string that contains the required data.
     * @throws NumberFormatException If the input string is malformed.
     */
    public NodeSpecification(String line) throws NumberFormatException {
        String[] data = line.split("\\s+");

        identifier = Integer.parseInt(data[0]);
        priority = Integer.parseInt(data[1]);
        owner = Owner.get(data[2]);
        successors = Arrays.stream(data[3].split(",")).mapToInt(Integer::parseInt).toArray();
        name = data.length == 5 ? data[4].substring(1, data[4].length() - 1) : null;
    }

    /**
     * An enum type that represents the different types of owners.
     */
    public enum Owner {
        Diamond, Box;

        /**
         * Get the owner that corresponds to the given target.
         *
         * @param target The symbol of the target.
         * @return Diamond if the symbol is a zero, otherwise Box.
         */
        public static Owner get(String target) {
            return target.equals("0") ? Diamond : Box;
        }
    }

    @Override
    public String toString() {
        return identifier +
                ", p=" + priority +
                ", owner=" + owner;
    }

    /**
     * Set the predecessors of the given node.
     *
     * @param predecessors The predecessors of the node.
     */
    public void setPredecessors(List<Integer> predecessors) {
        this.predecessors = predecessors.stream().mapToInt(e -> e).toArray();
    }
}
