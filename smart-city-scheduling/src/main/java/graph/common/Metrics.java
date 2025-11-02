package graph.common;

public interface Metrics {


    void reset();

    long getOperationCount();

    long getTimeNanos();

    void incrementOperationCount();

    void incrementOperationCount(int delta);
}