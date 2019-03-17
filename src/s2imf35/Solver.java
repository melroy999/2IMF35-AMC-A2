package s2imf35;

import s2imf35.data.AbstractProgressMeasure;
import s2imf35.data.ProgressMeasure;
import s2imf35.graph.ParityGame;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static s2imf35.graph.NodeSpecification.Owner.Diamond;

public class Solver {
    public void solve(ParityGame G) {
        // Initialize rho data structure.
        AbstractProgressMeasure rho = new ProgressMeasure();

    }

    /**
     * Lift the given vertex and alter the given progress measure to adhere to the new situation.
     *
     * @param v The vertex to lift.
     * @param rho The parity progress measure.
     * @param G The parity game graph.
     */
    private void lift(int v, AbstractProgressMeasure rho, ParityGame G) {
        // Get all transitions starting in v.
        int[] W = G.getSuccessors(v);

        // Calculate all the progression measures for the edges from v to W.
        List<List<Integer>> P = Arrays.stream(W).mapToObj(w -> prog(v, w, rho, G)).collect(Collectors.toList());

        if(G.getOwner(v) == Diamond) {

        } else {

        }
    }

    private List<Integer> prog(int v, int w, AbstractProgressMeasure rho, ParityGame G) {
        if(G.getPriority(v) % 2 == 0) {
            // The priority is even.
        } else {
            // The priority is odd.
        }

        return null;
    }
}
