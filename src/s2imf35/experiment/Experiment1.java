package s2imf35.experiment;

import s2imf35.*;
import s2imf35.graph.ParityGame;
import s2imf35.strategies.AbstractLiftingStrategy;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * Solution for exercise one of part II.
 */
public class Experiment1 extends AbstractExperiment {
    /**
     * Run the formulas within the experiment and evaluate the results.
     *
     * @param argMap The arguments given for the experiment.
     * @throws IOException Thrown when the file cannot be found or read.
     */
    @Override
    public void run(Map<String, Object> argMap) throws IOException {
        // Print an identifiable header for the test group.
        printHeader("Dining Philosophers");

        // Gather the optional parameters.
        int strategyId = (Integer) argMap.getOrDefault("-strategy", 0);
        boolean verbose = (Boolean) argMap.getOrDefault("-steps", false);
        boolean validate = (Boolean) argMap.getOrDefault("-validate", false);

        // Gather the files for the experiment.
        String rootPath = "inputs/experiment1/";
        File[] files = new File(rootPath).listFiles();
        List<String> gameNames = getParityGameNames(files);
        gameNames.sort(Comparator.comparing(
                o -> Integer.parseInt(o.split("\\.")[0].split("_")[1])
        ));

        // Track all the found performance metrics.
        HashMap<String, PerformanceCounter> metrics = new HashMap<>();

        // Do we want a validator?
        BiConsumer<ParityGame, Set<Integer>> validator = null;
        if(validate) {
            validator = (G, s) -> {
                try {
                    Validator.validate(G, 1, s);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            };
        }

        // Run the tests.
        runAll(strategyId, rootPath, verbose, gameNames, metrics, validator);
    }
}
