package com.smartcampus;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;


public class CampusPanel extends JPanel {
    private final CampusGraph graph;
    private Set<String> highlightedNodes = new HashSet<>();
    private Set<String> highlightedEdges = new HashSet<>();
    private String mode = "graph";

    private final Map<String, Point> positions = new LinkedHashMap<>();

    public CampusPanel(CampusGraph graph) {
        this.graph = graph;
        setBackground(Color.WHITE);
        setPreferredSize(new Dimension(680, 480));
        positions.put("Main Gate", new Point(90, 230));
        positions.put("Admin Block", new Point(230, 100));
        positions.put("Canteen", new Point(230, 360));
        positions.put("Library", new Point(380, 210));
        positions.put("Computer Lab", new Point(510, 95));
        positions.put("Hostel", new Point(530, 355));
        positions.put("Auditorium", new Point(630, 220));
    }

    public void showRoute(List<String> path) {
        mode = "route";
        highlightedNodes = new HashSet<>(path);
        highlightedEdges = new HashSet<>();
        for (int i = 0; i < path.size() - 1; i++) {
            highlightedEdges.add(edgeKey(path.get(i), path.get(i + 1)));
        }
        repaint();
    }

    public void showMst(List<CampusGraph.Edge> edges) {
        mode = "mst";
        highlightedNodes = new HashSet<>(graph.getLocations());
        highlightedEdges = new HashSet<>();
        for (CampusGraph.Edge edge : edges) {
            highlightedEdges.add(edgeKey(edge.from, edge.to));
        }
        repaint();
    }

    public void showAll() {
        mode = "graph";
        highlightedNodes.clear();
        highlightedEdges.clear();
        repaint();
    }

    private String edgeKey(String a, String b) {
        return a.compareTo(b) < 0 ? a + "|" + b : b + "|" + a;
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        Graphics2D g = (Graphics2D) graphics.create();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int panelW = getWidth();
        int panelH = getHeight();
        double sx = Math.max(0.65, (panelW - 70.0) / 680.0);
        double sy = Math.max(0.65, (panelH - 70.0) / 480.0);
        int offsetX = 25;
        int offsetY = 25;

    
        for (CampusGraph.Edge edge : graph.getEdges()) {
            Point a = scaled(edge.from, sx, sy, offsetX, offsetY);
            Point b = scaled(edge.to, sx, sy, offsetX, offsetY);
            boolean active = highlightedEdges.contains(edgeKey(edge.from, edge.to));
            g.setStroke(new BasicStroke(active ? 4f : 1.5f));
            g.setColor(active ? new Color(35, 135, 90) : new Color(175, 184, 195));
            g.drawLine(a.x, a.y, b.x, b.y);
            int mx = (a.x + b.x) / 2;
            int my = (a.y + b.y) / 2;
            g.setColor(new Color(50, 55, 65));
            g.setFont(new Font("SansSerif", Font.BOLD, 12));
            g.drawString(String.valueOf(edge.weight), mx + 3, my - 3);
        }

    
        for (String location : graph.getLocations()) {
            Point p = scaled(location, sx, sy, offsetX, offsetY);
            boolean active = highlightedNodes.contains(location);
            int r = 18;
            g.setColor(active ? new Color(35, 135, 90) : new Color(40, 92, 150));
            g.fillOval(p.x - r, p.y - r, 2 * r, 2 * r);
            g.setColor(Color.WHITE);
            g.setFont(new Font("SansSerif", Font.BOLD, 10));
            String label = shortLabel(location);
            FontMetrics fm = g.getFontMetrics();
            g.drawString(label, p.x - fm.stringWidth(label) / 2, p.y + 4);
            g.setColor(new Color(30, 38, 48));
            g.setFont(new Font("SansSerif", Font.PLAIN, 12));
            String full = location;
            int labelX = p.x - g.getFontMetrics().stringWidth(full) / 2;
            g.drawString(full, labelX, p.y + 33);
        }

        g.setColor(new Color(75, 85, 99));
        g.setFont(new Font("SansSerif", Font.ITALIC, 12));
        String caption = mode.equals("route") ? "Highlighted route" :
                         mode.equals("mst") ? "Highlighted Minimum Spanning Tree" : "Campus network (sample distances)";
        g.drawString(caption, 12, 18);
        g.dispose();
    }

    private Point scaled(String location, double sx, double sy, int ox, int oy) {
        Point p = positions.get(location);
        return new Point((int)(p.x * sx) + ox, (int)(p.y * sy) + oy);
    }

    private String shortLabel(String location) {
        switch (location) {
            case "Main Gate": return "MG";
            case "Admin Block": return "AB";
            case "Canteen": return "CA";
            case "Library": return "LI";
            case "Computer Lab": return "CL";
            case "Hostel": return "HO";
            case "Auditorium": return "AU";
            default: return "?";
        }
    }
}
