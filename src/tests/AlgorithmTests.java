package tests;

import connectivity.KosarajuScc;
import connectivity.TopologicalSort;
import dsu.DisjointSet;
import graph.Edge;
import graph.Graph;
import graph.GraphGenerator;
import mst.KruskalMst;
import mst.MstResult;
import mst.PrimMst;
import shortestpath.Dijkstra;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AlgorithmTests {
    private static int passed = 0;
    private static int failed = 0;

    public static void main(String[] args) {
        testDisjointSet();
        testSmallGraphMst();
        testSingleVertexMst();
        testDisconnectedMst();
        testDuplicateWeightsMst();
        testRandomGraphsMst();
        testTopologicalSortOnDag();
        testTopologicalSortWithCycle();
        testTopologicalSortSingleVertex();
        testScc();
        testSccSingleVertex();
        testDijkstra();
        testDijkstraSingleVertex();
        testDijkstraNegativeWeight();

        System.out.println("Passed: " + passed + ", Failed: " + failed);
    }

    private static void check(String name, boolean condition) {
        if (condition) {
            passed++;
            System.out.println("PASS: " + name);
        } else {
            failed++;
            System.out.println("FAIL: " + name);
        }
    }

    private static void testDisjointSet() {
        DisjointSet disjointSet = new DisjointSet(5);
        disjointSet.union(0, 1);
        disjointSet.union(1, 2);

        check("DSU joins connected elements", disjointSet.find(0) == disjointSet.find(2));
        check("DSU keeps other elements apart", disjointSet.find(0) != disjointSet.find(3));
        check("DSU union of same set returns false", !disjointSet.union(0, 2));
    }

    private static void testSmallGraphMst() {
        Graph graph = new Graph(4, false);
        graph.addEdge(0, 1, 1);
        graph.addEdge(1, 2, 2);
        graph.addEdge(2, 3, 3);
        graph.addEdge(0, 3, 10);

        check("Small graph Kruskal weight is 6", new KruskalMst().findMst(graph).getTotalWeight() == 6);
        check("Small graph Prim weight is 6", new PrimMst().findMst(graph).getTotalWeight() == 6);
    }

    private static void testSingleVertexMst() {
        Graph graph = new Graph(1, false);
        MstResult kruskal = new KruskalMst().findMst(graph);
        MstResult prim = new PrimMst().findMst(graph);

        check("Single vertex Kruskal weight is 0", kruskal.getTotalWeight() == 0);
        check("Single vertex Prim weight is 0", prim.getTotalWeight() == 0);
        check("Single vertex is connected", kruskal.isConnected() && prim.isConnected());
    }

    private static void testDisconnectedMst() {
        Graph graph = new Graph(4, false);
        graph.addEdge(0, 1, 1);
        graph.addEdge(2, 3, 2);

        check("Kruskal detects disconnected graph", !new KruskalMst().findMst(graph).isConnected());
        check("Prim detects disconnected graph", !new PrimMst().findMst(graph).isConnected());
    }

    private static void testDuplicateWeightsMst() {
        Graph graph = new Graph(4, false);
        graph.addEdge(0, 1, 1);
        graph.addEdge(1, 2, 1);
        graph.addEdge(2, 3, 1);
        graph.addEdge(3, 0, 1);
        graph.addEdge(0, 2, 1);

        check("Duplicate weights Kruskal weight is 3", new KruskalMst().findMst(graph).getTotalWeight() == 3);
        check("Duplicate weights Prim weight is 3", new PrimMst().findMst(graph).getTotalWeight() == 3);
    }

    private static void testRandomGraphsMst() {
        int[][] sizes = {{10, 30}, {100, 500}, {1000, 5000}};
        boolean sameWeight = true;
        boolean connected = true;

        for (int[] size : sizes) {
            Graph graph = new GraphGenerator(42).createConnectedGraph(size[0], size[1]);
            MstResult kruskal = new KruskalMst().findMst(graph);
            MstResult prim = new PrimMst().findMst(graph);
            if (kruskal.getTotalWeight() != prim.getTotalWeight()) {
                sameWeight = false;
            }
            if (!kruskal.isConnected() || !prim.isConnected()) {
                connected = false;
            }
        }
        check("Kruskal and Prim give the same weight on random graphs", sameWeight);
        check("Generated undirected graphs are connected", connected);
    }

    private static void testTopologicalSortOnDag() {
        Graph dag = new GraphGenerator(42).createDag(100, 500);
        TopologicalSort topologicalSort = new TopologicalSort();
        List<Integer> order = topologicalSort.sort(dag);

        check("DAG has no cycle", !topologicalSort.hasCycle());
        check("DAG topological order is valid", isValidOrder(dag, order));
    }

    private static boolean isValidOrder(Graph graph, List<Integer> order) {
        if (order.size() != graph.getVertexCount()) {
            return false;
        }
        int[] position = new int[graph.getVertexCount()];
        for (int index = 0; index < order.size(); index++) {
            position[order.get(index)] = index;
        }
        for (Edge edge : graph.getEdges()) {
            if (position[edge.getFrom()] >= position[edge.getTo()]) {
                return false;
            }
        }
        return true;
    }

    private static void testTopologicalSortWithCycle() {
        Graph graph = new Graph(3, true);
        graph.addEdge(0, 1, 1);
        graph.addEdge(1, 2, 1);
        graph.addEdge(2, 0, 1);

        TopologicalSort topologicalSort = new TopologicalSort();
        topologicalSort.sort(graph);

        check("Cycle is detected by topological sort", topologicalSort.hasCycle());
    }

    private static void testTopologicalSortSingleVertex() {
        Graph graph = new Graph(1, true);
        TopologicalSort topologicalSort = new TopologicalSort();
        List<Integer> order = topologicalSort.sort(graph);

        check("Single vertex has no cycle", !topologicalSort.hasCycle());
        check("Single vertex order is [0]", order.equals(List.of(0)));
    }

    private static void testScc() {
        Graph graph = new Graph(6, true);
        graph.addEdge(0, 1, 1);
        graph.addEdge(1, 2, 1);
        graph.addEdge(2, 0, 1);
        graph.addEdge(2, 3, 1);
        graph.addEdge(3, 4, 1);
        graph.addEdge(4, 5, 1);
        graph.addEdge(5, 3, 1);

        List<List<Integer>> components = new KosarajuScc().findComponents(graph);

        check("SCC finds 2 components", components.size() == 2);
        check("Every vertex is in exactly one SCC", countVertices(components) == 6
                && countUniqueVertices(components) == 6);
    }

    private static void testSccSingleVertex() {
        Graph graph = new Graph(1, true);
        List<List<Integer>> components = new KosarajuScc().findComponents(graph);

        check("Single vertex gives one SCC", components.size() == 1);
    }

    private static int countVertices(List<List<Integer>> components) {
        int count = 0;
        for (List<Integer> component : components) {
            count += component.size();
        }
        return count;
    }

    private static int countUniqueVertices(List<List<Integer>> components) {
        Set<Integer> vertices = new HashSet<>();
        for (List<Integer> component : components) {
            vertices.addAll(component);
        }
        return vertices.size();
    }

    private static void testDijkstra() {
        Graph graph = new Graph(5, true);
        graph.addEdge(0, 1, 4);
        graph.addEdge(0, 2, 1);
        graph.addEdge(2, 1, 2);
        graph.addEdge(1, 3, 1);

        Dijkstra dijkstra = new Dijkstra(graph, 0);
        dijkstra.run();

        check("Dijkstra distance to 1 is 3", dijkstra.getDistance(1) == 3);
        check("Dijkstra distance to 3 is 4", dijkstra.getDistance(3) == 4);
        check("Dijkstra path to 3 is correct", dijkstra.getPath(3).equals(List.of(0, 2, 1, 3)));
        check("Dijkstra marks vertex 4 as unreachable", !dijkstra.isReachable(4));
        check("Dijkstra returns empty path for unreachable vertex", dijkstra.getPath(4).isEmpty());
    }

    private static void testDijkstraSingleVertex() {
        Graph graph = new Graph(1, true);
        Dijkstra dijkstra = new Dijkstra(graph, 0);
        dijkstra.run();

        check("Dijkstra single vertex distance is 0", dijkstra.getDistance(0) == 0);
    }

    private static void testDijkstraNegativeWeight() {
        Graph graph = new Graph(2, true);
        graph.addEdge(0, 1, -5);

        boolean exceptionThrown = false;
        try {
            new Dijkstra(graph, 0);
        } catch (IllegalArgumentException exception) {
            exceptionThrown = true;
        }
        check("Dijkstra rejects negative weights", exceptionThrown);
    }
}