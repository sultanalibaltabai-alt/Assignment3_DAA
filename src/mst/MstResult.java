package mst;

import graph.Edge;

import java.util.List;

public class MstResult {
    private final List<Edge> edges;
    private final int totalWeight;
    private final boolean connected;
    private final long operations;

    public MstResult(List<Edge> edges, int totalWeight, boolean connected, long operations) {
        this.edges = edges;
        this.totalWeight = totalWeight;
        this.connected = connected;
        this.operations = operations;
    }

    public List<Edge> getEdges() {
        return edges;
    }

    public int getTotalWeight() {
        return totalWeight;
    }

    public boolean isConnected() {
        return connected;
    }

    public long getOperations() {
        return operations;
    }
}