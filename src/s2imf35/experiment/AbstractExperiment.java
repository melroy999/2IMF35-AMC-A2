package s2imf35.experiment;

import s2imf35.Parser;
import s2imf35.PerformanceCounter;
import s2imf35.Solution;
import s2imf35.solver.LinearSolver;
import s2imf35.solver.Solver;
import s2imf35.graph.ParityGame;
import s2imf35.strategies.AbstractLiftingStrategy;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import java.util.concurrent.*;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

/**
 * An abstract class representing an entire experiment in the exercise.
 */
public abstract class AbstractExperiment {
    /**
     * Run the formulas within the experiment and evaluate the results.
     *
     * @param argMap The arguments given for the experiment.
     * @throws IOException Thrown when the file cannot be found or read.
     */
    public abstract HashMap<String, PerformanceCounter> run(Map<String, Object> argMap) throws IOException;

    /**
     * Run the formulas within the experiment and evaluate the results. The experiment is repeated n times.
     *
     * @param argMap The arguments given for the experiment.
     * @param n The number of times to repeat the experiment.
     * @throws IOException Thrown when the file cannot be found or read.
     */
    public abstract void runMulti(Map<String, Object> argMap, int n) throws IOException;

    /**
     * Print a fancy header for the experiment.
     *
     * @param name The name of the experiment.
     */
    public void printHeader(String name) {
        int n = 56;
        String text = repeatText("=", n) + "\n"
                + "===" + repeatText(" ", (int) Math.floor((n - name.length()) / 2.0) - 3) + name.toUpperCase()
                + repeatText(" ", (int) Math.ceil((n - name.length()) / 2.0) - 3) + "===" + "\n"
                + repeatText("=", n) + "\n";

        System.out.println(text);
    }

    /**
     * Repeat a given character for the given number of times.
     *
     * @param symbol The symbol to repeat.
     * @param n The number of repetitions.
     * @return The string symbol repeated n times.
     */
    private String repeatText(String symbol, int n) {
        return new String(new char[n]).replaceAll("\0", symbol);
    }

    List<String> getParityGameNames(File[] files) {
        return Arrays.stream(files).filter(e -> e.getName().endsWith(".gm"))
                .map(File::getName).collect(Collectors.toList());
    }

    void runAll(String rootPath, List<String> gameNames, Map<String, Object> argMap, HashMap<String, PerformanceCounter> metrics, BiConsumer<ParityGame, Set<Integer>> validator) throws IOException {

        int strategyId = (int) argMap.getOrDefault("-strategy", 0);
        boolean verbose = (boolean) argMap.getOrDefault("-steps", false);
        boolean linear = (boolean) argMap.getOrDefault("-linear", false);
        int seed = (Integer) argMap.getOrDefault("-seed", 0);
        int timeout = (Integer) argMap.getOrDefault("-timeout", -1);

        System.out.println(">>> Strategy: [" + AbstractLiftingStrategy.getName(strategyId) + "]" +
                ", seed = " + seed + ", linear = " + linear);
        System.out.println();


        for(String game : gameNames) {
            // Import the game.
            ParityGame G = Parser.parseParityGame(rootPath + game);
            System.out.println("Parity game: [" + game + "]");

            // Solve the game.
            Solution solution;

            if(timeout != -1) {

                // Create an executor service, which will be used to enforce timeouts.
                ExecutorService service = Executors.newSingleThreadExecutor();

                Future<Solution> task;

                if(linear) {
                    task = service.submit(() -> LinearSolver.solve(G, AbstractLiftingStrategy.get(G, strategyId, seed)));
                } else {
                    task = service.submit(() -> Solver.solve(G, verbose, AbstractLiftingStrategy.get(G, strategyId, seed)));
                }

                // Perform the check with a timeout.
                try {
                    solution = task.get(timeout, TimeUnit.SECONDS);
                } catch (InterruptedException | ExecutionException | TimeoutException e) {
                    solution = null;
                }

                // Do not forget to shut down the executor service.
                service.shutdownNow();

            } else {
                if(linear) {
                    solution = LinearSolver.solve(G, AbstractLiftingStrategy.get(G, strategyId, seed));
                } else {
                    solution = Solver.solve(G, verbose, AbstractLiftingStrategy.get(G, strategyId, seed));
                }
            }

            if(solution == null) {
                System.out.println("The calculation hit the timeout limit. Proceeding to next calculation.");
            } else {
                System.out.println(solution);
                System.out.println("Contains vertex with index 0: " + solution.V.contains(0));

                if(validator != null) {
                    validator.accept(G, solution.V);
                }

                metrics.put(game, solution.counter);
            }

            System.out.println();
        }
    }

    public <T> void outputToFile(String folder, String name, Map<Integer, T> data) {
        try (PrintWriter out = new PrintWriter(folder + "/" + name + ".txt")) {
            for(Map.Entry<Integer, T> entry : data.entrySet()) {
                out.println(entry.getKey() + "\t" + entry.getValue());
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
