package s2imf35.util;

public class LinearLimitGraph {
    public static void main(String[] args) {
        double max = Long.MAX_VALUE;

        for(double d = 1; d < 20; d++) {
            System.out.println("d = " + d + ": " + (long) Math.floor(d * pow(max, d)));
        }
    }

    public static Double pow(Double base, Double n) {
        return Math.pow(Math.E, Math.log(base)/n);
    }
}
