package s2imf35.util;

import s2imf35.Parser;
import s2imf35.graph.ParityGame;
import s2imf35.strategies.AbstractLiftingStrategy;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.List;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

public class OutputLatexGraph {
    private static final Map<String, String> legendMap = new HashMap<>();
    static {
        legendMap.put("input_order", "input_order");
        legendMap.put("random_order", "random_order");
        legendMap.put("input_order_rep", "input_order_rep");
        legendMap.put("random_order_rep", "random_order_rep");
        legendMap.put("predecessors", "predecessors");
        legendMap.put("predecessors_rep", "predecessors_rep");
        legendMap.put("predecessors_diamond_first", "pred_diamond_first");
        legendMap.put("predecessors_diamond_first_rep", "pred_diamond_first_rep");
    }

    public static void main(String[] args) throws IOException {

        // The strategies that we want in the graph.
        int[] strategyIds = {0, 1, 2, 3, 4, 5, 6, 7};

        // Get the strategy slugs.
        List<String> strategies = Arrays.stream(strategyIds).mapToObj(AbstractLiftingStrategy::getSlug).collect(Collectors.toList());

        outputExperiment1(strategies);
        outputExperiment2(strategies);
        outputExperiment3(strategies);
    }

    private static void outputExperiment1(List<String> strategies) throws IOException {

        int minx = 2;
        int maxx = 11;

        String experiment = "experiment1";

        // The names of the experiments to output.
        String[] experiments = {
                "invariantly_inevitably_eat",
                "invariantly_plato_starves",
                "invariantly_possibly_eat",
                "plato_infinitely_often_can_eat"
        };

        for (String experiment1 : experiments) {
            System.out.println("\\begin{figure}");
            System.out.println("\\centering");

            String xLabel = "Number of Philosophers $(n)$";
            String yLabel = "Running Time [ms]";
            printDurationTable(strategies, experiment, experiment1, yLabel, minx, maxx);

            printSideData(minx, maxx, experiment1, (i, s) -> "inputs/experiment1/dining_" + i + "." + s + ".gm");

            yLabel = "Number of Iterations";
            printIterationTable(strategies, experiment, experiment1, yLabel, minx, maxx);

            yLabel = "Success Ratio $\\left[\\frac{U}{L}\\right]$";
            printRatioTable(strategies, experiment, experiment1, xLabel, yLabel, minx, maxx);

            System.out.println("\\caption{Performance statistics for the \\texttt{dining\\_n." + experiment1.replace("_", "\\_") + ".gm} parity games representing the dining philosophers problem set with $n \\in [2, \\dots, 11]$ dining philosophers.}");
            System.out.println("\\label{graph.performance." + experiment1 + "}");
            System.out.println("\\end{figure}");
        }
    }

    private static void outputExperiment2(List<String> strategies) throws IOException {

        int minx = 2;
        int maxx = 5;

        String experiment = "experiment2";

        // The names of the experiments to output.
        String[] experiments = {
                "infinite_run_no_access",
                "invariantly_eventually_fair_shared_access"
        };

        for (String experiment1 : experiments) {
            System.out.println("\\begin{figure}");
            System.out.println("\\centering");

            String xLabel = "Number of Clients $(n)$";
            String yLabel = "Running Time [ms]";
            printDurationTable(strategies, experiment, experiment1, yLabel, minx, maxx);

            printSideData(minx, maxx, experiment1, (i, s) -> "inputs/experiment2/german_linear_" + i + "." + s + ".gm");

            yLabel = "Number of Iterations";
            printIterationTable(strategies, experiment, experiment1, yLabel, minx, maxx);

            yLabel = "Success Ratio $\\left[\\frac{U}{L}\\right]$";
            printRatioTable(strategies, experiment, experiment1, xLabel, yLabel, minx, maxx);

            experiment1 = experiment1.replace("invariantly_eventually_fair_shared_access", "invariantly_eventually_fair_ shared_access");

            System.out.println("\\caption{Performance statistics for the \\texttt{german\\_linear\\_n." + experiment1.replace("_", "\\_") + ".gm} parity games representing the cache coherence problem set with $n \\in [2, \\dots, 5]$ clients.}");
            System.out.println("\\label{graph.performance." + experiment1 + "}");
            System.out.println("\\end{figure}");
        }
    }

