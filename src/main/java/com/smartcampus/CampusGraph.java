package com.smartcampus;

import java.util.*;


 * Stores the undirected, weighted campus graph.
 * Distances are illustrative units, not real campus measurements.
public class CampusGraph {
    public static final class Edge {
        public final String from;
        public final String to;
        public final int weight;

        public Edge(String from, String to, int weight) {
            this.from = from;
            this.to = to;
            this.weight = weight;
        }
    }

    private final Map<String, List<Edge>> adjacency = new LinkedHashMap<>();
    private final List<Edge> edges = new ArrayList<>();

    public CampusGraph() {
        String[] locations = {
            "Main Gate", "Admin Block", "Canteen", "Library",
            "Computer Lab", "Hostel", "Auditorium"
        };
        for (String location : locations) {
            adjacency.put(location, new ArrayList<>());
        }

        addEdge("Main Gate", "Admin Block", 4);
        addEdge("Main Gate", "Canteen", 6);
        addEdge("Admin Block", "Library", 3);
        addEdge("Admin Block", "Computer Lab", 5);
        addEdge("Canteen", "Library", 2);
        addEdge("Canteen", "Hostel", 7);
        addEdge("Library", "Computer Lab", 2);
        addEdge("Library", "Auditorium", 6);
        addEdge("Computer Lab", "Hostel", 4);
        addEdge("Hostel", "Auditorium", 3);
    }

    private void addEdge(String from, String to, int weight) {
        Edge edge = new Edge(from, to, weight);
        edges.add(edge);
        adjacency.get(from).add(edge);
        adjacency.get(to).add(edge);
    }

    public Set<String> getLocations() {
        return Collections.unmodifiableSet(adjacency.keySet());
    }

    public List<Edge> getEdges() {
        return Collections.unmodifiableList(edges);
    }

    public List<Edge> getAdjacentEdges(String location) {
        return Collections.unmodifiableList(adjacency.getOrDefault(location, Collections.emptyList()));
    }

    public String otherEnd(Edge edge, String location) {
        return edge.from.equals(location) ? edge.to : edge.from;
    }
}
