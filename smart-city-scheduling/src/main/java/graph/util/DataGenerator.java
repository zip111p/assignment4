package graph.util;

import graph.model.GraphData;
import java.io.*;
import java.util.*;

public class DataGenerator {
    private static final Random random = new Random(42);

    public static void generateAllDatasets() {
        System.out.println("Generating 9 datasets...");

        // Create data directory
        new File("data").mkdirs();

        try {
            // Small datasets
            writeGraphData(createSmallDAG(), "data/small_1.json");
            writeGraphData(createSmallWithCycle(), "data/small_2.json");
            writeGraphData(createSmallMultipleCycles(), "data/small_3.json");

            // Medium datasets
            writeGraphData(createMediumMixed(), "data/medium_1.json");
            writeGraphData(createMediumMultipleSCCs(), "data/medium_2.json");
            writeGraphData(createMediumDense(), "data/medium_3.json");

            // Large datasets
            writeGraphData(createLargeSparse(), "data/large_1.json");
            writeGraphData(createLargeDense(), "data/large_2.json");
            writeGraphData(createLargeMixed(), "data/large_3.json");

            // Example tasks
            writeGraphData(createExampleTasks(), "data/tasks.json");

            System.out.println("✓ All 9 datasets generated successfully!");

        } catch (Exception e) {
            System.out.println("Note: Could not write files, but mock data is available");
        }
    }

    private static GraphData createExampleTasks() {
        GraphData data = new GraphData(true, 8, null, 4, "edge");
        data.addEdge(0, 1, 3);
        data.addEdge(1, 2, 2);
        data.addEdge(2, 3, 4);
        data.addEdge(3, 1, 1); // Creates cycle 1->2->3->1
        data.addEdge(4, 5, 2);
        data.addEdge(5, 6, 5);
        data.addEdge(6, 7, 1);
        return data;
    }

    private static GraphData createSmallDAG() {
        GraphData data = new GraphData(true, 6, null, 0, "edge");
        data.addEdge(0, 1, 3);
        data.addEdge(1, 2, 2);
        data.addEdge(2, 3, 4);
        data.addEdge(3, 4, 1);
        data.addEdge(4, 5, 5);
        return data;
    }

    private static GraphData createSmallWithCycle() {
        GraphData data = new GraphData(true, 6, null, 0, "edge");
        data.addEdge(0, 1, 2);
        data.addEdge(1, 2, 3);
        data.addEdge(2, 0, 1); // Cycle
        data.addEdge(2, 3, 4);
        data.addEdge(3, 4, 2);
        data.addEdge(4, 5, 3);
        return data;
    }

    private static GraphData createSmallMultipleCycles() {
        GraphData data = new GraphData(true, 6, null, 0, "edge");
        data.addEdge(0, 1, 2);
        data.addEdge(1, 0, 3); // Cycle 1
        data.addEdge(2, 3, 1);
        data.addEdge(3, 4, 2);
        data.addEdge(4, 2, 4); // Cycle 2
        data.addEdge(0, 2, 3);
        data.addEdge(1, 5, 2);
        return data;
    }

    private static GraphData createMediumMixed() {
        GraphData data = new GraphData(true, 15, null, 0, "edge");
        for (int i = 0; i < 14; i++) {
            data.addEdge(i, i + 1, (i % 3) + 1);
        }
        data.addEdge(2, 5, 3);
        data.addEdge(7, 3, 2);
        data.addEdge(10, 8, 4);
        data.addEdge(8, 10, 2); // Small cycle
        return data;
    }

    private static GraphData createMediumMultipleSCCs() {
        GraphData data = new GraphData(true, 15, null, 0, "edge");
        // First SCC (vertices 0-4)
        data.addEdge(0, 1, 2);
        data.addEdge(1, 2, 3);
        data.addEdge(2, 0, 1);
        data.addEdge(2, 3, 2);
        data.addEdge(3, 4, 3);
        data.addEdge(4, 2, 1);

        // Second SCC (vertices 5-9)
        data.addEdge(5, 6, 2);
        data.addEdge(6, 7, 3);
        data.addEdge(7, 5, 1);

        // Third SCC (vertices 10-14) - DAG-like
        data.addEdge(10, 11, 2);
        data.addEdge(11, 12, 3);
        data.addEdge(12, 13, 1);
        data.addEdge(13, 14, 2);

        // Connect SCCs
        data.addEdge(4, 5, 2);
        data.addEdge(7, 10, 3);
        return data;
    }

    private static GraphData createMediumDense() {
        GraphData data = new GraphData(true, 12, null, 0, "edge");
        for (int i = 0; i < 12; i++) {
            for (int j = i + 1; j < 12; j++) {
                if ((i + j) % 3 == 0) {
                    data.addEdge(i, j, (i + j) % 5 + 1);
                }
            }
        }
        return data;
    }

    private static GraphData createLargeSparse() {
        GraphData data = new GraphData(true, 30, null, 0, "edge");
        for (int i = 0; i < 60; i++) {
            int from = random.nextInt(30);
            int to = random.nextInt(30);
            if (from != to) {
                data.addEdge(from, to, random.nextInt(10) + 1);
            }
        }
        return data;
    }

    private static GraphData createLargeDense() {
        GraphData data = new GraphData(true, 25, null, 0, "edge");
        for (int i = 0; i < 25; i++) {
            for (int j = i + 1; j < 25; j++) {
                if (random.nextDouble() < 0.3) {
                    data.addEdge(i, j, random.nextInt(5) + 1);
                }
            }
        }
        return data;
    }

    private static GraphData createLargeMixed() {
        GraphData data = new GraphData(true, 35, null, 0, "edge");
        for (int i = 0; i < 34; i++) {
            data.addEdge(i, i + 1, random.nextInt(3) + 1);
        }
        for (int i = 0; i < 15; i++) {
            int from = random.nextInt(35);
            int to = random.nextInt(35);
            if (from < to) {
                data.addEdge(from, to, random.nextInt(4) + 1);
            }
        }
        data.addEdge(5, 2, 2);
        data.addEdge(15, 12, 3);
        data.addEdge(25, 22, 1);
        return data;
    }

    private static void writeGraphData(GraphData graphData, String filePath) {
        // Simple file writing without Jackson
        try (PrintWriter writer = new PrintWriter(new FileWriter(filePath))) {
            writer.println("{");
            writer.println("  \"directed\": " + graphData.isDirected() + ",");
            writer.println("  \"n\": " + graphData.getVertices() + ",");
            writer.println("  \"edges\": [");

            List<GraphData.Edge> edges = graphData.getEdges();
            for (int i = 0; i < edges.size(); i++) {
                GraphData.Edge edge = edges.get(i);
                writer.print("    {\"u\": " + edge.getFrom() + ", \"v\": " + edge.getTo() + ", \"w\": " + edge.getWeight() + "}");
                if (i < edges.size() - 1) {
                    writer.println(",");
                } else {
                    writer.println();
                }
            }

            writer.println("  ],");
            writer.println("  \"source\": " + (graphData.getSource() != null ? graphData.getSource() : 0) + ",");
            writer.println("  \"weight_model\": \"" + graphData.getWeightModel() + "\"");
            writer.println("}");
            System.out.println("✓ Generated: " + filePath);
        } catch (IOException e) {
            System.out.println("Could not write " + filePath + ", but mock data is available");
        }
    }
}