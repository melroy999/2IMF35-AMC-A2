package s2imf35.util;

public class LinearLimitGraph {
    public static void main(String[] args) {
        long max = Long.MAX_VALUE;

        for(int d = 2; d < 40; d++) {
            // Calculate the safe value. First, find the d/2th root.
            long root = (long) Math.pow(max, 1.0/(d / 2));

            // Now, we know that root^(d/2) = max. However, we do not want that the addition of B[0] overflows.
            // We know that B[0] = root^(d/2-1).
            long b0 = (long) Math.pow(root, d/2 - 1);

            // Given b0, we can recalculate root.
            root = (long) Math.pow(max - b0, 1.0/(d / 2));

            // We also have that our calculated value is not the full value; we have to multiply by d/2.
            System.out.println("d = " + d + ": " + (root * (d / 2)));
        }
    }
}
