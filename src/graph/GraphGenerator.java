package graph;

import java.util.Random;

public class GraphGenerator {
    private static final int MAX_WEIGHT = 100;

    private final Random random;

    public GraphGenerator(long seed) {
        random = new Random(seed);
    }

    public Graph createConnectedGraph(int vertexCount, int edgeCount) {
        Graph graph = new Graph(vertexCount, false);
        boolean[][] used = new boolean[vertexCount][vertexCount];

        for (int vertex = 1; vertex < vertexCount; vertex++) {
            int otherVertex = random.nextInt(vertex);
            addEdge(graph, used, vertex, otherVertex);
        }
        addRandomEdges(graph, used, edgeCount, false);
        return graph;
    }

    public Graph createDirectedGraph(int vertexCount, int edgeCount) {
        Graph graph = new Graph(vertexCount, true);
        boolean[][] used = new boolean[vertexCount][vertexCount];

        addRandomEdges(graph, used, edgeCount, false);
        return graph;
    }

    public Graph createDag(int vertexCount, int edgeCount) {
        Graph graph = new Graph(vertexCount, true);
        boolean[][] used = new boolean[vertexCount][vertexCount];

        addRandomEdges(graph, used, edgeCount, true);
        return graph;
    }

    private void addRandomEdges(Graph graph, boolean[][] used, int edgeCount, boolean smallerToLarger) {
        int vertexCount = graph.getVertexCount();

        while (graph.getEdgeCount() < edgeCount) {
            int from = random.nextInt(vertexCount);
            int to = random.nextInt(vertexCount);

            if (smallerToLarger && from > to) {
                int temp = from;
                from = to;
                to = temp;
            }
            if (from != to && !used[from][to]) {
                addEdge(graph, used, from, to);
            }
        }
    }

    private void addEdge(Graph graph, boolean[][] used, int from, int to) {
        used[from][to] = true;
        if (!graph.isDirected()) {
            used[to][from] = true;
        }
        int weight = random.nextInt(MAX_WEIGHT) + 1;
        graph.addEdge(from, to, weight);
    }
}