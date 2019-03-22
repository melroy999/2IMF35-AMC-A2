package s2imf35.strategies;

import s2imf35.graph.ParityGame;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class RandomLiftingStrategy extends AbstractLiftingStrategy {
    // The order we have determined.
    private final Integer[] indices;

    // The current index.
    private int i = 0;

    public RandomLiftingStrategy(ParityGame G, long seed) {
        List<Integer> indices = Arrays.stream(G.originalOrder).boxed().collect(Collectors.toList());

        // Create a seeded random.
        Random random = new Random(seed);

        // Shuffle the indices.
        Collections.shuffle(indices, random);

        // Convert the list to an array.
        this.indices = indices.toArray(new Integer[indices.size()]);
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
}
