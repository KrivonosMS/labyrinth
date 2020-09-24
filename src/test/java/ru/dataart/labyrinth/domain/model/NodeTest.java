package ru.dataart.labyrinth.domain.model;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class NodeTest {
    @Test
    void shouldReturnTrueCheckIsNeighbourIfNeighbour() throws Exception {
        Node node1 = Node.create(NodeId.create(1), 2, 2, '.');
        Node node2 = Node.create(NodeId.create(2), 3, 2, '.');

        boolean isNeighbour = node1.isNeighbour(node2);

        assertThat(isNeighbour).isTrue();
    }

    @Test
    void shouldReturnFalseCheckIsNeighbourIfNotNeighbour() throws Exception {
        Node node1 = Node.create(NodeId.create(1), 2, 2, '.');
        Node node2 = Node.create(NodeId.create(2), 3, 3, '.');

        boolean isNeighbour = node1.isNeighbour(node2);

        assertThat(isNeighbour).isFalse();
    }

    @Test
    void shouldReturnFalseWhenCheckIsNeighbourIfTheSameNode() throws Exception {
        Node node = Node.create(NodeId.create(1), 2, 2, '.');

        boolean isNeighbour = node.isNeighbour(node);

        assertThat(isNeighbour).isFalse();
    }

    @Test
    void shouldThrowExceptionWhenNotCorrectSymbol() {
        NodeCreationException exception = assertThrows(NodeCreationException.class, () -> {
            Node.create(NodeId.create(1), 2, 3, 'C');
        });

        assertThat(exception).hasMessage("Некорректный символ 'C' с координатами '[2, 3]'. Возможные символы: '8', '.' , 'A', 'B'");
    }

    @Test
    void shouldReturnFalseWhenCheckWall() throws Exception {
        Node node = Node.create(NodeId.create(1), 2, 2, '.');

        boolean isWall = node.isWall();

        assertThat(isWall).isFalse();
    }

    @Test
    void shouldReturnTrueWhenCheckWall() throws Exception {
        Node node = Node.create(NodeId.create(1), 2, 2, '8');

        boolean isWall = node.isWall();

        assertThat(isWall).isTrue();
    }

    @Test
    void shouldCreateCorrectNode() throws Exception {
        Node node = Node.create(NodeId.create(1), 2, 3, '8');

        assertThat(node)
                .isNotNull()
                .matches(n -> n.nodeId().value() == 1)
                .matches(n -> n.x() == 2)
                .matches(n -> n.y() == 3)
                .matches(n -> n.symbol() =='8');
    }

    @Test
    void shouldReturnTrueWhenChekStartNode() throws Exception {
        Node node = Node.create(NodeId.create(1), 2, 2, 'A');

        boolean isStartNode = node.isStartNode();

        assertThat(isStartNode).isTrue();
    }

    @Test
    void shouldReturnTrueWhenChekEndNode() throws Exception {
        Node node = Node.create(NodeId.create(1), 2, 2, 'B');

        boolean isEndNode = node.isEndNode();

        assertThat(isEndNode).isTrue();
    }
}