package s2imf35.data;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * Representation of the progress measure table.
 */
public class ProgressMeasure extends AbstractProgressMeasure {
    // The internal representation of the progress measure table is a hash map.
    private final HashMap<Key, Integer> data = new HashMap<>();

    // The vertices that are considered lost.
    private final Set<Integer> trapped = new HashSet<>();

    /**
     * Get the value for a vertex and position pair. This function returns -1 when the vertex is considered lost.
     *
     * @param v The vertex to search for.
     * @param i The position to receive.
     * @return The value at position (v, i) in the mapping if present, zero otherwise. If v in trapped, return -1.
     */
    public int get(int v, int i) {
        return trapped.contains(v) ? -1 : data.getOrDefault(new Key(v, i), 0);
    }

    /**
     * Add a new value for a vertex and position pair. If -1 is given, the vertex is added to the trapped states.
     *
     * @param v The vertex to add the value for.
     * @param i The position to add the value for.
     * @param value The value to add.
     */
    public void put(int v, int i, int value) {
        if(value == -1) {
            trapped.add(v);
        } else {
            data.put(new Key(v, i), value);
        }
    }

    /**
     * The object used as a key for a progress measure.
     */
    public class Key {
        private final int v;
        private final int i;

        /**
         * Create a key pair for a given vertex and position.
         *
         * @param v The vertex to create a key-pair for.
         * @param i The position to create a key-pair for.
         */
        Key(int v, int i) {
            this.v = v;
            this.i = i;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Key key = (Key) o;
            return v == key.v && i == key.i;
        }

        @Override
        public int hashCode() {
            int result = v;
            result = 31 * result + i;
            return result;
        }
    }
}
