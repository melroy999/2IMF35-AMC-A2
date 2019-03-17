package s2imf35.strategies;

import s2imf35.graph.ParityGame;

public class InputOrderLiftingStrategy extends AbstractLiftingStrategy {
    // The order we have determined.
    private final int[] indices;

    // The current index.
    private int i = 0;

    public InputOrderLiftingStrategy(ParityGame G) {
        this.indices = G.originalOrder;
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
}
