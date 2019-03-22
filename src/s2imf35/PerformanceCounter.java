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

    // The number of times a vertex of the diamond player has been updated.
    public long lifted_diamond = 0;

    // The number of vertices that have been considered during lift operations.
    public long lifted_successor_count = 0;

    // The number of times the progress measure table has been updated.
    public long updated = 0;

    // The number of times the progress measure table has been updated for the diamond player.
    public long updated_diamond = 0;

    // The number of times the progress step was done choosing the even path.
    public long even_progressions = 0;

    // The number of times the progress step was done choosing the odd path.
    public long odd_progressions = 0;

    // The number of times the progress step was done choosing the T path.
    public long T_progressions = 0;

    public long min_search_steps = 0;
    public long max_search_steps = 0;
    public long min_search_step_changes = 0;
    public long max_search_step_changes = 0;

    @Override
    public String toString() {
        return "i=" + i +
                ", tSkips=" + tSkips +
                ", lifted=" + lifted +
                ", lifted_diamond=" + lifted_diamond +
                ", lifted_box=" + (lifted - lifted_diamond) +
                ", updated=" + updated +
                ", updated_diamond=" + updated_diamond +
                ", updated_box=" + (updated - updated_diamond) +
                ", updated/lifted=" + Math.round(updated * 100.0 / lifted) / 100.0 +
                ", lifted_successor_count=" + lifted_successor_count +
                ", even_progressions=" + even_progressions +
                ", odd_progressions=" + odd_progressions +
                ", T_progressions=" + T_progressions +
                ", min_search_steps=" + min_search_steps +
                ", max_search_steps=" + max_search_steps +
                ", min_search_step_changes=" + min_search_step_changes +
                ", max_search_step_changes=" + max_search_step_changes +
                ", min_search_step_no_changes=" + (min_search_steps - min_search_step_changes) +
                ", max_search_step_no_changes=" + (max_search_steps - max_search_step_changes);
    }
}
