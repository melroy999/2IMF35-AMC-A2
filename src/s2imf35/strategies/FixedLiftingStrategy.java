package s2imf35.strategies;

import s2imf35.graph.ParityGame;

public class FixedLiftingStrategy extends AbstractLiftingStrategy {
    // The order we have determined.
    private final int[] indices = {0, 0, 0, 1, 3, 2, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 5, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 3, 2, 1};

    // The current index.
    private int i = 0;

    public FixedLiftingStrategy() {

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
