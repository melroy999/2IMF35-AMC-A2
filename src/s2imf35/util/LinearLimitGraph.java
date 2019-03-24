package s2imf35.util;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;

public class LinearLimitGraph {
    public static void main(String[] args) {
        long max = Long.MAX_VALUE;

        for(int d = 2; d < 41; d += 2) {
            // Calculate the safe value. First, find the d/2th root.
            long root = (long) Math.pow(max, 1.0/(d / 2));

            // Now, we know that root^(d/2) = max. However, we do not want that the addition of B[0] overflows.
            // We know that B[0] = root^(d/2-1).
            long b0 = (long) Math.pow(root, d/2 - 1);

            // Given b0, we can recalculate root.
            root = (long) Math.pow(max - b0, 1.0/(d / 2));

            // We also have that our calculated value is not the full value; we have to multiply by d/2.
//            System.out.println("" + d + " & " + (root * (d / 2)) + "\\\\");

            // How many random trials will we do?
            int n = 10000001;

            long[] footprints = new long[n];

            // Generate random sums, and see how close they get to max.
            for(int i = 0; i < n; i++) {
                long[] components = randSum(d / 2, root * (d / 2));

                // Maximize b0.
                Arrays.sort(components);

                // Find the B vector.
                long[] B = new long[d / 2];

                // Construct the B vector by multiplying each value with the previously calculated value.
                B[B.length - 1] = 1L;
                for(int j = B.length - 1; j > 0; j--) {
                    B[j - 1] = (components[j] + 1) * B[j];
                }

                // Check the footprint of the components by calculating the max value, plus B[0].
                for(int j = 0; j < B.length; j++) {
                    footprints[i] += components[j] * B[j];
                }

                if(d > 2) footprints[i] += B[0];

                if(footprints[i] < 0) {
                    // Square root is not precise.
                    i--;
                }
            }

            // Find the min, max and median of the footprint array.
//            System.out.println(d + " " + footprints[0] + " " + footprints[(n - 1) / 2] + " " + footprints[n - 1]);
            Arrays.sort(footprints);

            int min_factor = (int) ((double) max / footprints[0]);
            int average_factor = (int) ((double) max / average(footprints));
            int median_factor = (int) ((double) max / footprints[(n - 1) / 2]);
            int max_factor = (int) ((double) max / footprints[n - 1]);

            System.out.println(d + " " + average_factor + " " + median_factor + " " + min_factor + " " + max_factor);
        }
    }

    private static long average(long[] a) {
        BigInteger result = BigInteger.ZERO;
        for(long v : a) {
            result = result.add(BigInteger.valueOf(v));
        }
        return result.divideAndRemainder(BigInteger.valueOf(a.length))[0].longValueExact();
    }

    // Source @https://stackoverflow.com/a/32136220
    private static long[] randSum(int n, long m) {
        long[] nums = new long[n];

        if(m <= 0)
            throw new IllegalArgumentException();

        for(int i=1; i<nums.length; i++) {
            nums[i] = ThreadLocalRandom.current().nextLong(m);
        }

        Arrays.sort(nums, 1, nums.length);

        for(int i=1; i < nums.length; i++) {
            nums[i-1] = nums[i] - nums[i-1];
        }

        nums[nums.length-1] = m - nums[nums.length-1];
        return nums;
    }
}
