package s2imf35;

import s2imf35.data.ProgressMeasure;
import s2imf35.graph.ParityGame;
import s2imf35.strategies.AbstractLiftingStrategy;
import s2imf35.util.ComparisonHelper;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static s2imf35.graph.NodeSpecification.Owner.Diamond;

public class Solver {
    public static Set<Integer> solve(ParityGame G, boolean verbose, AbstractLiftingStrategy strategy) {
        // Initialize rho data structure.
        ProgressMeasure rho = new ProgressMeasure(G);

        // A table that holds all vertices that remain unchanged.
        Set<Integer> unchanged = new HashSet<>(G.n);

        // We loop until unchanged contains all vertices.
        while(unchanged.size() != G.n) {
            int v = strategy.next();

            // No need to lift vertices with the special symbol.
            if(rho.get(v) == null) {
                unchanged.add(v);
                continue;
            }

            int[] liftValue = lift(v, rho, G);

            // We only register a change when rho < lift_v(rho). I.e., the value must have become larger.
            if(ComparisonHelper.isGreater(liftValue, rho.get(v), G.d - 1)) {
                rho.put(v, liftValue);
                unchanged.clear();
            } else {
                unchanged.add(v);
            }

            if(verbose) {
                String name = G.getName(v);
                name = name == null ? "v" + v : name;
                System.out.println("Lift(rho, " + name + ") = rho[" + name + " := " + Arrays.toString(liftValue) + "]");
            }
        }

        // We have found a solution. Find which states belong to the winning set of player diamond.
        return rho.diamondWinningSet();

    }

    /**
     * Lift the given vertex and alter the given progress measure to adhere to the new situation.
     *
     * @param v The vertex to lift.
     * @param rho The parity progress measure.
     * @param G The parity game graph.
     * @return The new value for rho(v).
     */
    private static int[] lift(int v, ProgressMeasure rho, ParityGame G) {
        // Get all transitions starting in v.
        int[] W = G.getSuccessors(v);

        // Calculate all the progress measures for the edges from v to W.
        List<int[]> progressMeasures = Arrays.stream(W).mapToObj(w -> progress(rho.get(w), G.getPriority(v), G))
                .collect(Collectors.toList());

        int[] result;
        if(G.getOwner(v) == Diamond) {
            // Find the minimal progress value.
            result = progressMeasures.get(0);
            for(int i = 1; i < progressMeasures.size(); i++) {
                if(ComparisonHelper.isGreaterOrEqual(result, rho.get(i), G.d - 1)) {
                    result = progressMeasures.get(i);
                }
            }
        } else {
            // Find the maximal progress value.
            result = progressMeasures.get(0);
            for(int i = 1; i < progressMeasures.size(); i++) {
                if(ComparisonHelper.isGreaterOrEqual(rho.get(i), result, G.d - 1)) {
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
