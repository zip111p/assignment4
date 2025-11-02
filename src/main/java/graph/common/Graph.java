package graph.common;

import java.util.*;

public class Graph {
    private final int vertices;
    private final List<List<Integer>> adj;
    private final List<List<Integer>> reverseAdj;
    private final int[][] weights;

    public Graph(int vertices) {
        this.vertices = vertices;
        this.adj = new ArrayList<>();
        this.reverseAdj = new ArrayList<>();
        this.weights = new int[vertices][vertices];

        for (int i = 0; i < vertices; i++) {
            adj.add(new ArrayList<>());
            reverseAdj.add(new ArrayList<>());
            Arrays.fill(weights[i], -1);
        }
    }

    public void addEdge(int from, int to, int weight) {
        adj.get(from).add(to);
        reverseAdj.get(to).add(from);
        weights[from][to] = weight;
    }

    public List<Integer> getAdjacent(int vertex) {
        return adj.get(vertex);
    }

    public List<Integer> getReverseAdjacent(int vertex) {
        return reverseAdj.get(vertex);
    }

    public int getWeight(int from, int to) {
        return weights[from][to];
    }

    public int getVerticesCount() {
        return vertices;
    }

    public List<List<Integer>> getAdjacencyList() {
        return adj;
    }

    public int getEdgesCount() {
        int count = 0;
        for (List<Integer> edges : adj) {
            count += edges.size();
        }
        return count;
    }
}