    private static void outputExperiment3(List<String> strategies) throws IOException {

        int minx = 2;
        int maxx = 7;

        String experiment = "experiment3";

        // The names of the experiments to output.
        String[] experiments = {
                "elevator1",
                "elevator2"
        };

        for (String experiment1 : experiments) {
            System.out.println("\\begin{figure}");
            System.out.println("\\centering");

            String xLabel = "Number of Floors $(n)$";
            String yLabel = "Running Time [ms]";
            printDurationTable(strategies, experiment, experiment1, yLabel, minx, maxx);

            printSideData(minx, maxx, experiment1, (i, s) -> "inputs/experiment3/" + experiment1 +  "_" + i + ".gm");

            yLabel = "Number of Iterations";
            printIterationTable(strategies, experiment, experiment1, yLabel, minx, maxx);

            yLabel = "Success Ratio $\\left[\\frac{U}{L}\\right]$";
            printRatioTable(strategies, experiment, experiment1, xLabel, yLabel, minx, maxx);

            System.out.println("\\caption{Performance statistics for the \\texttt{" + experiment1.replace("_", "\\_") + "\\_n.gm} parity games representing the elevator fairness verification problem set with $n \\in [2, \\dots, 7]$ floors.}");
            System.out.println("\\label{graph.performance." + experiment1 + "}");
            System.out.println("\\end{figure}");
        }
    }


    private static void printTableHeader(StringBuilder builder, String ymode, String title, String xLabel, String yLabel,
                                         int xmin, int xmax, double ymin, long ymax, Double yshift) {
        builder.append("\\begin{axis}[").append(System.lineSeparator());
        builder.append("\txmode=linear,").append(System.lineSeparator());
        builder.append("\tymode=").append(ymode).append(",").append(System.lineSeparator());
        if(title != null) {
            builder.append("\ttitle=\\textbf{").append(title).append("},").append(System.lineSeparator());
            builder.append("\ttitle style={xshift=1.7cm},").append(System.lineSeparator());
        }
        if(xLabel != null) builder.append("\txlabel={").append(xLabel).append("},").append(System.lineSeparator());
        builder.append("\tylabel={").append(yLabel).append("},").append(System.lineSeparator());
        builder.append("\txmin=").append(xmin).append(",").append(System.lineSeparator());
        builder.append("\txmax=").append(xmax).append(",").append(System.lineSeparator());
        builder.append("\tymin=").append(ymin).append(",").append(System.lineSeparator());
        builder.append("\tymax=").append(ymax).append(",").append(System.lineSeparator());
        builder.append("\txtick={").append(xmin).append(",...,").append(xmax).append("},").append(System.lineSeparator());
        builder.append("\tlegend pos=outer north east,").append(System.lineSeparator());
        builder.append("\tlegend cell align={left},").append(System.lineSeparator());
        builder.append("\tgrid=both,").append(System.lineSeparator());
        builder.append("\tminor grid style={gray!25},").append(System.lineSeparator());
        builder.append("\tmajor grid style={gray!25},").append(System.lineSeparator());
        builder.append("\twidth=11cm,").append(System.lineSeparator());
        builder.append("\theight=7.2cm,").append(System.lineSeparator());
        builder.append("\tcycle list name=black white,").append(System.lineSeparator());
        if(title != null) {
            builder.append("\tlegend style={nodes={scale=0.75, transform shape},name=leg},").append(System.lineSeparator());
        } else {
            builder.append("\tlegend style={nodes={scale=0.75, transform shape}},").append(System.lineSeparator());
        }
        if(yshift != null) builder.append("\tyshift=").append(yshift).append("cm,").append(System.lineSeparator());
        builder.append("]").append(System.lineSeparator());
    }

    private static void printTableFooter(StringBuilder builder) {
        builder.append("\\end{axis}").append(System.lineSeparator());
    }

