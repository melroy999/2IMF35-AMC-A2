package s2imf35.strategies;

import s2imf35.data.LinearProgressMeasure;
import s2imf35.graph.NodeSpecification;
import s2imf35.graph.ParityGame;

import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Queue;

/**
 * A lifting strategy that uses the predecessor list to avoid vertices that cannot be lifted under the current circumstances.
 * The lifting strategy uses a priority queue, which ensures that vertices controlled by the diamond player are processed first.
 * Additionally, vertices are repeated after successful lifts.
 */
public class PredecessorDiamondFirstRepeatLiftingStrategy extends AbstractLiftingStrategy {
    // Whether we have enqueued the given vertex.
    private final boolean[] enqueued;

    // The priority queue that holds the vertices we are about to visit.
    private final Queue<Integer> queue;

    // The last vertex we have lifted successfully. Null if it does not exist.
    private Integer lastLifted = null;

    /**
     * Create predecessor lifting strategy for the given parity game, that sorts the vertices in the queue such that
     * vertices owned by the diamond player occur first.
     *
     * @param G The parity game to create the lifting strategy for.
     */
    public PredecessorDiamondFirstRepeatLiftingStrategy(ParityGame G) {
        enqueued = new boolean[G.n];
        queue = new PriorityQueue<>(
                Comparator.comparing(v -> G.getOwner((int) v))
        );

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
    @SuppressWarnings("Duplicates")
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
