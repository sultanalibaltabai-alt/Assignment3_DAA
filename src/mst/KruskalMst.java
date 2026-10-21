package mst;

import dsu.DisjointSet;
import graph.Edge;
import graph.Graph;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class KruskalMst {
    public MstResult findMst(Graph graph) {
        List<Edge> sortedEdges = new ArrayList<>(graph.getEdges());
        Collections.sort(sortedEdges);

        DisjointSet disjointSet = new DisjointSet(graph.getVertexCount());
        List<Edge> mstEdges = new ArrayList<>();
        int totalWeight = 0;
        long edgeExaminations = 0;

        for (Edge edge : sortedEdges) {
            edgeExaminations++;
            if (disjointSet.union(edge.getFrom(), edge.getTo())) {
                mstEdges.add(edge);
                totalWeight += edge.getWeight();
            }
        }

        boolean connected = mstEdges.size() == graph.getVertexCount() - 1;
        long operations = edgeExaminations + disjointSet.getFindCount() + disjointSet.getUnionCount();
        return new MstResult(mstEdges, totalWeight, connected, operations);
    }
}