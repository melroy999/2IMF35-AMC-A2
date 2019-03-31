package s2imf35;

import s2imf35.graph.ParityGame;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;

/**
 * A class that handles the validation of the results given by our program, but cross referencing it against results obtained by PGSolver.
 */
public class Validator {
    /**
     * Validate the results of the given parity game against the results obtained by PGSolver.
     *
     * @param G The parity game to validate.
     * @param experiment The number of the experiment, to determine the appropriate folder.
     * @param result The results obtained by our algorithm.
     * @throws IOException If the PGSolver result file cannot be found.
     */
    public static void validate(ParityGame G, int experiment, Set<Integer> result) throws IOException {
        String contents = new String(Files.readAllBytes(
                Paths.get("maxified-solutions/experiment"+ experiment + "/"
                        + G.name.substring(0, G.name.lastIndexOf('.')) + ".txt"))
                , StandardCharsets.UTF_8
        );

        String target = contents.substring(contents.indexOf("{") + 1, contents.indexOf("}"));

        Set<Integer> answer = new HashSet<>(G.n);
        StringTokenizer st = new StringTokenizer(target, ",");
        while(st.hasMoreTokens()) {
            answer.add(Integer.parseInt(st.nextToken().trim()));
        }

        // Check whether the two arrays are equal.
        if(answer.equals(result)) {
            System.out.println("Cross validation with PGSolver solution has been done successfully: the result is valid.");
        } else {
            System.out.println("WARNING: Cross validation shows that the result is invalid.");
        }
    }
}
