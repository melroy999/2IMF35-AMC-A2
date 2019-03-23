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

    // The number of times the progress measure table has been updated.
    public long updated = 0;

    @Override
    public String toString() {
        return "i=" + i +
                ", tSkips=" + tSkips +
                ", lifted=" + lifted +
                ", updated=" + updated +
                ", updated/lifted=" + Math.round(updated * 100.0 / lifted) / 100.0;
    }
}
