package s2imf35.strategies;

import s2imf35.data.LinearProgressMeasure;
import s2imf35.graph.NodeSpecification;
import s2imf35.graph.ParityGame;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class RandomRepeatLiftingStrategy extends AbstractLiftingStrategy {
    // The order we have determined.
    private final int[] indices;

    // The current index.
    private int i = 0;

    // The number of unchanged iterations.
    private int unchangedIterations = 0;

    /**
     * Create an random order lifting strategy with repetition for the given parity game.
     *
     * @param G The parity game to create the lifting strategy for.
     * @param seed A seed to control randomness with.
     */
    public RandomRepeatLiftingStrategy(ParityGame G, long seed) {
        indices = G.originalOrder;

        // Shuffle the indices.
        shuffleArray(indices, seed);
    }

    /**
     * Implementation for the Fisher-Yates shuffle.
     * Source: https://stackoverflow.com/a/1520212
     *
     * @param ar The array to shuffle.
     * @param seed The seed to control randomness with.
     */
    private static void shuffleArray(int[] ar, long seed)
    {
        // If running on Java 6 or older, use `new Random()` on RHS here
        Random rnd = new Random(seed);
        for (int i = ar.length - 1; i > 0; i--)
        {
            int index = rnd.nextInt(i + 1);
            // Simple swap
            int a = ar[index];
            ar[index] = ar[i];
            ar[i] = a;
        }
    }

    /**
     * Check whether we have a next value to report.
     *
     * @return False if we have had n consecutive iterations, in which no successful lifts have been achieved.
     */
    @Override
    public boolean hasNext() {
        return unchangedIterations < indices.length;
    }

    /**
     * Returns the next element in the iteration.
     *
     * @return the next element in the iteration
     */
    @Override
    public Integer next() {
        int value = indices[i];
        if(i < indices.length - 1) {
            i++;
            unchangedIterations++;
        } else {
            i = 0;
        }
        return value;
    }

    /**
     * This method is called when a vertex has been lifted successfully.
     *
     * @param v   The vertex that has been lifted successfully.
     * @param rho The process measure that contains vector information.
     */
    @Override
    public void lifted(NodeSpecification v, LinearProgressMeasure rho) {
        // We reset the unchanged counter.
        unchangedIterations = 0;

        // Backtrack one index.
        if(i > 0) {
            i--;
        } else {
            i = indices.length - 1;
        }
    }

    /**
     * This method is called when a vertex has been lifted successfully.
     *
     * @param v The vertex that has been lifted successfully.
     */
    @Override
    public void lifted(NodeSpecification v) {
        // We reset the unchanged counter.
        unchangedIterations = 0;

        // Backtrack one index.
        if(i > 0) {
            i--;
        } else {
            i = indices.length - 1;
        }
    }
}
