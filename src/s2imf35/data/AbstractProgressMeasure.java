package s2imf35.data;

public abstract class AbstractProgressMeasure {
    /**
     * Get the value for a vertex and position pair. This function returns -1 when the vertex is considered lost.
     *
     * @param v The vertex to search for.
     * @param i The position to receive.
     * @return The value at position (v, i) in the mapping if present, zero otherwise. If v in trapped, return -1.
     */
    public abstract int get(int v, int i);

    /**
     * Add a new value for a vertex and position pair.
     *
     * @param v The vertex to add the value for.
     * @param i The position to add the value for.
     * @param value The value to add.
     */
    public abstract void put(int v, int i, int value);
}
