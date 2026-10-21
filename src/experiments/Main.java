package experiments;

import connectivity.KosarajuScc;
import connectivity.TopologicalSort;
import graph.Graph;
import graph.GraphGenerator;
import mst.KruskalMst;
import mst.MstResult;
import mst.PrimMst;
import shortestpath.Dijkstra;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Main {
    private static final long SEED = 42;
    private static final int REPEATS = 5;
    private static final int SOURCE = 0;
    private static final int[][] SIZES = {{10, 30}, {100, 500}, {1000, 5000}};

    private static final List<String> rows = new ArrayList<>();

    public static void main(String[] args) throws IOException {
        rows.add("algorithm,vertices,edges,avgTimeMs,operations");

        for (int[] size : SIZES) {
            int vertexCount = size[0];
            int edgeCount = size[1];

            Graph undirected = new GraphGenerator(SEED).createConnectedGraph(vertexCount, edgeCount);
            Graph directed = new GraphGenerator(SEED).createDirectedGraph(vertexCount, edgeCount);
            Graph dag = new GraphGenerator(SEED).createDag(vertexCount, edgeCount);

            System.out.println("=== V=" + vertexCount + ", E=" + edgeCount + " ===");
            reportResults(undirected, directed, dag);

            runKruskal(undirected);
            runPrim(undirected);
            runTopologicalSort("TopologicalSort-DAG", dag);
            runTopologicalSort("TopologicalSort-Cyclic", directed);
            runScc(directed);
            runDijkstra(directed);
        }

        Files.createDirectories(Path.of("results/tables"));
        Files.write(Path.of("results/tables/results.csv"), rows);
        System.out.println("Results saved to results/tables/results.csv");
    }

    private static void reportResults(Graph undirected, Graph directed, Graph dag) {
        MstResult kruskal = new KruskalMst().findMst(undirected);
        MstResult prim = new PrimMst().findMst(undirected);
        System.out.println("Kruskal weight: " + kruskal.getTotalWeight());
        System.out.println("Prim weight: " + prim.getTotalWeight());
        System.out.println("Graph connected: " + kruskal.isConnected());

        TopologicalSort dagSort = new TopologicalSort();
        List<Integer> order = dagSort.sort(dag);
        System.out.println("DAG has cycle: " + dagSort.hasCycle());
        if (dag.getVertexCount() <= 10) {
            System.out.println("Topological order: " + order);
        }

        TopologicalSort directedSort = new TopologicalSort();
        directedSort.sort(directed);
        System.out.println("Directed graph has cycle: " + directedSort.hasCycle());

        List<List<Integer>> components = new KosarajuScc().findComponents(directed);
        System.out.println("SCC count: " + components.size());
        if (directed.getVertexCount() <= 10) {
            System.out.println("SCC list: " + components);
            printDijkstra(directed);
        }
        System.out.println();
    }

    private static void printDijkstra(Graph graph) {
        Dijkstra dijkstra = new Dijkstra(graph, SOURCE);
        dijkstra.run();
        System.out.println("Dijkstra from vertex " + SOURCE + ":");
        for (int vertex = 0; vertex < graph.getVertexCount(); vertex++) {
            if (dijkstra.isReachable(vertex)) {
                System.out.println("  to " + vertex + ": distance " + dijkstra.getDistance(vertex)
                        + ", path " + dijkstra.getPath(vertex));
            } else {
                System.out.println("  to " + vertex + ": unreachable");
            }
        }
    }

    private static void runKruskal(Graph graph) {
        long totalTime = 0;
        long operations = 0;
        for (int repeat = 0; repeat < REPEATS; repeat++) {
            long start = System.nanoTime();
            MstResult result = new KruskalMst().findMst(graph);
            totalTime += System.nanoTime() - start;
            operations = result.getOperations();
        }
        addRow("Kruskal", graph, totalTime, operations);
    }

    private static void runPrim(Graph graph) {
        long totalTime = 0;
        long operations = 0;
        for (int repeat = 0; repeat < REPEATS; repeat++) {
            long start = System.nanoTime();
            MstResult result = new PrimMst().findMst(graph);
            totalTime += System.nanoTime() - start;
            operations = result.getOperations();
        }
        addRow("Prim", graph, totalTime, operations);
    }

    private static void runTopologicalSort(String name, Graph graph) {
        long totalTime = 0;
        long operations = 0;
        for (int repeat = 0; repeat < REPEATS; repeat++) {
            TopologicalSort topologicalSort = new TopologicalSort();
            long start = System.nanoTime();
            topologicalSort.sort(graph);
            totalTime += System.nanoTime() - start;
            operations = topologicalSort.getEdgeExaminations();
        }
        addRow(name, graph, totalTime, operations);
    }

    private static void runScc(Graph graph) {
        long totalTime = 0;
        long operations = 0;
        for (int repeat = 0; repeat < REPEATS; repeat++) {
            KosarajuScc scc = new KosarajuScc();
            long start = System.nanoTime();
            scc.findComponents(graph);
            totalTime += System.nanoTime() - start;
            operations = scc.getEdgeExaminations();
        }
        addRow("SCC", graph, totalTime, operations);
    }

    private static void runDijkstra(Graph graph) {
        long totalTime = 0;
        long operations = 0;
        for (int repeat = 0; repeat < REPEATS; repeat++) {
            Dijkstra dijkstra = new Dijkstra(graph, SOURCE);
            long start = System.nanoTime();
            dijkstra.run();
            totalTime += System.nanoTime() - start;
            operations = dijkstra.getOperations();
        }
        addRow("Dijkstra", graph, totalTime, operations);
    }

    private static void addRow(String name, Graph graph, long totalTime, long operations) {
        double averageMs = totalTime / (double) REPEATS / 1_000_000.0;
        rows.add(String.format(Locale.US, "%s,%d,%d,%.4f,%d",
                name, graph.getVertexCount(), graph.getEdgeCount(), averageMs, operations));
    }
}