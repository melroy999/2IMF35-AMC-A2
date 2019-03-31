package s2imf35.data;

import s2imf35.graph.ParityGame;

import java.util.HashSet;
import java.util.Set;

/**
 * Implementation of the progress measure table, using the compressed long representations of vectors.
 */
public class LinearProgressMeasure {
    // The progress measure arrays are encoded as long values.
    private final long[] data;

    // The cheat table that helps with the conversion and calculations upon vectors.
    // This array functions as an index-based base number of the number.
    public final long[] B;

    // The value of T, which is one larger as the largest storable number.
    public final long T;

    /**
     * Create a new linearized progress measure table for the given parity game.
     *
     * @param G The parity game to create a progress measure table for.
     */
    public LinearProgressMeasure(ParityGame G) {
        data = new long[G.n];
        B = new long[G.d];

        // Construct the B vector by multiplying each value with the previously calculated value.
        B[B.length - 1] = 1L;
        for(int i = B.length - 1; i > 0; i--) {
            B[i - 1] = (G.M[i] + 1) * B[i];
        }

        // Convert the max vector M to a linear representation and choose a value for T.
        long T = 0;
        for(int i = 0; i < B.length; i++) {
            T += G.M[i] * B[i];
        }
        this.T = T + 1;

        // We have to make sure that the graph G fits in the configuration; i.e. we do not want overflows.
        // We do this calculation after the one above, as otherwise the entire program becomes 3x slower.
        for(int i = 0; i < B.length - 1; i++) {
            if(B[i] < B[i + 1]) {
                throw new ArithmeticException("The vector B has overflow errors. " +
                        "The linear storage utility will not work appropriately.");
            }
        }

        T = 0;
        for(int i = 0; i < B.length; i++) {
            T = Math.addExact(T, Math.multiplyExact(G.M[i], B[i]));
        }
        T = Math.addExact(T, B[0]);
    }

    /**
     * Get the row in the progress measure table associated to the given vertex.
     *
     * @param v The vertex that is queried.
     * @return The compressed long representation of the row.
     */
    public long get(int v) {
        return data[v];
    }

    /**
     * Set the row in the progress measure table associated to the given vertex.
     *
     * @param v The vertex that is selected.
     * @param l The compressed row representation to store.
     */
    public void set(int v, long l) {
        data[v] = l;
    }

    /**
     * Get all the vertices that are part of the winning set.
     *
     * @return Return all vertices that do not have the value T.
     */
    public Set<Integer> diamondWinningSet() {
        Set<Integer> result = new HashSet<>();
        for(int i = 0; i < data.length; i++) {
            if(data[i] != T) {
                result.add(i);
            }
        }
        return result;
    }
}
