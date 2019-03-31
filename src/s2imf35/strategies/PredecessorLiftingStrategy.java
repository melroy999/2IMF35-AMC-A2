package s2imf35.strategies;

import s2imf35.data.LinearProgressMeasure;
import s2imf35.graph.NodeSpecification;
import s2imf35.graph.ParityGame;

import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;

/**
 * A lifting strategy that uses the predecessor list to avoid vertices that cannot be lifted under the current circumstances.
 */
public class PredecessorLiftingStrategy extends AbstractLiftingStrategy {
    // Whether we have enqueued the given vertex.
    private final boolean[] enqueued;

    // The priority queue that holds the vertices we are about to visit.
    private final Queue<Integer> queue;

    /**
     * Create predecessor lifting strategy for the given parity game.
     *
     * @param G The parity game to create the lifting strategy for.
     */
    public PredecessorLiftingStrategy(ParityGame G) {
        enqueued = new boolean[G.n];
        queue = new LinkedList<>();

        // Enqueue all vertices.
        for(int v = 0; v < G.n; v++) {
            queue.add(v);
            enqueued[v] = true;
        }
    }

    /**
     * Check whether we have a next value to report.
     *
     * @return Returns true as long as the queue contains vertices to visit.
     */
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
        int v = queue.poll();
        enqueued[v] = false;
        return v;
    }

    /**
     * This method is called when a vertex has been lifted successfully.
     *
     * @param v The vertex that has been lifted successfully.
     */
    @Override
    public void lifted(NodeSpecification v, LinearProgressMeasure rho) {
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
        for(int w : v.predecessors) {
            if(!enqueued[w]) {
                enqueued[w] = true;
                queue.add(w);
            }
        }
    }
}