    private static void printTableSeries(StringBuilder builder, String legendEntry, String[] entries) {
        builder.append("\\addplot table [").append(System.lineSeparator());
        builder.append("\tx expr=\\thisrowno{0},").append(System.lineSeparator());
        builder.append("\ty expr=\\thisrowno{1}").append(System.lineSeparator());
        builder.append("] {").append(System.lineSeparator());

        for(String entry : entries) {
            builder.append("\t").append(entry).append(System.lineSeparator());
        }

        builder.append("};").append(System.lineSeparator());
        builder.append("\\addlegendentry{").append(legendEntry).append("}").append(System.lineSeparator());
    }

    private static void printDurationTable(List<String> strategies, String experimentFolder, String experiment, String yLabel, int minx, int maxx) throws IOException {
        StringBuilder builder = new StringBuilder();

        long max = 0;

        for(String strategy : strategies) {
            String path = "data/" + experimentFolder + "/" + strategy + "/" + experiment + "_duration.txt";

            // Load the desired file.
            String contents = new String(Files.readAllBytes(Paths.get(path)), StandardCharsets.UTF_8);
            String[] entries = contents.split("\\r?\\n");

            max = Math.max(max, Long.parseLong(entries[entries.length - 1].trim().split("\t")[1]));
        }

        builder.append("\\begin{tikzpicture}").append(System.lineSeparator());

        printTableHeader(
                builder,
                "log",
                "Performance measures for `" + experiment.replace("_", "\\_") + ".gm'",
                null,
                yLabel,
                minx,
                maxx,
                1,
                (long) Math.pow(10, Math.ceil(Math.log10(max)) + 1),
                null
        );

        for(String strategy : strategies) {
            String path = "data/" + experimentFolder + "/" + strategy + "/" + experiment + "_duration.txt";

            // Load the desired file.
            String contents = new String(Files.readAllBytes(Paths.get(path)), StandardCharsets.UTF_8);
            String[] entries = contents.split("\\r?\\n");

            printTableSeries(
                    builder,
                    "\\texttt{" + legendMap.get(strategy).replace("_", "\\_") + "}",
                    entries
            );
        }

        printTableFooter(builder);
        System.out.println(builder.toString());
    }

    private static void printSideData(int minx, int maxx, String experiment, BiFunction<Integer, String, String> getFilePath) throws IOException {
        StringBuilder builder = new StringBuilder();

        // What number of vertices does each of the graphs have?
        Map<Integer, Integer> noVertices = new LinkedHashMap<>();
        Map<Integer, Long> noEdges = new LinkedHashMap<>();
        Map<Integer, Integer> dSize = new LinkedHashMap<>();

        for(int i = minx; i < maxx + 1; i++) {
            String path = getFilePath.apply(i, experiment);
            ParityGame parityGame = Parser.parseParityGame(path);
            noVertices.put(i, parityGame.n);
            noEdges.put(i, parityGame.getNoEdges());
            dSize.put(i, parityGame.d);
        }

        builder.append("\\node[anchor=north west, below= 0.3cm of leg] (R) {").append(System.lineSeparator());
        builder.append("\t\\small\\texttt{repetitions = 10}").append(System.lineSeparator());
        builder.append("};").append(System.lineSeparator());

        builder.append("\\node[anchor=north west, below= 0cm of R] (K) {").append(System.lineSeparator());
        builder.append("\t\\small\\texttt{time limit = 60s}").append(System.lineSeparator());
        builder.append("};").append(System.lineSeparator());

        if(dSize.values().stream().distinct().count() == 1) {
            builder.append("\\node[anchor=north west, below= 0cm of K] (D) {").append(System.lineSeparator());
            builder.append("\t\\small\\texttt{d = ").append(dSize.get(minx)).append("}").append(System.lineSeparator());
            builder.append("};").append(System.lineSeparator());
        } else {
            builder.append("\\node[anchor=north west, below= 0.3cm of R] (D) {").append(System.lineSeparator());
            builder.append("\t\\begin{tabular}{>{\\small\\ttfamily}c||>{\\small\\ttfamily}l}").append(System.lineSeparator());
            builder.append("\t\tn & d\\\\").append(System.lineSeparator());
            builder.append("\t\t\\hline").append(System.lineSeparator());
            for(int i = minx; i < maxx + 1; i++) {
                builder.append("\t\t").append(i).append(" & ").append(dSize.get(i)).append("\\\\").append(System.lineSeparator());
            }
            builder.append("\\end{tabular}").append(System.lineSeparator());
            builder.append("};").append(System.lineSeparator());
        }

        builder.append("\\node[anchor=north west, below= 0.3cm of D] (V) {").append(System.lineSeparator());
        builder.append("{\\def\\arraystretch{0.85}").append(System.lineSeparator());
        builder.append("\t\\begin{tabular}{>{\\small\\ttfamily}c||>{\\small\\ttfamily}l}").append(System.lineSeparator());
        builder.append("\t\tn & |V|\\\\").append(System.lineSeparator());
        builder.append("\t\t\\hline").append(System.lineSeparator());
        for(int i = minx; i < maxx + 1; i++) {
            builder.append("\t\t").append(i).append(" & ").append(noVertices.get(i)).append("\\\\").append(System.lineSeparator());
        }
        builder.append("\t\t\\multicolumn{2}{c}{}\\\\").append(System.lineSeparator());
        builder.append("\t\tn & |E|\\\\").append(System.lineSeparator());
        builder.append("\t\t\\hline").append(System.lineSeparator());
        for(int i = minx; i < maxx + 1; i++) {
            builder.append("\t\t").append(i).append(" & ").append(noEdges.get(i)).append("\\\\").append(System.lineSeparator());
        }
        builder.append("\\end{tabular}").append(System.lineSeparator());
        builder.append("}").append(System.lineSeparator());
        builder.append("};").append(System.lineSeparator());

        System.out.println(builder.toString());
    }

