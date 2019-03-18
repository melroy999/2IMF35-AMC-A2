package s2imf35.data;

import s2imf35.graph.ParityGame;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Representation of the progress measure table.
 */
public class ProgressMeasure {
    // Naive implementation: the table is a 2d array of integers.
    private final int[][] data;

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

    public void print(ParityGame G) {
        System.out.println();
        for(int v = 0; v < data.length; v++) {
            String name = G.getName(v);
            name = name == null ? "v" + v : name;
            System.out.println(name + ": p(v) = " + G.getPriority(v) + "; " + (data[v] == null ? "T" : Arrays.toString(data[v])));
        }
        System.out.println();
    }

    public void printStatistics() {
        int TCount = 0;
        int zeroCount = 0;

        for (int[] entry : data) {
            if (entry == null) {
                TCount++;
            } else if (Arrays.stream(entry).allMatch(i -> i == 0)) {
                zeroCount++;
            }
        }

        System.out.println("Number of entries: " + data.length);
        System.out.println("Number of T entries: " + TCount);
        System.out.println("Number of (0, 0, ...) entries: " + zeroCount);
    }
}
