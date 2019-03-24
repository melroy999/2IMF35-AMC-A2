package s2imf35.strategies;

import s2imf35.data.LinearProgressMeasure;
import s2imf35.graph.NodeSpecification;
import s2imf35.graph.ParityGame;

import java.util.Iterator;

public abstract class AbstractLiftingStrategy implements Iterator<Integer> {

    public static AbstractLiftingStrategy get(ParityGame G, int i, int seed) {
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
            case 0: default:
                return new InputOrderLiftingStrategy(G);
        }
    }

    public static String getName(int i) {
        switch (i) {
            case 1: return "Random order";
            case 2: return "Input order with repetition";
            case 3: return "Random order with repetition";
            case 4: return "Predecessors";
            case 5: return "Predecessors with repetition";
            case 0: default: return "Input order";
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
