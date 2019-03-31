package s2imf35.util;

/**
 * A helper class that defines a couple of useful comparison methods for lexicographic orderings.
 */
public class ComparisonHelper {
    /**
     * Check whether a is lexicographically greater than b up to position r.
     *
     * @param a The array that is desired to be the larger element.
     * @param b The array that is desired to be the smaller element.
     * @param r The position up to which to compare.
     * @return Whether a >(_r) b holds.
     */
    public static boolean isGreater(int[] a, int[] b, int r) {
        // T > T does not hold.
        if(a == null && b == null) return false;

        // T > b holds when b != T.
        if(a == null) return true;

        // a > T does not hold.
        if(b == null) return false;

        for(int i = 0; i <= r; i++) {
            if(b[i] > a[i]) return false;
            if(b[i] < a[i]) return true;
        }

        // The two pairs are equal.
        return false;
    }

    /**
     * Check whether a is lexicographically greater than or equal to b up to position r.
     *
     * @param a The array that is desired to be the larger element.
     * @param b The array that is desired to be the smaller element.
     * @param r The position up to which to compare.
     * @return Whether a >=(_r) b holds.
     */
    public static boolean isGreaterOrEqual(int[] a, int[] b, int r) {
        // a >= T does not hold when a != T.
        if(b == null && a != null) return false;

        // T >= b, where b may also be T.
        if(a == null) return true;

        for(int i = 0; i <= r; i++) {
            if(b[i] > a[i]) return false;
            if(b[i] < a[i]) return true;
        }

        // The two pairs are equal.
        return true;
    }

    /**
     * Check whether a is lexicographically equal to b up to position r.
     *
     * @param a The first array to compare.
     * @param b The second array to compare.
     * @param r The position up to which to compare.
     * @return Whether a =(_r) b holds.
     */
    public static boolean isEqual(int[] a, int[] b, int r) {
        if(a == null && b == null) return true;
        if(a == null) return false;
        if(b == null) return false;

        for(int i = 0; i <= r; i++) {
            if(b[i] != a[i]) return false;
        }
        return true;
    }
}