    private static void printIterationTable(List<String> strategies, String experimentFolder, String experiment, String yLabel, int minx, int maxx) throws IOException {
        StringBuilder builder = new StringBuilder();

        long max = 0;

        for(String strategy : strategies) {
            String path = "data/" + experimentFolder + "/" + strategy + "/" + experiment + "_i_count.txt";

            // Load the desired file.
            String contents = new String(Files.readAllBytes(Paths.get(path)), StandardCharsets.UTF_8);
            String[] entries = contents.split("\\r?\\n");

            max = Math.max(max, Long.parseLong(entries[entries.length - 1].trim().split("\t")[1]));
        }

        printTableHeader(
                builder,
                "log",
                null,
                null,
                yLabel,
                minx,
                maxx,
                1,
                (long) Math.pow(10, Math.ceil(Math.log10(max)) + 1),
                -6.3
        );

        for(String strategy : strategies) {
            String path = "data/" + experimentFolder + "/" + strategy + "/" + experiment + "_i_count.txt";

            // Load the desired file.
            String contents = new String(Files.readAllBytes(Paths.get(path)), StandardCharsets.UTF_8);
            String[] entries = contents.split("\\r?\\n");

            printTableSeries(
                    builder,
                    "\\texttt{" + legendMap.get(strategy).replace("_", "\\_") + "}",
                    entries
            );
        }

        builder.append("\\legend{};").append(System.lineSeparator());

        printTableFooter(builder);

        System.out.println(builder.toString());
    }

    private static void printRatioTable(List<String> strategies, String experimentFolder, String experiment, String xLabel, String yLabel, int minx, int maxx) throws IOException {
        StringBuilder builder = new StringBuilder();

        printTableHeader(
                builder,
                "linear",
                null,
                xLabel,
                yLabel,
                minx,
                maxx,
                0,
                1,
                -12.6
        );

        for(String strategy : strategies) {
            String path = "data/" + experimentFolder + "/" + strategy + "/" + experiment + "_lift_update_ratio.txt";

            // Load the desired file.
            String contents = new String(Files.readAllBytes(Paths.get(path)), StandardCharsets.UTF_8);
            String[] entries = contents.split("\\r?\\n");

            printTableSeries(
                    builder,
                    "\\texttt{" + legendMap.get(strategy).replace("_", "\\_") + "}",
                    entries
            );
        }

        builder.append("\\legend{};").append(System.lineSeparator());

        printTableFooter(builder);

        builder.append("\\end{tikzpicture}").append(System.lineSeparator());

        System.out.println(builder.toString());
    }
}
