package s2imf35.graph;

import java.util.Arrays;
import java.util.List;

public class NodeSpecification {
    public final int identifier;
    public final int priority;
    public final Owner owner;
    public final int[] successors;
    public int[] predecessors;
    public final String name;

    public NodeSpecification(String line) throws NumberFormatException {
        String[] data = line.split("\\s+");

        identifier = Integer.parseInt(data[0]);
        priority = Integer.parseInt(data[1]);
        owner = Owner.get(data[2]);
        successors = Arrays.stream(data[3].split(",")).mapToInt(Integer::parseInt).toArray();
        name = data.length == 5 ? data[4].substring(1, data[4].length() - 1) : null;
    }

    public enum Owner {
        Diamond, Box;

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

    public void setPredecessors(List<Integer> predecessors) {
        this.predecessors = predecessors.stream().mapToInt(e -> e).toArray();
    }
}
