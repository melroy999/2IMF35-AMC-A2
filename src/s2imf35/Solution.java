package s2imf35;

import java.util.Arrays;
import java.util.Set;

/**
 * A class that holds all information we have about a solution.
 */
public class Solution {
    // The vertices that are won by the diamond player.
    public final Set<Integer> V;

    // The performance counter associated with the solution.
    public final PerformanceCounter counter;

    public Solution(Set<Integer> V, PerformanceCounter counter) {
        this.V = V;
        this.counter = counter;
    }

    @Override
    public String toString() {
        // Select only the first 20 states in the solution.
        Object[] first50States = V.stream().limit(20).toArray();
        String list = Arrays.toString(first50States);

        if(V.size() != first50States.length) {
            list = list.substring(0, list.length() - 1) + ", ...]";
        }

        return list + "\nCalculated in " + counter.duration + " milliseconds with (" + counter.toString() + ").";
    }
}
