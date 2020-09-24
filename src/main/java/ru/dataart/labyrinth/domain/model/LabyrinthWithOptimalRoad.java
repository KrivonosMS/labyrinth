package ru.dataart.labyrinth.domain.model;

import ru.dataart.labyrinth.domain.Printer;
import ru.dataart.labyrinth.domain.PrinterException;

import java.util.Map;

public class LabyrinthWithOptimalRoad {
    private final Map<NodeId, Node> nodes;
    private final int verticalSize;
    private final int horizontalSize;
    private final OptimalRoad optimalRoad;

    private LabyrinthWithOptimalRoad(final Map<NodeId, Node> nodes, final int verticalSize, final int horizontalSize, final OptimalRoad optimalRoad) {
        this.nodes = nodes;
        this.verticalSize = verticalSize;
        this.horizontalSize = horizontalSize;
        this.optimalRoad = optimalRoad;
    }

    static LabyrinthWithOptimalRoad create(final Labyrinth labyrinth, final OptimalRoad optimalRoad) {
        return new LabyrinthWithOptimalRoad(
                labyrinth.nodes(),
                labyrinth.verticalSize(),
                labyrinth.horizontalSize(),
                optimalRoad
        );
    }

    public Map<NodeId, Node> nodes() {
        return nodes;
    }

    public int verticalSize() {
        return verticalSize;
    }

    public int horizontalSize() {
        return horizontalSize;
    }

    public OptimalRoad optimalRoad() {
        return optimalRoad;
    }

    public void print(final Printer printer) throws PrinterException {
        printer.print(this);
    }
}