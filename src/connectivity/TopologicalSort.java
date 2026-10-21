package connectivity;

import graph.Edge;
import graph.Graph;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class TopologicalSort {
    private long edgeExaminations;
    private boolean cycleFound;

    public List<Integer> sort(Graph graph) {
        edgeExaminations = 0;
        int vertexCount = graph.getVertexCount();

        int[] inDegree = new int[vertexCount];
        for (Edge edge : graph.getEdges()) {
            inDegree[edge.getTo()]++;
        }

        Queue<Integer> queue = new LinkedList<>();
        for (int vertex = 0; vertex < vertexCount; vertex++) {
            if (inDegree[vertex] == 0) {
                queue.add(vertex);
            }
        }

        List<Integer> order = new ArrayList<>();
        while (!queue.isEmpty()) {
            int vertex = queue.poll();
            order.add(vertex);
            for (Edge edge : graph.getNeighbors(vertex)) {
                edgeExaminations++;
                inDegree[edge.getTo()]--;
                if (inDegree[edge.getTo()] == 0) {
                    queue.add(edge.getTo());
                }
            }
        }

        cycleFound = order.size() != vertexCount;
        return order;
    }

    public boolean hasCycle() {
        return cycleFound;
    }

    public long getEdgeExaminations() {
        return edgeExaminations;
    }
}