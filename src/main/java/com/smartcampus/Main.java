package com.smartcampus;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class Main {
    private final CampusGraph graph = new CampusGraph();
    private CampusPanel campusPanel;
    private JComboBox<String> sourceBox;
    private JComboBox<String> destinationBox;
    private JTextArea resultArea;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Main().createAndShowGui());
    }

    private void createAndShowGui() {
        JFrame frame = new JFrame("Smart Campus Navigation System | DAA Mini Project");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setMinimumSize(new Dimension(1000, 650));
        frame.setSize(1180, 760);
        frame.setLocationRelativeTo(null);

        JPanel root = new JPanel(new BorderLayout(12, 12));
        root.setBorder(BorderFactory.createEmptyBorder(14, 14, 14, 14));
        root.setBackground(new Color(244, 247, 251));

        JLabel title = new JLabel("SMART CAMPUS NAVIGATION SYSTEM");
        title.setFont(new Font("SansSerif", Font.BOLD, 23));
        title.setForeground(new Color(23, 54, 93));
        JLabel subtitle = new JLabel("DAA Mini Project • Graphs • Dijkstra • BFS • Prim's MST");
        subtitle.setFont(new Font("SansSerif", Font.PLAIN, 13));
        subtitle.setForeground(new Color(75, 85, 99));

        JPanel heading = new JPanel(new GridLayout(2, 1, 0, 4));
        heading.setOpaque(false);
        heading.add(title);
        heading.add(subtitle);
        root.add(heading, BorderLayout.NORTH);

        campusPanel = new CampusPanel(graph);
        JScrollPane graphScroll = new JScrollPane(campusPanel);
        graphScroll.setBorder(BorderFactory.createTitledBorder("Campus Graph"));

        JPanel controls = buildControls();
        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, graphScroll, controls);
        split.setResizeWeight(0.68);
        split.setBorder(null);
        root.add(split, BorderLayout.CENTER);

        JLabel footer = new JLabel("Note: distances are illustrative sample units, not official campus measurements.");
        footer.setForeground(new Color(90, 99, 110));
        footer.setFont(new Font("SansSerif", Font.ITALIC, 11));
        root.add(footer, BorderLayout.SOUTH);

        frame.setContentPane(root);
        frame.setVisible(true);
    }

    private JPanel buildControls() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(8, 10, 8, 4));
        panel.setBackground(Color.WHITE);
        panel.setPreferredSize(new Dimension(330, 500));

        JLabel sourceLabel = new JLabel("Source location");
        sourceLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        sourceBox = new JComboBox<>(graph.getLocations().toArray(new String[0]));
        sourceBox.setMaximumSize(new Dimension(Integer.MAX_VALUE, 34));
        sourceBox.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel destinationLabel = new JLabel("Destination location");
        destinationLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        destinationBox = new JComboBox<>(graph.getLocations().toArray(new String[0]));
        destinationBox.setSelectedItem("Library");
        destinationBox.setMaximumSize(new Dimension(Integer.MAX_VALUE, 34));
        destinationBox.setAlignmentX(Component.LEFT_ALIGNMENT);

        panel.add(sourceLabel);
        panel.add(Box.createVerticalStrut(5));
        panel.add(sourceBox);
        panel.add(Box.createVerticalStrut(12));
        panel.add(destinationLabel);
        panel.add(Box.createVerticalStrut(5));
        panel.add(destinationBox);
        panel.add(Box.createVerticalStrut(16));

        JButton dijkstraButton = button("Find Shortest Route (Dijkstra)");
        JButton bfsButton = button("Find Fewest-Edge Route (BFS)");
        JButton primButton = button("Show Minimum Spanning Tree (Prim)");
        JButton resetButton = button("Show Full Campus Graph");

        dijkstraButton.addActionListener(e -> runDijkstra());
        bfsButton.addActionListener(e -> runBfs());
        primButton.addActionListener(e -> runPrim());
        resetButton.addActionListener(e -> {
            campusPanel.showAll();
            resultArea.setText("Full campus graph shown.\\nChoose an algorithm to explore its result.");
        });

        panel.add(dijkstraButton);
        panel.add(Box.createVerticalStrut(8));
        panel.add(bfsButton);
        panel.add(Box.createVerticalStrut(8));
        panel.add(primButton);
        panel.add(Box.createVerticalStrut(8));
        panel.add(resetButton);
        panel.add(Box.createVerticalStrut(14));

        JLabel resultLabel = new JLabel("Result");
        resultLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(resultLabel);
        panel.add(Box.createVerticalStrut(5));

        resultArea = new JTextArea(12, 25);
        resultArea.setEditable(false);
        resultArea.setLineWrap(true);
        resultArea.setWrapStyleWord(true);
        resultArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        resultArea.setText("Choose source and destination, then run an algorithm.");
        JScrollPane resultScroll = new JScrollPane(resultArea);
        resultScroll.setAlignmentX(Component.LEFT_ALIGNMENT);
        resultScroll.setPreferredSize(new Dimension(310, 240));
        resultScroll.setMaximumSize(new Dimension(Integer.MAX_VALUE, 300));
        panel.add(resultScroll);
        panel.add(Box.createVerticalStrut(12));

        JTextArea note = new JTextArea(
            "Dijkstra: minimum total distance\\n" +
            "BFS: fewest number of edges\\n" +
            "Prim: minimum-cost network connecting all locations"
        );
        note.setEditable(false);
        note.setOpaque(false);
        note.setFont(new Font("SansSerif", Font.PLAIN, 11));
        note.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(note);
        return panel;
    }

    private JButton button(String text) {
        JButton button = new JButton(text);
        button.setAlignmentX(Component.LEFT_ALIGNMENT);
        button.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        button.setFocusPainted(false);
        return button;
    }

    private String selectedSource() {
        return (String) sourceBox.getSelectedItem();
    }

    private String selectedDestination() {
        return (String) destinationBox.getSelectedItem();
    }

    private void runDijkstra() {
        String source = selectedSource();
        String destination = selectedDestination();
        Algorithms.RouteResult result = Algorithms.dijkstra(graph, source, destination);
        if (!result.found) {
            resultArea.setText("No route found between the selected locations.");
            campusPanel.showAll();
            return;
        }
        campusPanel.showRoute(result.path);
        resultArea.setText(
            "ALGORITHM: Dijkstra\\n\\n" +
            "Shortest route:\\n" + String.join(" -> ", result.path) + "\\n\\n" +
            "Total distance: " + result.totalWeight + " units\\n" +
            "Locations visited in route: " + result.path.size() + "\\n\\n" +
            "Typical complexity with a priority queue:\\nO((V + E) log V)\\n\\n" +
            "Purpose: minimum total weight for non-negative edges."
        );
    }

    private void runBfs() {
        String source = selectedSource();
        String destination = selectedDestination();
        Algorithms.RouteResult result = Algorithms.bfs(graph, source, destination);
        if (!result.found) {
            resultArea.setText("No route found between the selected locations.");
            campusPanel.showAll();
            return;
        }
        campusPanel.showRoute(result.path);
        resultArea.setText(
            "ALGORITHM: BFS\\n\\n" +
            "Fewest-edge route:\\n" + String.join(" -> ", result.path) + "\\n\\n" +
            "Number of edges (hops): " + Math.max(0, result.path.size() - 1) + "\\n" +
            "Distance of this selected route: " + result.totalWeight + " units\\n\\n" +
            "Time complexity with adjacency lists: O(V + E)\\n\\n" +
            "Important: BFS minimises hops, not total weighted distance."
        );
    }

    private void runPrim() {
        String source = selectedSource();
        Algorithms.MstResult result = Algorithms.prim(graph, source);
        campusPanel.showMst(result.edges);
        StringBuilder text = new StringBuilder("ALGORITHM: Prim's Minimum Spanning Tree\\n\\n");
        for (CampusGraph.Edge edge : result.edges) {
            text.append(edge.from).append(" -- ").append(edge.to)
                .append(" (weight ").append(edge.weight).append(")\\n");
        }
        text.append("\\nTotal MST weight: ").append(result.totalWeight).append(" units\\n");
        text.append("MST edges: ").append(result.edges.size()).append("\\n\\n");
        text.append("Typical complexity with a priority queue: O(E log V)\\n\\n");
        if (result.connected) {
            text.append("All campus locations are connected by this MST.");
        } else {
            text.append("Warning: graph is disconnected; this is only the component reachable from the selected start.");
        }
        text.append("\\n\\nPrim connects the whole network; it does not find a shortest route between two locations.");
        resultArea.setText(text.toString());
    }
}
