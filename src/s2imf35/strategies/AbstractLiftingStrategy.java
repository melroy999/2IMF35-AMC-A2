package s2imf35.strategies;

import s2imf35.graph.ParityGame;

import java.util.Iterator;

public abstract class AbstractLiftingStrategy implements Iterator<Integer> {

    public static AbstractLiftingStrategy get(ParityGame G, int i) {
        switch (i) {
            case 0:
                return new InputOrderLiftingStrategy(G);
        }
        return null;
    }
}
