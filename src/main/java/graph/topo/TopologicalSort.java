package graph.topo;

import graph.common.Graph;
import graph.common.BaseMetrics;
import java.util.*;

public class TopologicalSort extends BaseMetrics {
    private Graph graph;

    public TopologicalSort(Graph graph) {
        this.graph = graph;
    }

    public TopoResult kahnTopologicalSort() {
        reset();

        int vertices = graph.getVerticesCount();
        int[] inDegree = new int[vertices];

        for (int u = 0; u < vertices; u++) {
            for (int v : graph.getAdjacent(u)) {
                inDegree[v]++;
                incrementOperationCount();
            }
            incrementOperationCount();
        }

        Queue<Integer> queue = new LinkedList<>();
        for (int i = 0; i < vertices; i++) {
            if (inDegree[i] == 0) {
                queue.offer(i);
            }
            incrementOperationCount();
        }

        List<Integer> topoOrder = new ArrayList<>();
        int visitedCount = 0;

        while (!queue.isEmpty()) {
            int u = queue.poll();
            topoOrder.add(u);
            visitedCount++;

            for (int v : graph.getAdjacent(u)) {
                inDegree[v]--;
                if (inDegree[v] == 0) {
                    queue.offer(v);
                }
                incrementOperationCount();
            }
            incrementOperationCount();
        }

        if (visitedCount != vertices) {
            throw new IllegalArgumentException("Graph has cycles - topological sort not possible");
        }

        return new TopoResult(topoOrder, getOperationCount(), getTimeNanos());
    }

    public static class TopoResult {
        private final List<Integer> order;
        private final long operations;
        private final long timeNanos;

        public TopoResult(List<Integer> order, long operations, long timeNanos) {
            this.order = order;
            this.operations = operations;
            this.timeNanos = timeNanos;
        }

        public List<Integer> getOrder() { return order; }
        public long getOperations() { return operations; }
        public long getTimeNanos() { return timeNanos; }
    }
}