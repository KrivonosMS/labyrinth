package ru.dataart.labyrinth.domain.model;

import java.util.Objects;

public class NodeId {
    private final int id;

    private NodeId(final int id) {
        this.id = id;
    }

    public static NodeId create(final int id) {
        return new NodeId(id);
    }

    public int value() {
        return id;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NodeId nodeId = (NodeId) o;
        return id == nodeId.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}