package graph.model;

import java.util.List;
import java.util.ArrayList;

/**
 * Simplified GraphData model without Jackson dependencies
 */
public class GraphData {
    private boolean directed;
    private int vertices;
    private List<Edge> edges;
    private Integer source;
    private String weightModel;

    public GraphData() {
        this.edges = new ArrayList<>();
    }

    public GraphData(boolean directed, int vertices, List<Edge> edges, Integer source, String weightModel) {
        this.directed = directed;
        this.vertices = vertices;
        this.edges = edges != null ? edges : new ArrayList<>();
        this.source = source;
        this.weightModel = weightModel;
    }

    public boolean isDirected() { return directed; }
    public void setDirected(boolean directed) { this.directed = directed; }

    public int getVertices() { return vertices; }
    public void setVertices(int vertices) { this.vertices = vertices; }

    public List<Edge> getEdges() { return edges; }
    public void setEdges(List<Edge> edges) { this.edges = edges; }

    public Integer getSource() { return source; }
    public void setSource(Integer source) { this.source = source; }

    public String getWeightModel() { return weightModel; }
    public void setWeightModel(String weightModel) { this.weightModel = weightModel; }

    public void addEdge(int from, int to, int weight) {
        this.edges.add(new Edge(from, to, weight));
    }

    public static class Edge {
        private int from;
        private int to;
        private int weight;

        public Edge() {}

        public Edge(int from, int to, int weight) {
            this.from = from;
            this.to = to;
            this.weight = weight;
        }

        public int getFrom() { return from; }
        public void setFrom(int from) { this.from = from; }

        public int getTo() { return to; }
        public void setTo(int to) { this.to = to; }

        public int getWeight() { return weight; }
        public void setWeight(int weight) { this.weight = weight; }

        @Override
        public String toString() {
            return from + "->" + to + "(" + weight + ")";
        }
    }

    @Override
    public String toString() {
        return "GraphData{vertices=" + vertices + ", edges=" + edges.size() + ", directed=" + directed + "}";
    }
}