package s2imf35.strategies;

import s2imf35.data.LinearProgressMeasure;
import s2imf35.graph.NodeSpecification;
import s2imf35.graph.ParityGame;

import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;

public class PredecessorRepeatLiftingStrategy extends AbstractLiftingStrategy {
    // Whether we have enqueued the given vertex.
    private final boolean[] enqueued;

    // The priority queue that holds the vertices we are about to visit.
    private final Queue<Integer> queue;

    // The last vertex we have lifted successfully. Null if it does not exist.
    private Integer lastLifted = null;

    public PredecessorRepeatLiftingStrategy(ParityGame G) {
        enqueued = new boolean[G.n];
        queue = new LinkedList<>();

        // Enqueue all vertices.
        for(int v = 0; v < G.n; v++) {
            queue.add(v);
            enqueued[v] = true;
        }
    }

    @Override
    public boolean hasNext() {
        return !queue.isEmpty();
    }

    /**
     * Returns the next element in the iteration.
     *
     * @return the next element in the iteration
     */
    @Override
    public Integer next() {
        if(lastLifted != null) {
            int v = lastLifted;
            lastLifted = null;
            return v;
        } else {
            int v = queue.poll();
            enqueued[v] = false;
            return v;
        }
    }

    /**
     * This method is called when a vertex has been lifted successfully.
     *
     * @param v The vertex that has been lifted successfully.
     */
    @Override
    public void lifted(NodeSpecification v, LinearProgressMeasure rho) {
        lastLifted = v.priority;

        for(int w : v.predecessors) {
            if(!enqueued[w] && rho.get(w) != rho.T) {
                enqueued[w] = true;
                queue.add(w);
            }
        }
    }

    /**
     * This method is called when a vertex has been lifted successfully.
     *
     * @param v The vertex that has been lifted successfully.
     */
    @Override
    public void lifted(NodeSpecification v) {
        lastLifted = v.priority;

        for(int w : v.predecessors) {
            if(!enqueued[w]) {
                enqueued[w] = true;
                queue.add(w);
            }
        }
    }
}
