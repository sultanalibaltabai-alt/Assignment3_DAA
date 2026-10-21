package dsu;

public class DisjointSet {
    private final int[] parent;
    private final int[] rank;
    private long findCount;
    private long unionCount;

    public DisjointSet(int size) {
        parent = new int[size];
        rank = new int[size];
        for (int element = 0; element < size; element++) {
            parent[element] = element;
        }
    }

    public int find(int element) {
        findCount++;
        if (parent[element] != element) {
            parent[element] = find(parent[element]);
        }
        return parent[element];
    }

    public boolean union(int first, int second) {
        unionCount++;
        int firstRoot = find(first);
        int secondRoot = find(second);

        if (firstRoot == secondRoot) {
            return false;
        }

        if (rank[firstRoot] < rank[secondRoot]) {
            parent[firstRoot] = secondRoot;
        } else if (rank[firstRoot] > rank[secondRoot]) {
            parent[secondRoot] = firstRoot;
        } else {
            parent[secondRoot] = firstRoot;
            rank[firstRoot]++;
        }
        return true;
    }

    public long getFindCount() {
        return findCount;
    }

    public long getUnionCount() {
        return unionCount;
    }
}