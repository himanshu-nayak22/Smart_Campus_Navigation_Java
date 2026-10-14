package com.smartcampus;

import java.util.*;

/** Graph algorithms used by the project. */
public final class Algorithms {
    private Algorithms() {}

    public static final class RouteResult {
        public final List<String> path;
        public final int totalWeight;
        public final boolean found;

        public RouteResult(List<String> path, int totalWeight, boolean found) {
            this.path = path;
            this.totalWeight = totalWeight;
            this.found = found;
        }
    }

    public static final class MstResult {
        public final List<CampusGraph.Edge> edges;
        public final int totalWeight;
        public final boolean connected;

        public MstResult(List<CampusGraph.Edge> edges, int totalWeight, boolean connected) {
            this.edges = edges;
            this.totalWeight = totalWeight;
            this.connected = connected;
        }
    }

    private static final class NodeDistance {
        final String node;
        final int distance;

        NodeDistance(String node, int distance) {
            this.node = node;
            this.distance = distance;
        }
    }

    /** Dijkstra: minimum total weight with non-negative edge weights. */
    public static RouteResult dijkstra(CampusGraph graph, String source, String destination) {
        if (source == null || destination == null ||
            !graph.getLocations().contains(source) || !graph.getLocations().contains(destination)) {
            return new RouteResult(Collections.emptyList(), 0, false);
        }
        Map<String, Integer> dist = new HashMap<>();
        Map<String, String> previous = new HashMap<>();
        for (String location : graph.getLocations()) dist.put(location, Integer.MAX_VALUE);
        dist.put(source, 0);

        PriorityQueue<NodeDistance> pq =
            new PriorityQueue<>(Comparator.comparingInt(nd -> nd.distance));
        pq.add(new NodeDistance(source, 0));

        while (!pq.isEmpty()) {
            NodeDistance current = pq.poll();
            if (current.distance != dist.get(current.node)) continue;
            if (current.node.equals(destination)) break;

            for (CampusGraph.Edge edge : graph.getAdjacentEdges(current.node)) {
                String neighbour = graph.otherEnd(edge, current.node);
                if (current.distance > Integer.MAX_VALUE - edge.weight) continue;
                int candidate = current.distance + edge.weight;
                if (candidate < dist.get(neighbour)) {
                    dist.put(neighbour, candidate);
                    previous.put(neighbour, current.node);
                    pq.add(new NodeDistance(neighbour, candidate));
                }
            }
        }

        if (dist.get(destination) == Integer.MAX_VALUE) {
            return new RouteResult(Collections.emptyList(), 0, false);
        }

        LinkedList<String> path = new LinkedList<>();
        String cursor = destination;
        while (cursor != null) {
            path.addFirst(cursor);
            if (cursor.equals(source)) break;
            cursor = previous.get(cursor);
        }
        if (path.isEmpty() || !path.getFirst().equals(source)) {
            return new RouteResult(Collections.emptyList(), 0, false);
        }
        return new RouteResult(path, dist.get(destination), true);
    }

    /** BFS: finds a path with the fewest edges, not the lowest weighted distance. */
    public static RouteResult bfs(CampusGraph graph, String source, String destination) {
        if (source == null || destination == null ||
            !graph.getLocations().contains(source) || !graph.getLocations().contains(destination)) {
            return new RouteResult(Collections.emptyList(), 0, false);
        }

        Queue<String> queue = new ArrayDeque<>();
        Set<String> visited = new HashSet<>();
        Map<String, String> previous = new HashMap<>();
        queue.add(source);
        visited.add(source);

        while (!queue.isEmpty()) {
            String current = queue.remove();
            if (current.equals(destination)) break;
            for (CampusGraph.Edge edge : graph.getAdjacentEdges(current)) {
                String neighbour = graph.otherEnd(edge, current);
                if (visited.add(neighbour)) {
                    previous.put(neighbour, current);
                    queue.add(neighbour);
                }
            }
        }

        if (!visited.contains(destination)) {
            return new RouteResult(Collections.emptyList(), 0, false);
        }

        LinkedList<String> path = new LinkedList<>();
        String cursor = destination;
        while (cursor != null) {
            path.addFirst(cursor);
            if (cursor.equals(source)) break;
            cursor = previous.get(cursor);
        }
        if (path.isEmpty() || !path.getFirst().equals(source)) {
            return new RouteResult(Collections.emptyList(), 0, false);
        }

        int totalWeight = 0;
        for (int i = 0; i < path.size() - 1; i++) {
            totalWeight += findWeight(graph, path.get(i), path.get(i + 1));
        }
        return new RouteResult(path, totalWeight, true);
    }

    private static int findWeight(CampusGraph graph, String a, String b) {
        for (CampusGraph.Edge edge : graph.getAdjacentEdges(a)) {
            if (graph.otherEnd(edge, a).equals(b)) return edge.weight;
        }
        return 0;
    }

    /** Prim: creates an MST for the component containing the chosen start node. */
    public static MstResult prim(CampusGraph graph, String start) {
        List<CampusGraph.Edge> selected = new ArrayList<>();
        if (start == null || !graph.getLocations().contains(start)) {
            return new MstResult(selected, 0, false);
        }

        Set<String> visited = new HashSet<>();
        PriorityQueue<CampusGraph.Edge> pq =
            new PriorityQueue<>(Comparator.comparingInt(e -> e.weight));
        visited.add(start);
        pq.addAll(graph.getAdjacentEdges(start));
        int total = 0;

        while (!pq.isEmpty() && visited.size() < graph.getLocations().size()) {
            CampusGraph.Edge edge = pq.poll();
            boolean fromVisited = visited.contains(edge.from);
            boolean toVisited = visited.contains(edge.to);
            if (fromVisited == toVisited) continue;

            selected.add(edge);
            total += edge.weight;
            String newNode = fromVisited ? edge.to : edge.from;
            visited.add(newNode);

            for (CampusGraph.Edge next : graph.getAdjacentEdges(newNode)) {
                String other = graph.otherEnd(next, newNode);
                if (!visited.contains(other)) pq.add(next);
            }
        }

        boolean connected = visited.size() == graph.getLocations().size();
        return new MstResult(selected, total, connected);
    }
}
