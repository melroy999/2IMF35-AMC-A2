package s2imf35.strategies;

import s2imf35.data.LinearProgressMeasure;
import s2imf35.graph.NodeSpecification;
import s2imf35.graph.ParityGame;

import java.util.Iterator;

/**
 * Abstract template for lifting strategies.
 */
public abstract class AbstractLiftingStrategy implements Iterator<Integer> {

    /**
     * Get an instance of the desired lifting strategy, given its id.
     *
     * @param G The graph to create a lifting strategy for.
     * @param i The id of the desired lifting strategy.
     * @param seed Optional seed, to control randomness.
     * @return The targeted lifting strategy.
     */
    public static AbstractLiftingStrategy get(ParityGame G, int i, long seed) {
        switch (i) {
            case 1:
                return new RandomLiftingStrategy(G, seed);
            case 2:
                return new InputOrderRepeatLiftingStrategy(G);
            case 3:
                return new RandomRepeatLiftingStrategy(G, seed);
            case 4:
                return new PredecessorLiftingStrategy(G);
            case 5:
                return new PredecessorRepeatLiftingStrategy(G);
            case 6:
                return new PredecessorDiamondFirstLiftingStrategy(G);
            case 7:
                return new PredecessorDiamondFirstRepeatLiftingStrategy(G);
            case 0: default:
                return new InputOrderLiftingStrategy(G);
        }
    }

    /**
     * Get the human readable name of the desired lifting strategy, given its id.
     *
     * @param i The id of the desired lifting strategy.
     * @return The name associated to the lifting strategy's id.
     */
    public static String getName(int i) {
        switch (i) {
            case 1: return "Random order";
            case 2: return "Input order with repetition";
            case 3: return "Random order with repetition";
            case 4: return "Predecessors";
            case 5: return "Predecessors with repetition";
            case 6: return "Predecessors with diamond first heuristic";
            case 7: return "Predecessors with diamond first heuristic and repetition";
            case 0: default: return "Input order";
        }
    }

    /**
     * Get the human readable slug of the desired lifting strategy, given its id.
     *
     * @param i The id of the desired lifting strategy.
     * @return The slug associated to the lifting strategy's id.
     */
    public static String getSlug(int i) {
        switch (i) {
            case 1: return "random_order";
            case 2: return "input_order_rep";
            case 3: return "random_order_rep";
            case 4: return "predecessors";
            case 5: return "predecessors_rep";
            case 6: return "predecessors_diamond_first";
            case 7: return "predecessors_diamond_first_rep";
            case 0: default: return "input_order";
        }
    }

    /**
     * This method is called when a vertex has been lifted successfully.
     *
     * @param v The vertex that has been lifted successfully.
     * @param rho The process measure that contains vector information.
     */
    public abstract void lifted(NodeSpecification v, LinearProgressMeasure rho);

    /**
     * This method is called when a vertex has been lifted successfully.
     *
     * @param v The vertex that has been lifted successfully.
     */
    public abstract void lifted(NodeSpecification v);
}
