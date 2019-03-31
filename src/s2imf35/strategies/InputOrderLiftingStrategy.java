package s2imf35.strategies;

import s2imf35.data.LinearProgressMeasure;
import s2imf35.graph.NodeSpecification;
import s2imf35.graph.ParityGame;

/**
 * A lifting strategy that iterates in the order of the vertices, as given in the file.
 */
public class InputOrderLiftingStrategy extends AbstractLiftingStrategy {
    // The order we have determined.
    private final int[] indices;

    // The current index.
    private int i = 0;

    // The number of unchanged iterations.
    private int unchangedIterations = 0;

    /**
     * Create an input order lifting strategy for the given parity game.
     *
     * @param G The parity game to create the lifting strategy for.
     */
    public InputOrderLiftingStrategy(ParityGame G) {
        this.indices = G.originalOrder;
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
    }
}
