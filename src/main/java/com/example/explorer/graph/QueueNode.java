package com.example.explorer.graph;

import com.example.explorer.Position;

import java.util.ArrayList;
import java.util.List;

/**
 * Graph node for implementation of BFS.
 */
public class QueueNode {

    private Position position;
    private int distance;
    private List<Position> path;

    public QueueNode() {
        path = new ArrayList<>();
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public List<Position> getPath() {
        return path;
    }

    public void addPath(Position pos) {
        this.path.add(pos);
    }
}
