package s2imf35.strategies;

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
            case 0: default:
                return new InputOrderLiftingStrategy(G);
        }
    }

    public static String getName(int i) {
        switch (i) {
            case 1: return "Random order";
            case 2: return "Input order with repetition";
            case 3: return "Random order with repetition";
            case 0: default: return "Input order";
        }
    }

    public void back() {
        // Default implementation does nothing on a back step.
    }
}
