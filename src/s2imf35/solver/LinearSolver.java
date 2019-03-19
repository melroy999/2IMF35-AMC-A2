package s2imf35.solver;

import s2imf35.PerformanceCounter;
import s2imf35.Solution;
import s2imf35.data.LinearProgressMeasure;
import s2imf35.graph.ParityGame;
import s2imf35.strategies.AbstractLiftingStrategy;

import java.time.Duration;
import java.time.Instant;
import java.util.*;

import static s2imf35.graph.NodeSpecification.Owner.Diamond;

public class LinearSolver {
    public static Solution solve(ParityGame G, AbstractLiftingStrategy strategy) {
        System.out.println("Solving: " + G.name);

        // Initialize rho data structure.
        LinearProgressMeasure rho = new LinearProgressMeasure(G);

        // Create a new performance measure.
        PerformanceCounter counter = new PerformanceCounter();
        Instant start = Instant.now();

        // The last vertex id that resulted in a change.
        int noChangeIterations = 0;

        // The current step.
        int i = 0;

        // We loop until unchanged contains all vertices.
        do {
            int v = strategy.next();
            counter.i++;

            // We do not need to lift vertices that have the special symbol T.
            if(rho.get(v) == rho.T) {
                counter.tSkips++;
                noChangeIterations++;
                continue;
            }

            // Get the lift value for vertex v.
            long lift = lift(v, rho, G);
            counter.lifted++;

            // We only register a change when rho < lift_v(rho). I.e., the value must have become larger.
            if(rho.get(v) < lift) {
                rho.set(v, lift);
                noChangeIterations = 0;
                counter.changed++;
            } else {
                noChangeIterations++;
            }
        } while(noChangeIterations <= G.n);

        // Measure the elapsed time.
        Instant finish = Instant.now();
        counter.duration = Duration.between(start, finish).toMillis();

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
    private static long lift(int v, LinearProgressMeasure rho, ParityGame G) {
        // Get all transitions starting in v.
        int[] W = G.getSuccessors(v);

        // The priority we are working under.
        int p = G.getPriority(v);

        // Find all unique values in the progress measure table at positions in W, and progress.
//        Set<Long> values = new HashSet<>();
//        for(int w : W) {
//            values.add(rho.get(w));
//        }
//
//        Set<Long> progressions = new HashSet<>();
//        for(long prog : values) {
//            progressions.add(progress(prog, p, rho, G));
//        }

        List<Long> progressions = new ArrayList<>();
        for(int w : W) {
            progressions.add(progress(rho.get(w), p, rho, G));
        }

        // Select the lift value.
        long result;
        if(G.getOwner(v) == Diamond) {
            // Find the minimal progress value.
            result = Collections.min(progressions);
        } else {
            // Find the maximal progress value.
            result = Collections.max(progressions);
        }

        // Our calculations might result in a larger value than T. Cap to T.
        return Math.min(result, rho.T);
    }

    /**
     * Get the least m in M^T that adheres to the requirements given in definition (Prog) (Slide 18/46, lecture 8).
     *
     * @param a The array corresponding to g(w).
     * @param p The priority p(v) of vertex v.
     * @param rho The parity progress measure.
     * @param G The parity game, containing the maximum M.
     * @return The linearized representation of the vector returned by Prog.
     */
    private static long progress(long a, int p, LinearProgressMeasure rho, ParityGame G) {
        // We cannot progress values that are already maximized.
        if(a == rho.T) {
            return rho.T;
        }

        // We first want to "forget" about all "digits" after position p.
        // We can forget about the last (p, d) digits by subtracting the value modulo B[i].
        long result = a - a % rho.B[p];

        if(p % 2 == 0) {
            // This way, we have that a =_{p} m, which is the smallest attainable value for a >=_{p} m.
            return result;
        } else {
            // We can find the next value in the topological order by simply adding B[i].
            // This works since, just like binary and any other base number, the value carries over to the next order.
            return result + rho.B[p];
        }
    }
}
