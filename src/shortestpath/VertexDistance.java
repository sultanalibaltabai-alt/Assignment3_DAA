package shortestpath;

public class VertexDistance implements Comparable<VertexDistance> {
    private final int vertex;
    private final int distance;

    public VertexDistance(int vertex, int distance) {
        this.vertex = vertex;
        this.distance = distance;
    }

    public int getVertex() {
        return vertex;
    }

    public int getDistance() {
        return distance;
    }

    @Override
    public int compareTo(VertexDistance other) {
        return Integer.compare(distance, other.distance);
    }
}