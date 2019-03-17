package s2imf35.data;

import s2imf35.graph.ParityGame;

import java.util.HashSet;
import java.util.Set;

/**
 * Representation of the progress measure table.
 */
public class ProgressMeasure {
    // Naive implementation: the table is a 2d array of integers.
    private final int[][] data;

    // The vertices that are set to the special value.
    private final HashSet<Integer> trapped = new HashSet<>();

    public ProgressMeasure(ParityGame G) {
        data = new int[G.n][G.d];
    }

    public int[] get(int v) {
        return data[v];
    }

    public void put(int v, int[] value) {
        data[v] = value;
    }

    public Set<Integer> diamondWinningSet() {
        Set<Integer> result = new HashSet<>();
        for(int i = 0; i < data.length; i++) {
            if(data[i] != null) {
                result.add(i);
            }
        }
        return result;
    }
}
