package graph;

import graph.common.Graph;
import graph.scc.SCCFinder;
import graph.topo.TopologicalSort;
import graph.dagsp.DAGShortestPath;

public class AlgorithmTests {

    public static void main(String[] args) {
        System.out.println("=== Algorithm Tests ===");

        runAllTests();

        System.out.println("=== All Tests Completed ===");
    }

    private static void runAllTests() {
        testSCC();
        testTopologicalSort();
        testShortestPath();
        testCriticalPath();
    }

    private static void testSCC() {
        System.out.println("\n1. Testing SCC Finder...");

        // Test 1 Simple DAG
        Graph dag = new Graph(4);
        dag.addEdge(0, 1, 1);
        dag.addEdge(1, 2, 1);
        dag.addEdge(2, 3, 1);

        SCCFinder finder = new SCCFinder(dag);
        SCCFinder.SCCResult result = finder.findSCCs();

        if (result.getComponents().size() == 4) {
            System.out.println("✓ SCC Test 1 PASSED: DAG has 4 SCCs");
        } else {
            System.out.println("✗ SCC Test 1 FAILED");
        }

        // Test 2 Graph with cycle
        Graph cyclic = new Graph(3);
        cyclic.addEdge(0, 1, 1);
        cyclic.addEdge(1, 2, 1);
        cyclic.addEdge(2, 0, 1);

        SCCFinder finder2 = new SCCFinder(cyclic);
        SCCFinder.SCCResult result2 = finder2.findSCCs();

        if (result2.getComponents().size() == 1) {
            System.out.println("✓ SCC Test 2 PASSED: Cycle has 1 SCC");
        } else {
            System.out.println("✗ SCC Test 2 FAILED");
        }
    }

    private static void testTopologicalSort() {
        System.out.println("\n2. Testing Topological Sort...");

        Graph dag = new Graph(5);
        dag.addEdge(0, 1, 1);
        dag.addEdge(1, 2, 1);
        dag.addEdge(2, 3, 1);
        dag.addEdge(3, 4, 1);

        TopologicalSort topo = new TopologicalSort(dag);
        try {
            TopologicalSort.TopoResult result = topo.kahnTopologicalSort();
            if (result.getOrder().size() == 5) {
                System.out.println("✓ Topological Sort Test PASSED");
            } else {
                System.out.println("✗ Topological Sort Test FAILED");
            }
        } catch (Exception e) {
            System.out.println("✗ Topological Sort Test FAILED: " + e.getMessage());
        }
    }

    private static void testShortestPath() {
        System.out.println("\n3. Testing Shortest Path...");

        Graph graph = new Graph(4);
        graph.addEdge(0, 1, 2);
        graph.addEdge(0, 2, 5);
        graph.addEdge(1, 3, 1);
        graph.addEdge(2, 3, 1);

        DAGShortestPath sp = new DAGShortestPath(graph);
        DAGShortestPath.ShortestPathResult result = sp.shortestPathsFromSource(0);

        if (result.getDistances()[3] == 3) { // 0->1->3 = 2+1=3
            System.out.println("✓ Shortest Path Test PASSED");
        } else {
            System.out.println("✗ Shortest Path Test FAILED");
        }
    }

    private static void testCriticalPath() {
        System.out.println("\n4. Testing Critical Path...");

        Graph graph = new Graph(4);
        graph.addEdge(0, 1, 2);
        graph.addEdge(0, 2, 5);
        graph.addEdge(1, 3, 1);
        graph.addEdge(2, 3, 1);

        DAGShortestPath sp = new DAGShortestPath(graph);
        DAGShortestPath.CriticalPathResult result = sp.findCriticalPath();

        if (result.getLength() == 6) { // 0->2->3 = 5+1=6
            System.out.println("✓ Critical Path Test PASSED");
        } else {
            System.out.println("✗ Critical Path Test FAILED");
        }
    }
}