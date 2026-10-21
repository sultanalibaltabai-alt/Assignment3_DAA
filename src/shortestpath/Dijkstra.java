package shortestpath;

import graph.Edge;
import graph.Graph;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.PriorityQueue;

public class Dijkstra {
    public static final int UNREACHABLE = Integer.MAX_VALUE;

    private final Graph graph;
    private final int source;
    private final int[] distances;
    private final int[] previous;
    private long edgeExaminations;
    private long queueOperations;

    public Dijkstra(Graph graph, int source) {
        for (Edge edge : graph.getEdges()) {
            if (edge.getWeight() < 0) {
                throw new IllegalArgumentException("Negative weights are not allowed");
            }
        }
        this.graph = graph;
        this.source = source;
        distances = new int[graph.getVertexCount()];
        previous = new int[graph.getVertexCount()];
    }

    public void run() {
        Arrays.fill(distances, UNREACHABLE);
        Arrays.fill(previous, -1);
        edgeExaminations = 0;
        queueOperations = 0;

        PriorityQueue<VertexDistance> queue = new PriorityQueue<>();
        distances[source] = 0;
        queue.add(new VertexDistance(source, 0));
        queueOperations++;

        while (!queue.isEmpty()) {
            VertexDistance current = queue.poll();
            queueOperations++;
            int vertex = current.getVertex();

            if (current.getDistance() > distances[vertex]) {
                continue;
            }

            for (Edge edge : graph.getNeighbors(vertex)) {
                edgeExaminations++;
                int newDistance = distances[vertex] + edge.getWeight();
                if (newDistance < distances[edge.getTo()]) {
                    distances[edge.getTo()] = newDistance;
                    previous[edge.getTo()] = vertex;
                    queue.add(new VertexDistance(edge.getTo(), newDistance));
                    queueOperations++;
                }
            }
        }
    }

    public int getDistance(int vertex) {
        return distances[vertex];
    }

    public boolean isReachable(int vertex) {
        return distances[vertex] != UNREACHABLE;
    }

    public List<Integer> getPath(int target) {
        List<Integer> path = new ArrayList<>();
        if (!isReachable(target)) {
            return path;
        }
        for (int vertex = target; vertex != -1; vertex = previous[vertex]) {
            path.add(vertex);
        }
        Collections.reverse(path);
        return path;
    }

    public long getOperations() {
        return edgeExaminations + queueOperations;
    }
}