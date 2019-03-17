package s2imf35;

import s2imf35.graph.ParityGame;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * A class that parses the input files and converts them to the desired object type.
 */
public class Parser {
    /**
     * Parse the given parity game file and convert it to a parity game representation.
     *
     * @param path The location of the input file.
     * @return A parity game representing the graph defined in the input file.
     * @throws IOException If the file cannot be found or read.
     */
    public static ParityGame parseParityGame(String path) throws IOException {
        String contents = new String(Files.readAllBytes(Paths.get(path)), StandardCharsets.UTF_8);
        return new ParityGame(contents);
    }
}
