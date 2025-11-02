package graph.scc;

import graph.common.Graph;
import graph.common.BaseMetrics;
import java.util.*;

public class SCCFinder extends BaseMetrics {
    private Graph graph;
    private boolean[] visited;
    private Stack<Integer> stack;

    public SCCFinder(Graph graph) {
        this.graph = graph;
    }

    public SCCResult findSCCs() {
        reset();

        // First pass fill stack with finishing times
        visited = new boolean[graph.getVerticesCount()];
        stack = new Stack<>();

        for (int i = 0; i < graph.getVerticesCount(); i++) {
            if (!visited[i]) {
                dfsFirstPass(i);
            }
            incrementOperationCount();
        }

        // Second pass process in reverse order
        visited = new boolean[graph.getVerticesCount()];
        List<List<Integer>> sccs = new ArrayList<>();

        while (!stack.isEmpty()) {
            int vertex = stack.pop();
            if (!visited[vertex]) {
                List<Integer> scc = new ArrayList<>();
                dfsSecondPass(vertex, scc);
                sccs.add(scc);
            }
            incrementOperationCount();
        }

        return new SCCResult(sccs, getOperationCount(), getTimeNanos());
    }

    private void dfsFirstPass(int vertex) {
        visited[vertex] = true;
        incrementOperationCount();

        for (int neighbor : graph.getAdjacent(vertex)) {
            if (!visited[neighbor]) {
                dfsFirstPass(neighbor);
            }
            incrementOperationCount();
        }
        stack.push(vertex);
    }

    private void dfsSecondPass(int vertex, List<Integer> scc) {
        visited[vertex] = true;
        scc.add(vertex);
        incrementOperationCount();

        for (int neighbor : graph.getReverseAdjacent(vertex)) {
            if (!visited[neighbor]) {
                dfsSecondPass(neighbor, scc);
            }
            incrementOperationCount();
        }
    }

    public Graph buildCondensationGraph(List<List<Integer>> sccs) {
        reset();

        // Map vertex to its component ID
        int[] componentId = new int[graph.getVerticesCount()];
        for (int i = 0; i < sccs.size(); i++) {
            for (int vertex : sccs.get(i)) {
                componentId[vertex] = i;
                incrementOperationCount();
            }
        }

        // Build condensation graph
        Graph condensation = new Graph(sccs.size());
        Set<String> edgesAdded = new HashSet<>();

        for (int u = 0; u < graph.getVerticesCount(); u++) {
            for (int v : graph.getAdjacent(u)) {
                int compU = componentId[u];
                int compV = componentId[v];
                if (compU != compV) {
                    String edgeKey = compU + "-" + compV;
                    if (!edgesAdded.contains(edgeKey)) {
                        // Use minimum weight for condensation edges
                        condensation.addEdge(compU, compV, 1);
                        edgesAdded.add(edgeKey);
                    }
                }
                incrementOperationCount();
            }
        }

        return condensation;
    }

    public static class SCCResult {
        private final List<List<Integer>> components;
        private final long operations;
        private final long timeNanos;

        public SCCResult(List<List<Integer>> components, long operations, long timeNanos) {
            this.components = components;
            this.operations = operations;
            this.timeNanos = timeNanos;
        }

        public List<List<Integer>> getComponents() { return components; }
        public long getOperations() { return operations; }
        public long getTimeNanos() { return timeNanos; }
    }
}