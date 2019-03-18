package s2imf35;

import s2imf35.graph.ParityGame;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;

public class Validator {
    public static void validate(ParityGame G, int experiment, Set<Integer> result) throws IOException {
        String contents = new String(Files.readAllBytes(
                Paths.get("maxified-solutions/experiment"+ experiment + "/"
                        + G.name.substring(0, G.name.lastIndexOf('.')) + ".txt"))
                , StandardCharsets.UTF_8
        );

        String target = contents.substring(contents.indexOf("{") + 1, contents.indexOf("}"));

        Set<Integer> answer = new HashSet<>(G.n);  // Or a more realistic size
        StringTokenizer st = new StringTokenizer(target, ",");
        while(st.hasMoreTokens()) {
            answer.add(Integer.parseInt(st.nextToken().trim()));
        }

        // Check whether the two arrays are equal.
        System.out.println("Valid result: " + answer.equals(result));
    }
}
