package ru.dataart.labyrinth.domain.model;

import java.util.Map;

public class OptimalRoad {
    private final Map<NodeId, Node> nodes;

    private OptimalRoad(final Map<NodeId, Node> nodes) {
        this.nodes = nodes;
    }

    static OptimalRoad create(final Map<NodeId, Node> nodes) {
        return new OptimalRoad(nodes);
    }

    public Map<NodeId, Node> nodes() {
        return nodes;
    }
}