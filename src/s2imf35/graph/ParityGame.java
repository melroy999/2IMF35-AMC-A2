package s2imf35.graph;

import java.util.Arrays;

public class ParityGame {
    // The raw nodes read from the PGSolver format.
    private final NodeSpecification[] specifications;

    public ParityGame(String input) throws NumberFormatException {
        // Parse the input and split in the semicolon symbol, which terminates entries.
        String[] lines = Arrays.stream(input.split(";")).map(String::trim).toArray(String[]::new);

        // Find the maximum identifier and determine the total size of the specification array.
        int n = Integer.parseInt(lines[0].split("\\s+")[1]) + 1;
        specifications = new NodeSpecification[n];

        // Load the specification.
        for (int i = 1; i < lines.length; i++) {
            String line = lines[i];
            // Skip the empty line at the end.
            if (line.equals("")) continue;

            NodeSpecification spec = new NodeSpecification(line);
            specifications[spec.identifier] = spec;
        }
    }
}
