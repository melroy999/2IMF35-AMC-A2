package s2imf35.strategies;

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

    public RandomRepeatLiftingStrategy(ParityGame G, long seed) {
        indices = G.originalOrder;

        // Shuffle the indices.
        shuffleArray(indices, seed);
    }

    // Implementing Fisher–Yates shuffle
    //@https://stackoverflow.com/a/1520212
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
     * Returns {@code true} if the iteration has more elements.
     * (In other words, returns {@code true} if {@link #next} would
     * return an element rather than throwing an exception.)
     *
     * @return {@code true} if the iteration has more elements
     */
    @Override
    public boolean hasNext() {
        return true;
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
        } else {
            i = 0;
        }
        return value;
    }

    @Override
    public void back() {
        if(i > 0) {
            i--;
        } else {
            i = indices.length - 1;
        }
    }
}
