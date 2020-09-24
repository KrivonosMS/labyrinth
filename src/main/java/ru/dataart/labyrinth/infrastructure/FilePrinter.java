package ru.dataart.labyrinth.infrastructure;

import ru.dataart.labyrinth.domain.Printer;
import ru.dataart.labyrinth.domain.PrinterException;
import ru.dataart.labyrinth.domain.model.LabyrinthWithOptimalRoad;
import ru.dataart.labyrinth.domain.model.Node;
import ru.dataart.labyrinth.domain.model.NodeId;
import ru.dataart.labyrinth.domain.model.OptimalRoad;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Map;

public class FilePrinter implements Printer {
    private final String fileOutputPath;

    public FilePrinter(final String fileOutputPath) {
        this.fileOutputPath = fileOutputPath;
    }

    @Override
    public void print(final LabyrinthWithOptimalRoad labyrinthWithOptimalRoad) throws PrinterException {
        try (Writer writer = new FileWriter(fileOutputPath)) {
            Map<NodeId, Node> nodes = labyrinthWithOptimalRoad.nodes();
            OptimalRoad optimalRoad = labyrinthWithOptimalRoad.optimalRoad();
            for (int i = 0; i < nodes.size(); i++) {
                if (isNextLine(labyrinthWithOptimalRoad.horizontalSize(), i)) {
                    writer.write("\n");
                }
                if (isRoad(optimalRoad, i)) {
                    writer.write('*');
                } else {
                    writer.write(nodes.get(NodeId.create(i)).symbol());
                }
            }
        } catch (IOException e) {
            throw new PrinterException(
                    "Возникла непредвиденная ошибка во время печати лабиринта в файл",
                    String.format("Ошибка во время печати лабиринта в файл. Количество узлов в лабиринте=%s", labyrinthWithOptimalRoad.nodes().size()),
                    e
            );
        }
    }

    private boolean isNextLine(final int horizontalSize, final int i) {
        return i % horizontalSize == 0 && i != 0;
    }

    private boolean isRoad(final OptimalRoad optimalRoad, final int i) {
        return optimalRoad.nodes().containsKey(NodeId.create(i))
                && !optimalRoad.nodes().get(NodeId.create(i)).isStartNode()
                && !optimalRoad.nodes().get(NodeId.create(i)).isEndNode();
    }
}