package s2imf35.util;

import s2imf35.Parser;
import s2imf35.graph.ParityGame;
import s2imf35.strategies.AbstractLiftingStrategy;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

public class OutputLatexTable {
    private static final Map<String, String> legendMap = new HashMap<>();
    static {
        legendMap.put("input_order", "0");
        legendMap.put("random_order", "1");
        legendMap.put("input_order_rep", "2");
        legendMap.put("random_order_rep", "3");
        legendMap.put("predecessors", "4");
        legendMap.put("predecessors_rep", "5");
        legendMap.put("predecessors_diamond_first", "6");
        legendMap.put("predecessors_diamond_first_rep", "7");
    }

    public static void main(String[] args) throws IOException {

        // The strategies that we want in the graph.
        int[] strategyIds = {0, 1, 2, 3, 4, 5, 6, 7};

        // Get the strategy slugs.
        List<String> strategies = Arrays.stream(strategyIds).mapToObj(AbstractLiftingStrategy::getSlug).collect(Collectors.toList());

        outputUnitTest(strategies);
    }

    private static void outputUnitTest(List<String> strategies) throws IOException {

        // The names of the experiments to output.
        String[] experiments = {
                "test_1",
                "test_2",
                "test_3",
                "test_4",
                "test_5",
                "test_6",
                "test_7",
                "test_8",
        };

        StringBuilder builder = new StringBuilder();
        builder.append("\\begin{table}[]").append(System.lineSeparator());
        builder.append("\t\\centering").append(System.lineSeparator());
        builder.append("\t\\begin{tabular}{c|c|cccc}").append(System.lineSeparator());

        builder.append("\t\t\\rot{\\textsc{Experiment}} & \\rot{\\textsc{Strategy}} & \\multicolumn{4}{c}{\\textsc{Measures}} \\\\").append(System.lineSeparator());

        for (String experiment : experiments) {
            builder.append("\t\t\\hline\n\t\t\\multicolumn{6}{c}{}\\\\[-1em]\n\t\t\\hline\n");
            builder.append("\t\t\\multirow{").append(strategies.size()).append("}{*}{\\rot{\\textsc{").append(experiment).append("}}}");

            for(int i = 0; i < strategies.size(); i++) {
                String strategy = strategies.get(i);
                String contents = new String(Files.readAllBytes(Paths.get("data/unitTests/" + strategy + "/" + experiment + "_data.txt")), StandardCharsets.UTF_8);
                List<String> entries = Arrays.stream(contents.split("\\r?\\n")).filter(e -> !e.contains("tSkips") && !e.contains("duration")).map(e -> e.split("\t")[1]).collect(Collectors.toList());

                if(i != 0) {
                    builder.append("\t\t");
                }

                if(i == 1 || i == 3) {
                    double iterations = 0;
                    double lifts = 0;
                    double updates = 0;

                    for(int j = 1; j < 11; j++) {
                        String contents2 = new String(Files.readAllBytes(Paths.get("data/unitTests/" + strategy + "/run" + j + "/" + experiment + "_data.txt")), StandardCharsets.UTF_8);
                        for(String v : contents2.split("\\r?\\n")) {
                            if(v.startsWith("i")) {
                                iterations += Double.parseDouble(v.split("\t")[1]);
                            }
                            if(v.startsWith("lifted")) {
                                lifts += Double.parseDouble(v.split("\t")[1]);
                            }
                            if(v.startsWith("updated")) {
                                updates += Double.parseDouble(v.split("\t")[1]);
                            }
                        }
                    }

                    iterations /= 10;
                    lifts /= 10;
                    updates /= 10;

                    builder.append(" & ").append(legendMap.get(strategy))
                            .append(" & ").append((String.format("%.1f", iterations)))
                            .append(" & ").append((String.format("%.1f", lifts)))
                            .append(" & ").append((String.format("%.1f", updates)))
                            .append(" & ").append(String.format("%.4f", updates / lifts)).append("\\\\").append(System.lineSeparator());
                } else {
                    builder.append(" & ").append(legendMap.get(strategy)).append(" & ").append(entries.get(0))
                            .append(" & ").append(entries.get(1))
                            .append(" & ").append(entries.get(2))
                            .append(" & ").append(String.format("%.4f", Double.parseDouble(entries.get(3)))).append("\\\\").append(System.lineSeparator());
                }
            }
        }

        builder.append("\t\\end{tabular}").append(System.lineSeparator());
        builder.append("\\end{table}").append(System.lineSeparator());

        System.out.println(builder.toString().replace("_", " "));
    }
}
