package s2imf35.strategies;

import s2imf35.graph.ParityGame;

import java.util.Arrays;
import java.util.Comparator;

public class InDegreeRepeatLiftingStrategy extends AbstractLiftingStrategy {
    // The order we have determined.
    private final int[] indices;

    // The current index.
    private int i = 0;

    public InDegreeRepeatLiftingStrategy(ParityGame G) {
        // Count the number of incoming edges.
        int[] incoming = new int[G.n];

        for(int v = 0; v < G.n; v++) {
            for(int w : G.getSuccessors(v)) {
                incoming[w]++;
            }
        }

        // Sort the edges based on their incoming edges count.
        indices = Arrays.stream(G.originalOrder).boxed()
                .sorted(Comparator.comparingInt(v -> incoming[v]))
                .mapToInt(i -> i).toArray();
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
