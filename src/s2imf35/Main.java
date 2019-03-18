package s2imf35;

import s2imf35.graph.ParityGame;
import s2imf35.strategies.AbstractLiftingStrategy;
import s2imf35.strategies.FixedLiftingStrategy;
import s2imf35.strategies.InputOrderLiftingStrategy;

import java.io.IOException;
import java.util.Arrays;
import java.util.Set;

public class Main {
	/**
     * Main call to the program.
     *
     * @param args The arguments given to the program, which are described in further detail in the report.
     * @throws IOException Thrown when an input file cannot be found or read.
     */
    public static void main(String[] args) throws IOException {
		ParityGame G = Parser.parseParityGame("inputs/unitTests/slides.gm");

        // Get the desired iterator type.
        Set<Integer> result = Solver.solve(G, true, new FixedLiftingStrategy());
        print(result);

        ParityGame[] games = {
            Parser.parseParityGame("inputs/experiment1/dining_2.invariantly_inevitably_eat.gm"),
            Parser.parseParityGame("inputs/experiment1/dining_2.invariantly_possibly_eat.gm"),
            Parser.parseParityGame("inputs/experiment1/dining_2.plato_infinitely_often_can_eat.gm"),
            Parser.parseParityGame("inputs/experiment1/dining_2.invariantly_plato_starves.gm"),
        };

        for(ParityGame game : games) {
            result = Solver.solve(game, true, new InputOrderLiftingStrategy(game));
            print(result);
        }
    }

    private static void print(Set<Integer> data) {
        // Select only the first 20 states in the solution.
        Object[] first50States = data.stream().limit(20).toArray();
        String list = Arrays.toString(first50States);

        if(data.size() != first50States.length) {
            list = list.substring(0, list.length() - 1) + ", ...]";
        }

        System.out.println(list);
    }
}