package s2imf35.data;

import s2imf35.graph.ParityGame;

import java.util.HashSet;
import java.util.Set;

public class LinearProgressMeasure {
    // The progress measure arrays are encoded as long values.
    private final long[] data;

    // The cheat table that helps with the conversion and calculations upon vectors.
    // This array functions as an index-based base number of the number.
    public final long[] B;

    // The value of T, which is one larger as the largest storable number.
    public final long T;

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
    }

    public boolean greater(long a, long b, int p) {
        long temp = a - b;
        return temp > temp % B[p];
    }

    public boolean greaterOrEqual(long a, long b, int p) {
        long temp = a - b;
        return temp >= temp % B[p];
    }

    public long get(int v) {
        return data[v];
    }

    public void set(int v, long l) {
        data[v] = l;
    }

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
