package s2imf35;

public class PerformanceCounter {
    // The time the calculation took in milliseconds.
    public long duration = 0;

    // The total number of iterations.
    public long i = 0;

    // The total number of T skips.
    public long tSkips = 0;

    // The number of iterations in which a lift value has been calculated.
    public long lifted = 0;

    // The number of iterations in which an entry in the progress measure table has been updated.
    public long changed = 0;

    @Override
    public String toString() {
        return "i=" + i +
                ", tSkips=" + tSkips +
                ", lifted=" + lifted +
                ", changed=" + changed +
                ", changed/lifted=" + Math.round(changed * 100.0 / lifted) / 100.0;
    }
}
