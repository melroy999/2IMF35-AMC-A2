package s2imf35.experiment;

import s2imf35.PerformanceCounter;
import s2imf35.Validator;
import s2imf35.graph.ParityGame;
import s2imf35.strategies.AbstractLiftingStrategy;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

/**
 * Solution for the dining philosophers exercise of part II.
 */
public class UnitTests extends AbstractExperiment {
    /**
     * Run the formulas within the experiment and evaluate the results.
     *
     * @param argMap The arguments given for the experiment.
     * @throws IOException Thrown when the file cannot be found or read.
     */
    @Override
    public HashMap<String, PerformanceCounter> run(Map<String, Object> argMap) throws IOException {
        // Print an identifiable header for the test group.
        printHeader("Unit Tests");

        // Gather the files for the experiment.
        String rootPath = "inputs/unitTests/";
        File[] files = new File(rootPath).listFiles();
        List<String> gameNames = getParityGameNames(files);
        gameNames.sort(Comparator.comparing(
                o -> Integer.parseInt(o.split("\\.")[0].split("_")[1])
        ));

        // Track all the found performance metrics.
        HashMap<String, PerformanceCounter> metrics = new HashMap<>();

        // Run the tests.
        runAll(rootPath, gameNames, argMap, metrics, null);

        return metrics;
    }

    /**
     * Run the formulas within the experiment and evaluate the results. The experiment is repeated n times.
     *
     * @param argMap The arguments given for the experiment.
     * @param n The number of times to repeat the experiment.
     * @throws IOException Thrown when the file cannot be found or read.
     */
    @SuppressWarnings("Duplicates")
    @Override
    public void runMulti(Map<String, Object> argMap, int n) throws IOException {

        int strategyId = (int) argMap.getOrDefault("-strategy", 0);
        String strategy = AbstractLiftingStrategy.getSlug(strategyId);

        // Create a collection of metrics over all the turns.
        List<HashMap<String, PerformanceCounter>> metrics = new ArrayList<>();

        // Given that we repeat the trial n times, we want different seeds on every iteration.
        Random random = new Random(0);

        for(int i = 0; i < n; i++) {
            argMap.put("-seed", random.nextLong());

            HashMap<String, PerformanceCounter> metric = new UnitTests().run(argMap);
            metrics.add(metric);
        }

        // The folder to output the files to.
        String folder = "data/unitTests/" + strategy;
        Path path = Paths.get(folder);
        if(Files.notExists(path)) {
            Files.createDirectories(path);
        }

        // A mapping that will contain the summation of all obtained results.
        Map<String, Map<String, BigDecimal>> results = new HashMap<>();

        // Create separate folders for all the runs.
        for(int i = 0; i < n; i++) {
            String experimentFolder = "data/unitTests/" + strategy + "/run" + (i + 1);
            Path experimentPath = Paths.get(experimentFolder);
            if(Files.notExists(experimentPath)) {
                Files.createDirectories(experimentPath);
            }

            // Output the metrics for the given experiment iteration to the folder.
            outputMetric(metrics.get(i), experimentFolder, results);
        }

        // Output the averages for all of the tests.
        for(String experiment : results.keySet()) {
            Map<String, BigDecimal> data = results.get(experiment);

            Map<String, Object> output = new LinkedHashMap<>();

            output.put("i", data.get("i").divide(BigDecimal.valueOf(n), RoundingMode.DOWN).longValue());
            output.put("tSkips", data.get("tSkips").divide(BigDecimal.valueOf(n), RoundingMode.DOWN).longValue());
            output.put("lifted", data.get("lifted").divide(BigDecimal.valueOf(n), RoundingMode.DOWN).longValue());
            output.put("updated", data.get("updated").divide(BigDecimal.valueOf(n), RoundingMode.DOWN).longValue());
            output.put("duration", data.get("duration").divide(BigDecimal.valueOf(n), RoundingMode.UP).longValue());
            output.put("ratio", data.get("updated").divide(data.get("lifted"), 4, RoundingMode.HALF_UP).doubleValue());

            // Output all the files.
            outputToFile(folder, experiment + "_data", output);
        }
    }

    /**
     * Output and accumulate the performance metric data entries for batch runs.
     *
     * @param metric The found performance metrics.
     * @param folder The folder to output the performance data to.
     * @param results The accumulated results, which will be used to calculate averages.
     */
    private void outputMetric(Map<String, PerformanceCounter> metric, String folder, Map<String, Map<String, BigDecimal>> results) {
        Set<String> files = metric.keySet();

        List<String> experiments = files.stream().map(
                e -> e.split("\\.")[0]
        ).distinct().sorted().collect(Collectors.toList());

        for(String experiment : experiments) {
            Map<String, Long> data = new LinkedHashMap<>();

            PerformanceCounter counter = metric.get(experiment + ".gm");

            data.put("i", counter.i);
            data.put("tSkips", counter.tSkips);
            data.put("lifted", counter.lifted);
            data.put("updated", counter.updated);
            data.put("duration", counter.duration);

            // Also update the results mapping.
            Map<String, BigDecimal> data2 = results.computeIfAbsent(experiment, e -> new LinkedHashMap<>());

            data2.put("i", data2.getOrDefault("i", BigDecimal.ZERO).add(BigDecimal.valueOf(counter.i)));
            data2.put("tSkips", data2.getOrDefault("tSkips", BigDecimal.ZERO).add(BigDecimal.valueOf(counter.tSkips)));
            data2.put("lifted", data2.getOrDefault("lifted", BigDecimal.ZERO).add(BigDecimal.valueOf(counter.lifted)));
            data2.put("updated", data2.getOrDefault("updated", BigDecimal.ZERO).add(BigDecimal.valueOf(counter.updated)));
            data2.put("duration", data2.getOrDefault("duration", BigDecimal.ZERO).add(BigDecimal.valueOf(counter.duration)));

            results.put(experiment, data2);

            // Output all the data to files.
            outputToFile(folder, experiment + "_data", data);
        }
    }
}
