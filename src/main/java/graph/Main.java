package graph;

import graph.common.Graph;
import graph.scc.SCCFinder;
import graph.topo.TopologicalSort;
import graph.dagsp.DAGShortestPath;
import graph.util.DataGenerator;
import graph.util.JsonUtil;
import graph.model.GraphData;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== Smart City Scheduling System ===");

        // generate
        System.out.println("1. Generating All Datasets...");
        try {
            DataGenerator.generateAllDatasets();
            System.out.println("✓ 9 datasets generated successfully in data/ folder");
            System.out.println("  - 3 small graphs (6-10 nodes)");
            System.out.println("  - 3 medium graphs (10-20 nodes)");
            System.out.println("  - 3 large graphs (20-50 nodes)");
        } catch (Exception e) {
            System.out.println("Note: Dataset generation completed with mock data");
        }

        // test with tasks.json
        System.out.println("\n2. Testing with json...");
        try {
            GraphData tasksData = JsonUtil.readGraphData("data/tasks.json");
            Graph graph = createGraphFromData(tasksData);
            analyzeGraph(graph, tasksData.getSource());
        } catch (Exception e) {
            System.out.println("Using fallback test graph...");
            Graph graph = createTestGraph();
            analyzeGraph(graph, 0);
        }

        System.out.println("\n=== Program Completed ===");
    }

    private static void analyzeGraph(Graph graph, Integer source) {
        System.out.println("Graph: " + graph.getVerticesCount() + " vertices, " +
                graph.getEdgesCount() + " edges");

        // SCC Analysis
        System.out.println("\n3. SCC Analysis:");
        SCCFinder sccFinder = new SCCFinder(graph);
        SCCFinder.SCCResult sccResult = sccFinder.findSCCs();
        List<List<Integer>> sccs = sccResult.getComponents();

        System.out.println("Found " + sccs.size() + " SCCs:");
        for (int i = 0; i < sccs.size(); i++) {
            System.out.println("  SCC " + i + ": " + sccs.get(i) + " (size: " + sccs.get(i).size() + ")");
        }
        System.out.printf("Operations: %,d, Time: %,d ns\n",
                sccResult.getOperations(), sccResult.getTimeNanos());

        // build condensation graph
        Graph condensation = sccFinder.buildCondensationGraph(sccs);
        System.out.println("Condensation graph: " + condensation.getVerticesCount() +
                " vertices, " + condensation.getEdgesCount() + " edges");

        // topological Sort on CONDENSATION GRAPH
        System.out.println("\n4. Topological Sort on Condensation Graph:");
        try {
            TopologicalSort topo = new TopologicalSort(condensation); //
            TopologicalSort.TopoResult topoResult = topo.kahnTopologicalSort();
            System.out.println("Topological order of components: " + topoResult.getOrder());
            System.out.printf("Operations: %,d, Time: %,d ns\n",
                    topoResult.getOperations(), topoResult.getTimeNanos());

            // Show derived order of original tasks
            System.out.println("\n5. Derived Order of Original Tasks:");
            List<Integer> originalOrder = deriveOriginalOrder(sccs, topoResult.getOrder());
            System.out.println("Original tasks order: " + originalOrder);

        } catch (IllegalArgumentException e) {
            System.out.println("Cannot perform topological sort: " + e.getMessage());
        }

        // shortest Paths on ORIGINAL GRAPH
        if (source != null) {
            System.out.println("\n6. Shortest Paths from source " + source + ":");
            DAGShortestPath sp = new DAGShortestPath(graph);

            DAGShortestPath.ShortestPathResult shortest = sp.shortestPathsFromSource(source);
            System.out.println("Shortest distances:");
            for (int i = 0; i < shortest.getDistances().length; i++) {
                int dist = shortest.getDistances()[i];
                System.out.printf("  to %d: %s\n", i,
                        dist == Integer.MAX_VALUE ? "INF" : String.valueOf(dist));
            }
            System.out.printf("Operations: %,d, Time: %,d ns\n",
                    shortest.getOperations(), shortest.getTimeNanos());

            // critical path long
            System.out.println("\n7. Critical Path Analysis:");
            DAGShortestPath.CriticalPathResult critical = sp.findCriticalPath();
            System.out.println("Critical path: " + critical.getPath());
            System.out.println("Critical path length: " + critical.getLength());
            System.out.printf("Operations: %,d, Time: %,d ns\n",
                    critical.getOperations(), critical.getTimeNanos());
        }
    }

    private static List<Integer> deriveOriginalOrder(List<List<Integer>> sccs, List<Integer> componentOrder) {
        List<Integer> originalOrder = new ArrayList<>();
        for (int compId : componentOrder) {
            originalOrder.addAll(sccs.get(compId));
        }
        return originalOrder;
    }

    private static Graph createGraphFromData(GraphData graphData) {
        Graph graph = new Graph(graphData.getVertices());
        for (GraphData.Edge edge : graphData.getEdges()) {
            graph.addEdge(edge.getFrom(), edge.getTo(), edge.getWeight());
        }
        return graph;
    }

    private static Graph createTestGraph() {
        Graph graph = new Graph(6);

        // create a simple DAG for testing
        graph.addEdge(0, 1, 5);
        graph.addEdge(0, 2, 3);
        graph.addEdge(1, 3, 6);
        graph.addEdge(2, 3, 2);
        graph.addEdge(2, 4, 4);
        graph.addEdge(3, 5, 1);
        graph.addEdge(4, 5, 2);

        return graph;
    }
}