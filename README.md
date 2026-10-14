# Smart Campus Navigation System (Java)

A beginner-friendly DAA mini project built with **Java Swing** and weighted graph algorithms. It runs locally and uses sample campus data; it does not require an internet connection, a database, GPS, or third-party libraries.

## Features

- Select a source and destination campus location.
- **Dijkstra's Algorithm**: finds the minimum-distance route for non-negative edge weights.
- **Breadth-First Search (BFS)**: finds a route with the fewest edges (hops), not necessarily the minimum distance.
- **Prim's Algorithm**: displays a Minimum Spanning Tree (MST) and its total weight.
- Visual campus graph with weighted edges and highlighted route/MST.
- Shows route result, distance/hop count, and basic complexity notes.
- Handles invalid selections and disconnected routes.

## Requirements

- JDK 17 or newer recommended (Java 11 may also work with minor differences depending on environment).
- Any Java IDE (IntelliJ IDEA, Eclipse, NetBeans) or terminal.
- No external libraries are required; the UI uses Swing.

## Run from terminal

Open a terminal in this project folder.

### Windows

```bat
javac -d out src\main\java\com\smartcampus\*.java
java -cp out com.smartcampus.Main
```

### macOS / Linux

```bash
mkdir -p out
javac -d out src/main/java/com/smartcampus/*.java
java -cp out com.smartcampus.Main
```

If `javac` or `java` is not recognised, install a JDK and configure PATH. Confirm with:

```bash
java -version
javac -version
```

## Run in an IDE

1. Extract the ZIP.
2. Open the extracted folder as a Java project, or create a Java project and add all `.java` files under `src/main/java/com/smartcampus`.
3. Run `Main.java`.
4. If the IDE asks for a JDK, select JDK 17 or newer.

## Project structure

```text
Smart_Campus_Navigation_Java/
├── README.md
├── docs/
│   └── PROJECT_REPORT_OUTLINE.md
└── src/
    └── main/
        └── java/
            └── com/
                └── smartcampus/
                    ├── Main.java
                    ├── CampusGraph.java
                    ├── Algorithms.java
                    └── CampusPanel.java
```

## Sample campus data

Distances are illustrative distance units, not real measurements.

- Main Gate — Admin Block: 4
- Main Gate — Canteen: 6
- Admin Block — Library: 3
- Admin Block — Computer Lab: 5
- Canteen — Library: 2
- Canteen — Hostel: 7
- Library — Computer Lab: 2
- Library — Auditorium: 6
- Computer Lab — Hostel: 4
- Hostel — Auditorium: 3

To edit campus data, update the edge list in `CampusGraph.java`.

## DAA concepts and complexity

| Algorithm | Purpose | Typical time complexity |
|---|---|---|
| BFS using adjacency lists | Fewest edges in an unweighted interpretation | `O(V + E)` |
| Dijkstra using a priority queue | Minimum total weight for non-negative weights | `O((V + E) log V)` |
| Prim using a priority queue | Minimum spanning tree | `O(E log V)` |

`V` = vertices/locations; `E` = edges/paths. Complexity depends on the data structure and implementation.

## Important distinctions

- Dijkstra minimises total edge weight from one source to a destination.
- BFS minimises the number of edges, not the total distance when edge weights differ.
- Prim creates a minimum-cost network connecting all locations; it is not a shortest-route algorithm.
- If the graph is disconnected, a single spanning tree for all locations does not exist.

## Troubleshooting

- **`javac` not found**: install a JDK and configure PATH.
- **Class not found**: compile using the commands above from the project root.
- **Window does not open**: ensure Java is running in a desktop environment with Swing support.
- **Route differs from expectation**: check the weights in `CampusGraph.java`; the displayed total is the sum of the route edges.
