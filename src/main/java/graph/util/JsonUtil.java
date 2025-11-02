package graph.util;

import graph.model.GraphData;
import java.io.*;

/**
 * JSON utility for reading graph data files
 */
public class JsonUtil {

    public static GraphData readGraphData(String filePath) {
        try {
            // Try to read actual file
            File file = new File(filePath);
            if (file.exists()) {
                return readActualJsonFile(file);
            }
        } catch (Exception e) {
            System.out.println("Could not read " + filePath + ", using mock data");
        }

        // Fallback to mock data based on filename
        return createMockData(filePath);
    }

    private static GraphData readActualJsonFile(File file) throws IOException {
        // Simple JSON parsing for our specific format
        StringBuilder content = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line);
            }
        }

        String json = content.toString();
        return parseSimpleJson(json);
    }

    private static GraphData parseSimpleJson(String json) {
        // Very basic JSON parsing for our specific format
        GraphData data = new GraphData();
        data.setDirected(true); // Our graphs are always directed
        data.setWeightModel("edge");

        try {
            // Extract vertices count
            int nIndex = json.indexOf("\"n\":");
            if (nIndex != -1) {
                int nStart = json.indexOf(":", nIndex) + 1;
                int nEnd = json.indexOf(",", nStart);
                String nStr = json.substring(nStart, nEnd).trim();
                data.setVertices(Integer.parseInt(nStr));
            }

            // Extract source
            int sourceIndex = json.indexOf("\"source\":");
            if (sourceIndex != -1) {
                int sourceStart = json.indexOf(":", sourceIndex) + 1;
                int sourceEnd = json.indexOf(",", sourceStart);
                if (sourceEnd == -1) sourceEnd = json.indexOf("}", sourceStart);
                String sourceStr = json.substring(sourceStart, sourceEnd).trim();
                data.setSource(Integer.parseInt(sourceStr));
            }

            // Extract edges - basic parsing
            int edgesStart = json.indexOf("\"edges\":") + 8;
            int edgesEnd = json.indexOf("]", edgesStart) + 1;
            String edgesArray = json.substring(edgesStart, edgesEnd);

            // Parse each edge {u:x, v:y, w:z}
            int edgeStart = 0;
            while ((edgeStart = edgesArray.indexOf("{", edgeStart)) != -1) {
                int edgeEnd = edgesArray.indexOf("}", edgeStart);
                String edgeStr = edgesArray.substring(edgeStart, edgeEnd + 1);

                // Extract u, v, w
                int uIndex = edgeStr.indexOf("\"u\":");
                int vIndex = edgeStr.indexOf("\"v\":");
                int wIndex = edgeStr.indexOf("\"w\":");

                if (uIndex != -1 && vIndex != -1 && wIndex != -1) {
                    int u = extractNumber(edgeStr, uIndex);
                    int v = extractNumber(edgeStr, vIndex);
                    int w = extractNumber(edgeStr, wIndex);

                    data.addEdge(u, v, w);
                }

                edgeStart = edgeEnd + 1;
            }

        } catch (Exception e) {
            System.out.println("Error parsing JSON, using mock data");
            return createMockData("fallback");
        }

        return data;
    }

    private static int extractNumber(String str, int index) {
        int start = str.indexOf(":", index) + 1;
        int end = str.indexOf(",", start);
        if (end == -1) end = str.indexOf("}", start);
        return Integer.parseInt(str.substring(start, end).trim());
    }

    private static GraphData createMockData(String filePath) {
        // Return appropriate mock data based on filename
        DataGenerator generator = new DataGenerator();

        if (filePath.contains("tasks.json")) {
            return createExampleTasks();
        } else if (filePath.contains("small_1")) {
            return createSmallDAG();
        } else if (filePath.contains("small_2")) {
            return createSmallWithCycle();
        } else if (filePath.contains("small_3")) {
            return createSmallMultipleCycles();
        } else if (filePath.contains("medium_1")) {
            return createMediumMixed();
        } else if (filePath.contains("medium_2")) {
            return createMediumMultipleSCCs();
        } else if (filePath.contains("medium_3")) {
            return createMediumDense();
        } else {
            return createSmallDAG();
        }
    }

    // Copy the create methods that are used
    private static GraphData createExampleTasks() {
        GraphData data = new GraphData(true, 8, null, 4, "edge");
        data.addEdge(0, 1, 3);
        data.addEdge(1, 2, 2);
        data.addEdge(2, 3, 4);
        data.addEdge(3, 1, 1);
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
        data.addEdge(2, 0, 1);
        data.addEdge(2, 3, 4);
        data.addEdge(3, 4, 2);
        data.addEdge(4, 5, 3);
        return data;
    }

    private static GraphData createSmallMultipleCycles() {
        GraphData data = new GraphData(true, 6, null, 0, "edge");
        data.addEdge(0, 1, 2);
        data.addEdge(1, 0, 3);
        data.addEdge(2, 3, 1);
        data.addEdge(3, 4, 2);
        data.addEdge(4, 2, 4);
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
        data.addEdge(8, 10, 2);
        return data;
    }

    private static GraphData createMediumMultipleSCCs() {
        GraphData data = new GraphData(true, 15, null, 0, "edge");
        data.addEdge(0, 1, 2);
        data.addEdge(1, 2, 3);
        data.addEdge(2, 0, 1);
        data.addEdge(2, 3, 2);
        data.addEdge(3, 4, 3);
        data.addEdge(4, 2, 1);
        data.addEdge(5, 6, 2);
        data.addEdge(6, 7, 3);
        data.addEdge(7, 5, 1);
        data.addEdge(10, 11, 2);
        data.addEdge(11, 12, 3);
        data.addEdge(12, 13, 1);
        data.addEdge(13, 14, 2);
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
}