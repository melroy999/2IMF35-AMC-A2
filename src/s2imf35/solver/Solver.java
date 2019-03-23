package s2imf35.solver;

import s2imf35.PerformanceCounter;
import s2imf35.Solution;
import s2imf35.data.ProgressMeasure;
import s2imf35.graph.NodeSpecification;
import s2imf35.graph.ParityGame;
import s2imf35.strategies.AbstractLiftingStrategy;
import s2imf35.util.ComparisonHelper;

import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

import static s2imf35.graph.NodeSpecification.Owner.Diamond;

public class Solver {
    public static Solution solve(ParityGame G, boolean verbose, AbstractLiftingStrategy strategy) {
        System.out.println("Solving: " + G.name);

        // Initialize rho data structure.
        ProgressMeasure rho = new ProgressMeasure(G);

        // Create a new performance measure.
        PerformanceCounter counter = new PerformanceCounter();
        Instant start = Instant.now();

        // The current step.
        int i = 0;

        if(verbose) {
            System.out.println("M = " + Arrays.toString(G.M));
        }

        // The last vertex id that resulted in a change.
        int noChangeIterations = 0;

        // We loop until unchanged contains all vertices.
        do {
            int v = strategy.next();
            counter.i++;

            // No need to lift vertices with the special symbol.
            if(rho.get(v) == null) {
                counter.tSkips++;
                noChangeIterations++;
                continue;
            }

            // Get the associated node.
            NodeSpecification node = G.get(v);

            if(verbose) {
                System.out.print("Step " + i++ + ": p(v) = " + node.priority + "; ");
            }

            int[] liftValue = lift(node, verbose, rho, G);
            counter.lifted++;

            if(verbose) {
                String name = node.name;
                name = name == null ? "v" + v : name;
                System.out.print(" = rho[" + name + " := max{" + (rho.get(v) == null ? "T" : Arrays.toString(rho.get(v))) + ", " + (liftValue == null ? "T" : Arrays.toString(liftValue)) + "}]");
            }

            // We only register a change when rho < lift_v(rho). I.e., the value must have become larger.
            if(ComparisonHelper.isGreater(liftValue, rho.get(v), G.d - 1)) {
                rho.put(v, liftValue);
                noChangeIterations = 0;
                counter.updated++;
                strategy.back();
            } else {
                noChangeIterations++;
            }

            if(verbose) {
                String name = node.name;
                name = name == null ? "v" + v : name;
                System.out.println(" = rho[" + name + " := " + (liftValue == null ? "T" : Arrays.toString(liftValue)) + "]");
            }
        } while(noChangeIterations <= G.n);

        if(verbose) {
            rho.print(G);
        }

        // Measure the elapsed time.
        Instant finish = Instant.now();
        counter.duration = Duration.between(start, finish).toMillis();

        System.out.println("d = " + G.d);
        rho.printStatistics();

        // We have found a solution. Find which states belong to the winning set of player diamond.
        return new Solution(rho.diamondWinningSet(), counter);

    }

    /**
     * Lift the given vertex and alter the given progress measure to adhere to the new situation.
     *
     * @param v The vertex to lift.
     * @param rho The parity progress measure.
     * @param G The parity game graph.
     * @return The new value for rho(v).
     */
    private static int[] lift(NodeSpecification v, boolean verbose, ProgressMeasure rho, ParityGame G) {
        // Get all transitions starting in v.
        int[] W = v.successors;

        // Calculate all the progress measures for the edges from v to W.
        List<int[]> progressMeasures = new ArrayList<>(W.length);
        for(int w : W) {
            progressMeasures.add(progress(rho.get(w), v.priority, G));
        }

        int[] result;
        if(v.owner == Diamond) {

            if(verbose) {
                String name = v.name;
                name = name == null ? "v" + v : name;
                System.out.print("Lift(rho, " + name + ") = rho[" + name + " := min{" + progressMeasures.stream()
                        .map(e -> e == null ? "T" : Arrays.toString(e)).collect(Collectors.joining(", ")) + "}]");
            }

            // Find the minimal progress value.
            result = progressMeasures.get(0);
            for(int i = 1; i < progressMeasures.size(); i++) {
                if(ComparisonHelper.isGreaterOrEqual(result, progressMeasures.get(i), G.d - 1)) {
                    result = progressMeasures.get(i);
                }
            }
        } else {

            if(verbose) {
                String name = v.name;
                name = name == null ? "v" + v : name;
                System.out.print("Lift(rho, " + name + ") = rho[" + name + " := max{" + progressMeasures.stream()
                        .map(e -> e == null ? "T" : Arrays.toString(e)).collect(Collectors.joining(", ")) + "}]");
            }

            // Find the maximal progress value.
            result = progressMeasures.get(0);
            for(int i = 1; i < progressMeasures.size(); i++) {
                if(ComparisonHelper.isGreaterOrEqual(progressMeasures.get(i), result, G.d - 1)) {
                    result = progressMeasures.get(i);
                }
            }
        }

        // Find the maximum between rho(v) and result.
        return result;
    }

    /**
     * Get the least m in M^T that adheres to the requirements given in definition (Prog) (Slide 18/46, lecture 8).
     *
     * @param a The array corresponding to g(w).
     * @param p The priority p(v) of vertex v.
     * @param G The parity game, containing the maximum M.
     * @return An array that is is the least option in M^T, or null (denoting T) when no such array is available.
     */
    private static int[] progress(int[] a, int p, ParityGame G) {
        if(a == null) {
            return null;
        }

        if(p % 2 == 0) {
            // p is even, so we should find the least m for which m >=_(p(v)) g(w) holds.
            // Thus, set all n_i values for i > p to 0.
            int[] b = new int[a.length];
            System.arraycopy(a, 0, b, 0, p + 1);
            return b;
        } else {
            // p is odd. Find the least m for which m >_(p(v)) g(w) holds if it exists.
            // If no such m exists, return T (denoted by null).
            boolean success = false;

            int[] b = new int[a.length];
            for(int i = p; i > 0; i--) {
                if(G.M[i] > a[i] && !success && i % 2 != 0) {
                    success = true;
                    b[i] = a[i] + 1;

                    // Reset all higher odd numbers to 0.
                    for(int j = i + 2; j <= p; j += 2) {
                        b[j] = 0;
                    }
                } else {
                    b[i] = a[i];
                }
            }

            // Return null when we did not manage to increase b (and thus, m = g(w) = T).
            return success ? b : null;
        }
    }
}
