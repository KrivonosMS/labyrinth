package ru.dataart.labyrinth.domain.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Labyrinth {
    private static final Logger LOG = LoggerFactory.getLogger(Labyrinth.class);

    private final Map<NodeId, Node> nodes;
    private final int verticalSize;
    private final int horizontalSize;

    private Labyrinth(final Map<NodeId, Node> nodes, final int verticalSize, final int horizontalSize) {
        this.nodes = Collections.unmodifiableMap(nodes);
        this.verticalSize = verticalSize;
        this.horizontalSize = horizontalSize;
    }

    public static Labyrinth create(final Template template) throws LabyrinthCreationException {
        try {
            List<char[]> chars = template.chars();
            int verticalSize = chars.size();
            int horizontalSize = chars.get(0).length;
            Map<NodeId, Node> nodes = new HashMap<>();
            int count = 0;
            for (int i = 0; i < verticalSize; i++) {
                char[] currentLineOfChars = chars.get(i);
                if (currentLineOfChars.length != horizontalSize) {
                    throw new LabyrinthCreationException(
                            String.format("Длина строки %s не соответсвует длине грани лабиринта - %s", i + 1, verticalSize),
                            "Ошибка при формировании лабиринта. Некорректный размер"
                    );
                }
                for (int j = 0; j < horizontalSize; j++) {
                    Node node = Node.create(NodeId.create(count), i, j, currentLineOfChars[j]);
                    nodes.put(NodeId.create(count), node);
                    count++;
                }
            }
            validateStartNode(nodes);
            validateEndNode(nodes);
            return new Labyrinth(nodes, verticalSize, horizontalSize);
        } catch (NodeCreationException e) {
            throw new LabyrinthCreationException(e.clientMessage(), "Ощибка при формировании лабиринта", e);
        }
    }

    private static void validateStartNode(final Map<NodeId, Node> nodes) throws LabyrinthCreationException {
        List<Node> startNodes = nodes
                .entrySet()
                .stream()
                .map(Map.Entry::getValue)
                .filter(Node::isStartNode)
                .collect(Collectors.toList());
        if (startNodes.size() == 0) {
            throw new LabyrinthCreationException(
                    "Не задана начальная точка в либиринте",
                    "Ошибка при формировании лабиринта. Не задана начальная точка в либиринте"
            );
        }
        if (startNodes.size() > 1) {
            throw new LabyrinthCreationException(
                    "Должна быть задана только одна начальная точка в либиринте",
                    String.format("Ошибка при формировании лабиринта. Заданы %s начальные(ых) точки(ек) в либиринте", startNodes.size())
            );
        }
    }

    private static void validateEndNode(final Map<NodeId, Node> nodes) throws LabyrinthCreationException {
        List<Node> endNodes = nodes
                .values()
                .stream()
                .filter(Node::isEndNode)
                .collect(Collectors.toList());
        if (endNodes.size() == 0) {
            throw new LabyrinthCreationException(
                    "Не задана конечная точка в либиринте",
                    "Ошибка при формировании лабиринта. Не задана конечная точка в либиринте"
            );
        }
        if (endNodes.size() > 1) {
            throw new LabyrinthCreationException(
                    "Должна быть задана только одна конечная точка в либиринте",
                    String.format("Ошибка при формировании лабиринта. Заданы %s конечные(ых) точки(ек) в либиринте", endNodes.size())
            );
        }
    }

    public Node startNode() {
        return nodes
                .values()
                .stream()
                .filter(Node::isStartNode)
                .findFirst()
                .get();
    }

    public Node endNode() {
        return nodes
                .entrySet()
                .stream()
                .map(Map.Entry::getValue)
                .filter(Node::isEndNode)
                .findFirst()
                .get();
    }

    public LabyrinthWithOptimalRoad findOptimalRoad() throws LabyrinthNotFoundOptimalRoadException {
        long start = System.currentTimeMillis();
        LOG.debug("method=findOptimalRoad action=\"поиск оптимального пути в лабиринте с размерами {}х{}\"", verticalSize, horizontalSize);

        Map<NodeId, Node> nodesForOptimalRoad = new HashMap<>();
        Map<NodeId, StepLength> roads = findRoads();
        Node currentNode = endNode();
        Node startNode = startNode();
        nodesForOptimalRoad.put(endNode().nodeId(), endNode());
        while (notEquals(currentNode, startNode)) {
            for (NodeId nodeId : roads.keySet()) {
                Node node = nodes.get(nodeId);
                if (isNeighbourWithStepLengthMinusOne(roads, currentNode, nodeId, node)) {
                    currentNode = node;
                    nodesForOptimalRoad.put(currentNode.nodeId(), currentNode);
                }
            }
        }
        long end = System.currentTimeMillis();
        LOG.debug("method=findOptimalRoad action=\"завершен поиск оптимального пути в лабиринте с размерами {}х{}\" time={}ms", verticalSize, horizontalSize, end - start);

        return LabyrinthWithOptimalRoad.create(this, OptimalRoad.create(nodesForOptimalRoad));
    }

    private boolean isNeighbourWithStepLengthMinusOne(final Map<NodeId, StepLength> roads, final Node currentNode, final NodeId nodeId, final Node node) {
        return currentNode.isNeighbour(node) && roads.get(nodeId).value() == roads.get(currentNode.nodeId()).value() - 1;
    }

    private boolean notEquals(final Node currentNode, final Node startNode) {
        return currentNode.nodeId().value() != startNode.nodeId().value();
    }

    private Map<NodeId, StepLength> findRoads() throws LabyrinthNotFoundOptimalRoadException {
        Map<NodeId, StepLength> roadsMap = new HashMap<>();
        int stepLengthCount = 1;
        long maxIterationSize = nodes.size() * nodes.size();
        long count = 1;
        roadsMap.put(startNode().nodeId(), StepLength.create(stepLengthCount));
        while (!isEndNodeIdPresentIn(roadsMap)) {
            if (count > maxIterationSize) {
                throw new LabyrinthNotFoundOptimalRoadException(
                        "Не удалось найти проход в лабиринте",
                        String.format("Превышено максимальное ('%s') количество попыток поиска прохода в лабиринте", maxIterationSize)
                );
            }
            for (Map.Entry<NodeId, Node> entry : nodes.entrySet()) {
                Node currentNode = entry.getValue();
                NodeId currentNodeId = entry.getKey();
                if (isEdgeNodeInRoadMap(roadsMap, stepLengthCount, currentNodeId)) {
                    for (Map.Entry<NodeId, Node> entryNode : nodes.entrySet()) {
                        Node newNode = entryNode.getValue();
                        NodeId newNodeId = entryNode.getKey();
                        if (isNewEmptyNeighbourNotInRoadMap(roadsMap, currentNode, newNode)) {
                            roadsMap.put(newNodeId, StepLength.create(stepLengthCount + 1));
                        }
                    }
                }
            }
            count++;
            stepLengthCount++;
        }
        return roadsMap;
    }

    private boolean isEndNodeIdPresentIn(final Map<NodeId, StepLength> roadsMap) {
        return isNodeIdPresentInRoadMap(roadsMap, endNode());
    }

    private boolean isNodeIdPresentInRoadMap(final Map<NodeId, StepLength> roadsMap, final Node node) {
        return roadsMap.containsKey(node.nodeId());
    }

    private boolean isEdgeNodeInRoadMap(final Map<NodeId, StepLength> roadsMap, final int stepLengthCount, final NodeId nodeId) {
        return roadsMap.containsKey(nodeId) && roadsMap.get(nodeId).equals(StepLength.create(stepLengthCount));
    }

    private boolean isNewEmptyNeighbourNotInRoadMap(final Map<NodeId, StepLength> roadsMap, final Node currentNode, final Node node) {
        return !isNodeIdPresentInRoadMap(roadsMap, node) && !node.isWall() && currentNode.isNeighbour(node);
    }

    int verticalSize() {
        return verticalSize;
    }

    int horizontalSize() {
        return horizontalSize;
    }

    Map<NodeId, Node> nodes() {
        return nodes;
    }
}