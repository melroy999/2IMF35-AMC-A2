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
 * Solution for exercise three of part II.
 */
public class Experiment3 extends AbstractExperiment {
    /**
     * Run the formulas within the experiment and evaluate the results.
     *
     * @param argMap The arguments given for the experiment.
     * @throws IOException Thrown when the file cannot be found or read.
     */
    @Override
    public HashMap<String, PerformanceCounter> run(Map<String, Object> argMap) throws IOException {
        // Print an identifiable header for the test group.
        printHeader("Elevator");

        // Gather the optional parameters.
        boolean validate = (boolean) argMap.getOrDefault("-validate", false);

        // Gather the files for the experiment.
        String rootPath = "inputs/experiment3/";
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
                    Validator.validate(G, 3, s);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            };
        }

        // Run the tests.
        runAll(rootPath, gameNames, argMap, metrics, validator);

        return metrics;
    }

    /**
     * Run the formulas within the experiment and evaluate the results. The experiment is repeated n times.
     *
     * @param argMap The arguments given for the experiment.
     * @param n The number of times to repeat the experiment.
     * @throws IOException Thrown when the file cannot be found or read.
     */
    @Override
    public void runMulti(Map<String, Object> argMap, int n) throws IOException {
        int strategyId = (int) argMap.getOrDefault("-strategy", 0);
        String strategy = AbstractLiftingStrategy.getSlug(strategyId);

        // Create a collection of metrics over all the turns.
        List<HashMap<String, PerformanceCounter>> metrics = new ArrayList<>();

        for(int i = 0; i < n; i++) {
            HashMap<String, PerformanceCounter> metric = new Experiment3().run(argMap);
            metrics.add(metric);
        }

        // The folder to output the files to.
        String folder = "data/experiment3/" + strategy;
        Path path = Paths.get(folder);
        if(Files.notExists(path)) {
            Files.createDirectories(path);
        }

        // A mapping that will contain the summation of all obtained results.
        Map<String, Map<String, Map<Integer, BigDecimal>>> results = new HashMap<>();

        // Create separate folders for all the runs.
        for(int i = 0; i < n; i++) {
            String experimentFolder = "data/experiment3/" + strategy + "/run" + (i + 1);
            Path experimentPath = Paths.get(experimentFolder);
            if(Files.notExists(experimentPath)) {
                Files.createDirectories(experimentPath);
            }

            // Output the metrics for the given experiment iteration to the folder.
            outputMetric(metrics.get(i), experimentFolder, results);
        }

        // Output the averages for all of the tests.
        for(String experiment : results.keySet()) {
            Map<String, Map<Integer, BigDecimal>> data = results.get(experiment);

            // Gather the results and output them.
            Map<Integer, Long> i_counts = new LinkedHashMap<>();
            Map<Integer, Long> tSkip_counts = new LinkedHashMap<>();
            Map<Integer, Long> lifted_counts = new LinkedHashMap<>();
            Map<Integer, Long> updated_counts = new LinkedHashMap<>();
            Map<Integer, Long> duration = new LinkedHashMap<>();
            Map<Integer, Double> lift_update_ratio = new LinkedHashMap<>();

            for(Integer x : data.get("_i_count").keySet()) {
                i_counts.put(x, data.get("_i_count").get(x).divide(BigDecimal.valueOf(n), RoundingMode.DOWN).longValue());
                tSkip_counts.put(x, data.get("_tskip_count").get(x).divide(BigDecimal.valueOf(n), RoundingMode.DOWN).longValue());
                lifted_counts.put(x, data.get("_lifted_count").get(x).divide(BigDecimal.valueOf(n), RoundingMode.DOWN).longValue());
                updated_counts.put(x, data.get("_updated_count").get(x).divide(BigDecimal.valueOf(n), RoundingMode.DOWN).longValue());
                lift_update_ratio.put(x, data.get("_updated_count").get(x).divide(data.get("_lifted_count").get(x), 4, RoundingMode.HALF_UP).doubleValue());
                duration.put(x, data.get("_duration").get(x).divide(BigDecimal.valueOf(n), RoundingMode.UP).longValue());
            }

            // Output all the files.
            outputToFile(folder, experiment + "_i_count", i_counts);
            outputToFile(folder, experiment + "_tskip_count", tSkip_counts);
            outputToFile(folder, experiment + "_lifted_count", lifted_counts);
            outputToFile(folder, experiment + "_updated_count", updated_counts);
            outputToFile(folder, experiment + "_lift_update_ratio", lift_update_ratio);
            outputToFile(folder, experiment + "_duration", duration);
        }
    }

    private void outputMetric(Map<String, PerformanceCounter> metric, String folder, Map<String, Map<String, Map<Integer, BigDecimal>>> results) {
        Set<String> files = metric.keySet();

        List<Integer> xValues = files.stream().map(
                e -> Integer.parseInt(e.split("\\.")[0].split("_")[1])
        ).distinct().sorted().collect(Collectors.toList());

        List<String> experiments = files.stream().map(
                e -> e.split("_")[0]
        ).distinct().sorted().collect(Collectors.toList());

        for(String experiment : experiments) {
            Map<Integer, Long> i_counts = new LinkedHashMap<>();
            Map<Integer, Long> tSkip_counts = new LinkedHashMap<>();
            Map<Integer, Long> lifted_counts = new LinkedHashMap<>();
            Map<Integer, Long> updated_counts = new LinkedHashMap<>();
            Map<Integer, Long> duration = new LinkedHashMap<>();
            Map<Integer, Double> lift_update_ratio = new LinkedHashMap<>();

            for(int x : xValues) {
                PerformanceCounter counter = metric.get(experiment + "_" + x + ".gm");

                if(counter == null) continue;

                i_counts.put(x, counter.i);
                tSkip_counts.put(x, counter.tSkips);
                lifted_counts.put(x, counter.lifted);
                updated_counts.put(x, counter.updated);
                duration.put(x, counter.duration);
                lift_update_ratio.put(x, (double) counter.updated / counter.lifted);

                // Also update the results mapping.
                Map<String, Map<Integer, BigDecimal>> acc = results.getOrDefault(experiment, new LinkedHashMap<>());

                Map<Integer, BigDecimal> i_counts1 = acc.computeIfAbsent("_i_count", e -> new LinkedHashMap<>());
                i_counts1.put(x, i_counts1.getOrDefault(x, BigDecimal.ZERO).add(BigDecimal.valueOf(counter.i)));
                acc.put("_i_count", i_counts1);

                Map<Integer, BigDecimal> tSkip_counts1 = acc.computeIfAbsent("_tskip_count", e -> new LinkedHashMap<>());
                tSkip_counts1.put(x, tSkip_counts1.getOrDefault(x, BigDecimal.ZERO).add(BigDecimal.valueOf(counter.tSkips)));
                acc.put("_tskip_count", tSkip_counts1);

                Map<Integer, BigDecimal> lifted_counts1 = acc.computeIfAbsent("_lifted_count", e -> new LinkedHashMap<>());
                lifted_counts1.put(x, lifted_counts1.getOrDefault(x, BigDecimal.ZERO).add(BigDecimal.valueOf(counter.lifted)));
                acc.put("_lifted_count", lifted_counts1);

                Map<Integer, BigDecimal> updated_counts1 = acc.computeIfAbsent("_updated_count", e -> new LinkedHashMap<>());
                updated_counts1.put(x, updated_counts1.getOrDefault(x, BigDecimal.ZERO).add(BigDecimal.valueOf(counter.updated)));
                acc.put("_updated_count", updated_counts1);

                Map<Integer, BigDecimal> duration1 = acc.computeIfAbsent("_duration", e -> new LinkedHashMap<>());
                duration1.put(x, duration1.getOrDefault(x, BigDecimal.ZERO).add(BigDecimal.valueOf(counter.duration)));
                acc.put("_duration", duration1);

                results.put(experiment, acc);
            }

            // Output all the data to files.
            outputToFile(folder, experiment + "_i_count", i_counts);
            outputToFile(folder, experiment + "_tskip_count", tSkip_counts);
            outputToFile(folder, experiment + "_lifted_count", lifted_counts);
            outputToFile(folder, experiment + "_updated_count", updated_counts);
            outputToFile(folder, experiment + "_lift_update_ratio", lift_update_ratio);
            outputToFile(folder, experiment + "_duration", duration);
        }
    }
}
