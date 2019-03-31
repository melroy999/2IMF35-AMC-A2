package s2imf35.data;

import s2imf35.graph.ParityGame;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Naive representation of the progress measure table.
 */
public class ProgressMeasure {
    // Naive implementation: the table is a 2d array of integers.
    private final int[][] data;

    /**
     * Create a new naive progress measure table for the given parity game.
     *
     * @param G The parity game to create a progress measure table for.
     */
    public ProgressMeasure(ParityGame G) {
        data = new int[G.n][G.d];
    }

    /**
     * Get the row in the progress measure table associated to the given vertex.
     *
     * @param v The vertex that is queried.
     * @return The vector representation of the row.
     */
    public int[] get(int v) {
        return data[v];
    }

    /**
     * Set the row in the progress measure table associated to the given vertex.
     *
     * @param v The vertex that is selected.
     * @param value The vector row representation to store.
     */
    public void set(int v, int[] value) {
        data[v] = value;
    }

    /**
     * Get all the vertices that are part of the winning set.
     *
     * @return Return all vertices that do not have the value T.
     */
    public Set<Integer> diamondWinningSet() {
        Set<Integer> result = new HashSet<>();
        for(int i = 0; i < data.length; i++) {
            if(data[i] != null) {
                result.add(i);
            }
        }
        return result;
    }

    /**
     * Print the entries in the progress measure table.
     *
     * @param G The graph that contains the names of the vertices.
     */
    public void print(ParityGame G) {
        System.out.println();
        for(int v = 0; v < data.length; v++) {
            String name = G.getName(v);
            name = name == null ? "v" + v : name;
            System.out.println(name + ": p(v) = " + G.getPriority(v) + "; " + (data[v] == null ? "T" : Arrays.toString(data[v])));
        }
        System.out.println();
    }
}
