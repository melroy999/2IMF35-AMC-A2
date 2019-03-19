package s2imf35;

import s2imf35.experiment.Experiment1;
import s2imf35.experiment.Experiment2;
import s2imf35.experiment.Experiment3;
import s2imf35.graph.ParityGame;
import s2imf35.solver.LinearSolver;
import s2imf35.solver.Solver;
import s2imf35.strategies.AbstractLiftingStrategy;
import s2imf35.strategies.FixedLiftingStrategy;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Main {

	/**
     * Main call to the program.
     *
     * @param args The arguments given to the program, which are described in further detail in the report.
     * @throws IOException Thrown when an input file cannot be found or read.
     */
    public static void main(String[] args) throws IOException {
        // Create the argument mapping.
        Map<String, Object> argMap = parseArguments(args);

        // We output the unit test as our default.
        if(argMap.isEmpty()) {
            // TODO implement unit test.
            return;
        }

        // Check which tests to perform. Here, single inputs have priority over experiment/unit tests.
        if(argMap.containsKey("-game")) {
            run(argMap);
        } else {
            // Run the specified tests.
            for(String arg : argMap.keySet()) {
                switch (arg) {
                    case "-experiment1":
                        new Experiment1().run(argMap);
                        break;
                    case "-experiment2":
                        new Experiment2().run(argMap);
                        break;
                    case "-experiment3":
                        new Experiment3().run(argMap);
                        break;
                    case "-unit":
                        // TODO implement unit test.
                        break;
                }
            }
        }
    }

    /**
     * Run a single parity game following the preferences in the arguments.
     *
     * @param args The arguments of the run, which should always include at least a parity game path.
     * @throws IOException Thrown when an input file cannot be found or read.
     */
    private static void run(Map<String, Object> args) throws IOException {
        // Get the path of the parity game.
        String gameFile = (String) args.getOrDefault("-game", null);
        int strategyId = (Integer) args.getOrDefault("-strategy", 0);
        int seed = (Integer) args.getOrDefault("-seed", 0);

        // Check whether we have all the required parameters.
        if(gameFile == null) {
            System.out.println("No parity game file has been provided. The file can be targeted with the " +
                    "argument -game=\"<g_path>\".");
            return;
        }

        // Find the optional parameters.
        boolean verbose = (boolean) args.getOrDefault("-steps", false);
        boolean linear = (boolean) args.getOrDefault("-linear", false);

        // Everything is filled in. Call the solver with the correct configuration.
        ParityGame G = Parser.parseParityGame(gameFile);
        AbstractLiftingStrategy strategy = AbstractLiftingStrategy.get(G, strategyId, seed);

        // Report the file name for clarity.
        String[] temp = gameFile.split("/");
        System.out.println("Parity game: [" + temp[temp.length - 1] + "]");

        // Solve the parity game.
        Solution solution;
        if(linear) {
            solution = LinearSolver.solve(G, strategy);
        } else {
            solution = Solver.solve(G, verbose, strategy);
        }

        System.out.println(solution);
    }

    /**
     * Parse the array of arguments and convert it to a mapping for easy access.
     *
     * @param args The arguments to parse.
     * @return A mapping in which the value of each argument is stored.
     */
    private static Map<String, Object> parseArguments(String[] args) {
        Map<String, Object> data = new HashMap<>();

        // Read all of the arguments and parse to the desired format.
        for(String arg : args) {
            // Make the argument parser case insensitive.
            arg = arg.toLowerCase();

            if(arg.startsWith("-game")) {
                data.put("-game", arg.substring(arg.indexOf("=") + 1, arg.length()));
            } else if(arg.equals("-steps")) {
                data.put("-steps", true);
            }else if(arg.equals("-validate")) {
                data.put("-validate", true);
            } else if(arg.startsWith("-strategy")) {
                data.put("-strategy", Integer.parseInt((arg.substring(arg.indexOf("=") + 1, arg.length()))));
            } else if(arg.startsWith("-seed")) {
                data.put("-seed", Integer.parseInt((arg.substring(arg.indexOf("=") + 1, arg.length()))));
            } else if(arg.equals("-experiment1")) {
                data.put("-experiment1", true);
            } else if(arg.equals("-experiment2")) {
                data.put("-experiment2", true);
            } else if(arg.equals("-experiment3")) {
                data.put("-experiment3", true);
            } else if(arg.equals("-unit")) {
                data.put("-unit", true);
            } else if(arg.equals("-linear")) {
                data.put("-linear", true);
            } else if(arg.equals("-all")) {
                data.put("-unit", true);
                data.put("-experiment1", true);
                data.put("-experiment2", true);
                data.put("-experiment3", true);
            }
        }

        return data;
    }

//    public static void deo() throws IOException {
//		ParityGame G = Parser.parseParityGame("inputs/unitTests/slides.gm");
//
//        // Get the desired iterator type.
//        Set<Integer> result = Solver.solve(G, false, new FixedLiftingStrategy());
//        print(result);
//
//        ParityGame[] games = {
//            Parser.parseParityGame("inputs/experiment1/dining_2.invariantly_inevitably_eat.gm"),
//            Parser.parseParityGame("inputs/experiment1/dining_2.invariantly_possibly_eat.gm"),
//            Parser.parseParityGame("inputs/experiment1/dining_2.plato_infinitely_often_can_eat.gm"),
//            Parser.parseParityGame("inputs/experiment1/dining_2.invariantly_plato_starves.gm"),
//        };
//
//        for(ParityGame game : games) {
//            result = Solver.solve(game, false, new InputOrderLiftingStrategy(game));
//            Validator.validate(game, 1, result);
//            print(result);
//        }
//    }
//
//    private static void print(Set<Integer> data) {
//        // Select only the first 20 states in the solution.
//        Object[] first50States = data.stream().limit(20).toArray();
//        String list = Arrays.toString(first50States);
//
//        if(data.size() != first50States.length) {
//            list = list.substring(0, list.length() - 1) + ", ...]";
//        }
//
//        System.out.println(list);
//        System.out.println();
//    }
}