package graph.dagsp;

import graph.common.Graph;
import graph.common.BaseMetrics;
import graph.topo.TopologicalSort;
import java.util.*;

public class DAGShortestPath extends BaseMetrics {
    private Graph graph;

    public DAGShortestPath(Graph graph) {
        this.graph = graph;
    }

    public ShortestPathResult shortestPathsFromSource(int source) {
        reset();

        int vertices = graph.getVerticesCount();
        int[] dist = new int[vertices];
        int[] prev = new int[vertices];
        Arrays.fill(dist, Integer.MAX_VALUE);
        Arrays.fill(prev, -1);
        dist[source] = 0;

        // Get topological order
        TopologicalSort topo = new TopologicalSort(graph);
        List<Integer> topoOrder = topo.kahnTopologicalSort().getOrder();
        incrementOperationCount(topo.getOperationCount());

        // Process vertices in topological order
        for (int u : topoOrder) {
            if (dist[u] != Integer.MAX_VALUE) {
                for (int v : graph.getAdjacent(u)) {
                    int weight = graph.getWeight(u, v);
                    if (dist[u] + weight < dist[v]) {
                        dist[v] = dist[u] + weight;
                        prev[v] = u;
                    }
                    incrementOperationCount();
                }
            }
            incrementOperationCount();
        }

        return new ShortestPathResult(dist, prev, getOperationCount(), getTimeNanos());
    }

    public ShortestPathResult longestPathsFromSource(int source) {
        reset();

        int vertices = graph.getVerticesCount();
        int[] dist = new int[vertices];
        int[] prev = new int[vertices];
        Arrays.fill(dist, Integer.MIN_VALUE);
        Arrays.fill(prev, -1);
        dist[source] = 0;

        // Get topological order
        TopologicalSort topo = new TopologicalSort(graph);
        List<Integer> topoOrder = topo.kahnTopologicalSort().getOrder();
        incrementOperationCount(topo.getOperationCount());

        // Process vertices in topological order for longest path
        for (int u : topoOrder) {
            if (dist[u] != Integer.MIN_VALUE) {
                for (int v : graph.getAdjacent(u)) {
                    int weight = graph.getWeight(u, v);
                    if (dist[u] + weight > dist[v]) {
                        dist[v] = dist[u] + weight;
                        prev[v] = u;
                    }
                    incrementOperationCount();
                }
            }
            incrementOperationCount();
        }

        return new ShortestPathResult(dist, prev, getOperationCount(), getTimeNanos());
    }

    public CriticalPathResult findCriticalPath() {
        reset();

        int vertices = graph.getVerticesCount();
        int maxLength = Integer.MIN_VALUE;
        List<Integer> criticalPath = new ArrayList<>();
        int bestEnd = -1;
        int bestStart = -1;
        int[] bestPrev = null;

        // Try all possible sources
        for (int source = 0; source < vertices; source++) {
            ShortestPathResult result = longestPathsFromSource(source);
            incrementOperationCount(result.getOperations());

            for (int i = 0; i < vertices; i++) {
                if (result.getDistances()[i] > maxLength && result.getDistances()[i] != Integer.MIN_VALUE) {
                    maxLength = result.getDistances()[i];
                    bestEnd = i;
                    bestStart = source;
                    bestPrev = result.getPredecessors();
                }
                incrementOperationCount();
            }
        }

        if (bestPrev != null && bestStart != -1) {
            criticalPath = reconstructPath(bestPrev, bestStart, bestEnd);
        }

        return new CriticalPathResult(criticalPath, maxLength, getOperationCount(), getTimeNanos());
    }

    private List<Integer> reconstructPath(int[] prev, int source, int target) {
        List<Integer> path = new ArrayList<>();
        for (int at = target; at != -1; at = prev[at]) {
            path.add(at);
        }
        Collections.reverse(path);
        return path;
    }

    public static class ShortestPathResult {
        private final int[] distances;
        private final int[] predecessors;
        private final long operations;
        private final long timeNanos;

        public ShortestPathResult(int[] distances, int[] predecessors, long operations, long timeNanos) {
            this.distances = distances;
            this.predecessors = predecessors;
            this.operations = operations;
            this.timeNanos = timeNanos;
        }

        public int[] getDistances() { return distances; }
        public int[] getPredecessors() { return predecessors; }
        public long getOperations() { return operations; }
        public long getTimeNanos() { return timeNanos; }
    }

    public static class CriticalPathResult {
        private final List<Integer> path;
        private final int length;
        private final long operations;
        private final long timeNanos;

        public CriticalPathResult(List<Integer> path, int length, long operations, long timeNanos) {
            this.path = path;
            this.length = length;
            this.operations = operations;
            this.timeNanos = timeNanos;
        }

        public List<Integer> getPath() { return path; }
        public int getLength() { return length; }
        public long getOperations() { return operations; }
        public long getTimeNanos() { return timeNanos; }
    }
}