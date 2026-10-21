package mst;

import graph.Edge;
import graph.Graph;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

public class PrimMst {
    private long edgeExaminations;
    private long queueOperations;

    public MstResult findMst(Graph graph) {
        edgeExaminations = 0;
        queueOperations = 0;

        boolean[] visited = new boolean[graph.getVertexCount()];
        PriorityQueue<Edge> queue = new PriorityQueue<>();
        List<Edge> mstEdges = new ArrayList<>();
        int totalWeight = 0;

        visited[0] = true;
        addEdgesToQueue(graph, 0, visited, queue);

        while (!queue.isEmpty()) {
            Edge edge = queue.poll();
            queueOperations++;
            if (visited[edge.getTo()]) {
                continue;
            }
            visited[edge.getTo()] = true;
            mstEdges.add(edge);
            totalWeight += edge.getWeight();
            addEdgesToQueue(graph, edge.getTo(), visited, queue);
        }

        boolean connected = mstEdges.size() == graph.getVertexCount() - 1;
        long operations = edgeExaminations + queueOperations;
        return new MstResult(mstEdges, totalWeight, connected, operations);
    }

    private void addEdgesToQueue(Graph graph, int vertex, boolean[] visited, PriorityQueue<Edge> queue) {
        for (Edge edge : graph.getNeighbors(vertex)) {
            edgeExaminations++;
            if (!visited[edge.getTo()]) {
                queue.add(edge);
                queueOperations++;
            }
        }
    }
}