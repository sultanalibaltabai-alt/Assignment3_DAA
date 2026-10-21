package graph;

import java.util.ArrayList;
import java.util.List;

public class Graph {
    private final int vertexCount;
    private final boolean directed;
    private final List<List<Edge>> adjacencyList = new ArrayList<>();
    private final List<Edge> edges = new ArrayList<>();

    public Graph(int vertexCount, boolean directed) {
        this.vertexCount = vertexCount;
        this.directed = directed;
        for (int vertex = 0; vertex < vertexCount; vertex++) {
            adjacencyList.add(new ArrayList<>());
        }
    }

    public void addEdge(int from, int to, int weight) {
        Edge edge = new Edge(from, to, weight);
        edges.add(edge);
        adjacencyList.get(from).add(edge);
        if (!directed) {
            adjacencyList.get(to).add(new Edge(to, from, weight));
        }
    }

    public int getVertexCount() {
        return vertexCount;
    }

    public int getEdgeCount() {
        return edges.size();
    }

    public boolean isDirected() {
        return directed;
    }

    public List<Edge> getEdges() {
        return edges;
    }

    public List<Edge> getNeighbors(int vertex) {
        return adjacencyList.get(vertex);
    }
}