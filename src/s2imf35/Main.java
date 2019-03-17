package s2imf35;

import s2imf35.graph.ParityGame;

import java.io.IOException;
import java.util.Set;

public class Main {
	/**
     * Main call to the program.
     *
     * @param args The arguments given to the program, which are described in further detail in the report.
     * @throws IOException Thrown when an input file cannot be found or read.
     */
    public static void main(String[] args) throws IOException {
		ParityGame game = Parser.parseParityGame("inputs/unitTests/slides.gm");
        Set<Integer> result = Solver.solve(game);
        for(int v : result) {
            System.out.println(v);
        }
    }
}