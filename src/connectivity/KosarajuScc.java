package connectivity;

import graph.Edge;
import graph.Graph;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class KosarajuScc {
    private boolean[] visited;
    private long edgeExaminations;

    public List<List<Integer>> findComponents(Graph graph) {
        edgeExaminations = 0;
        int vertexCount = graph.getVertexCount();

        visited = new boolean[vertexCount];
        Stack<Integer> finishOrder = new Stack<>();
        for (int vertex = 0; vertex < vertexCount; vertex++) {
            if (!visited[vertex]) {
                fillFinishOrder(graph, vertex, finishOrder);
            }
        }

        List<List<Integer>> reversedGraph = createReversedGraph(graph);

        visited = new boolean[vertexCount];
        List<List<Integer>> components = new ArrayList<>();
        while (!finishOrder.isEmpty()) {
            int vertex = finishOrder.pop();
            if (!visited[vertex]) {
                List<Integer> component = new ArrayList<>();
                collectComponent(reversedGraph, vertex, component);
                components.add(component);
            }
        }
        return components;
    }

    public long getEdgeExaminations() {
        return edgeExaminations;
    }

    private void fillFinishOrder(Graph graph, int vertex, Stack<Integer> finishOrder) {
        visited[vertex] = true;
        for (Edge edge : graph.getNeighbors(vertex)) {
            edgeExaminations++;
            if (!visited[edge.getTo()]) {
                fillFinishOrder(graph, edge.getTo(), finishOrder);
            }
        }
        finishOrder.push(vertex);
    }

    private List<List<Integer>> createReversedGraph(Graph graph) {
        List<List<Integer>> reversedGraph = new ArrayList<>();
        for (int vertex = 0; vertex < graph.getVertexCount(); vertex++) {
            reversedGraph.add(new ArrayList<>());
        }
        for (Edge edge : graph.getEdges()) {
            reversedGraph.get(edge.getTo()).add(edge.getFrom());
        }
        return reversedGraph;
    }

    private void collectComponent(List<List<Integer>> reversedGraph, int vertex, List<Integer> component) {
        visited[vertex] = true;
        component.add(vertex);
        for (int neighbor : reversedGraph.get(vertex)) {
            edgeExaminations++;
            if (!visited[neighbor]) {
                collectComponent(reversedGraph, neighbor, component);
            }
        }
    }
}