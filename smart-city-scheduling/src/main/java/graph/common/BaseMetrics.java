package graph.common;

public abstract class BaseMetrics implements Metrics {
    protected long operationCount;
    protected long startTime;


    public BaseMetrics() {
        reset();
    }

    @Override
    public void reset() {
        operationCount = 0;
        startTime = System.nanoTime();
    }

    @Override
    public long getOperationCount() {
        return operationCount;
    }

    @Override
    public long getTimeNanos() {
        return System.nanoTime() - startTime;
    }

    @Override
    public void incrementOperationCount() {
        operationCount++;
    }

    @Override
    public void incrementOperationCount(int delta) {
        operationCount += delta;
    }

    public void incrementOperationCount(long delta) {
        if (delta > Integer.MAX_VALUE) {
            operationCount += Integer.MAX_VALUE;
        } else {
            operationCount += (int) delta;
        }
    }


    public long getTimeMillis() {
        return getTimeNanos() / 1_000_000;
    }


    @Override
    public String toString() {
        return String.format("Operations: %,d, Time: %,d ns (%,d ms)",
                operationCount, getTimeNanos(), getTimeMillis());
    }
}