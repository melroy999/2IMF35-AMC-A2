package s2imf35.graph;

import java.util.Arrays;

public class NodeSpecification {
    public final int identifier;
    public final int priority;
    public final Owner owner;
    public final int[] successors;
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
        Diamond(false), Box(true);

        Owner(boolean sign) {
            this.sign = sign;
        }

        private final boolean sign;

        public boolean sign() {
            return sign;
        }

        public static Owner get(String target) {
            return target.equals("0") ? Diamond : Box;
        }
    }
}
