package ru.dataart.labyrinth.domain.model;

public class Node {
    private NodeId nodeId;
    private int x;
    private int y;
    private char symbol;

    private Node(final NodeId nodeId, final int x, final int y, final char symbol) throws NodeCreationException {
        setId(nodeId);
        setX(x);
        setY(y);
        setSymbol(symbol);
    }

    static Node create(final NodeId nodeId, final int x, final int y, final char symbol) throws NodeCreationException {
        return new Node(nodeId, x, y, symbol);
    }

    private void setId(final NodeId nodeId) {
        this.nodeId = nodeId;
    }

    private void setX(final int x) {
        this.x = x;
    }

    private void setY(final int y) {
        this.y = y;
    }

    private void setSymbol(final char symbol) throws NodeCreationException {
        if (symbol != '8' && symbol != '.' && symbol != 'A' && symbol != 'B') {
            throw new NodeCreationException(String.format("Некорректный символ '%s' с координатами '[%s, %s]'. Возможные символы: '8', '.' , 'A', 'B'", symbol, x, y));
        }
        this.symbol = symbol;
    }

    public NodeId nodeId() {
        return nodeId;
    }

    public int x() {
        return x;
    }

    public int y() {
        return y;
    }

    public char symbol() {
        return symbol;
    }

    public boolean isStartNode() {
        return 'A' == this.symbol;
    }

    public boolean isEndNode() {
        return 'B' == this.symbol;
    }

    public boolean isNeighbour(final Node node) {
        return (Math.abs(this.x - node.x()) + Math.abs(this.y - node.y())) == 1;
    }

    public boolean isWall() {
        return this.symbol == '8';
    }
}