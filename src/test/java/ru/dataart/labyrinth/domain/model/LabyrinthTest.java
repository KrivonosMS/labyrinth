package ru.dataart.labyrinth.domain.model;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class LabyrinthTest {
    @Test
    void shouldThrowExceptionWhenCreateLabyrinthAndTwoStartNode() {
        List<char[]> chars = Arrays.asList(
                new char[]{'8', 'A', '8'},
                new char[]{'8', 'B', '8'},
                new char[]{'8', 'A', '8'}
        );
        Template template = Template.create(chars);

        LabyrinthCreationException exception = assertThrows(LabyrinthCreationException.class, () -> {
            Labyrinth.create(template);
        });

        assertThat(exception).hasMessage("Ошибка при формировании лабиринта. Заданы 2 начальные(ых) точки(ек) в либиринте. clientMessage='Должна быть задана только одна начальная точка в либиринте'");
    }

    @Test
    void shouldThrowExceptionWhenCreateLabyrinthAndNoStartNode() {
        List<char[]> chars = Arrays.asList(
                new char[]{'8', '.', '8'},
                new char[]{'8', 'B', '8'},
                new char[]{'8', '.', '8'}
        );
        Template template = Template.create(chars);

        LabyrinthCreationException exception = assertThrows(LabyrinthCreationException.class, () -> {
            Labyrinth.create(template);
        });

        assertThat(exception).hasMessage("Ошибка при формировании лабиринта. Не задана начальная точка в либиринте. clientMessage='Не задана начальная точка в либиринте'");
    }

    @Test
    void shouldThrowExceptionWhenCreateLabyrinthAndTwoEndNode() {
        List<char[]> chars = Arrays.asList(
                new char[]{'8', 'A', '8'},
                new char[]{'8', 'B', '8'},
                new char[]{'8', 'B', '8'}
        );
        Template template = Template.create(chars);

        LabyrinthCreationException exception = assertThrows(LabyrinthCreationException.class, () -> {
            Labyrinth.create(template);
        });

        assertThat(exception).hasMessage("Ошибка при формировании лабиринта. Заданы 2 конечные(ых) точки(ек) в либиринте. clientMessage='Должна быть задана только одна конечная точка в либиринте'");
    }

    @Test
    void shouldThrowExceptionWhenCreateLabyrinthAndNoEndNode() {
        List<char[]> chars = Arrays.asList(
                new char[]{'8', 'A', '8'},
                new char[]{'8', '.', '8'},
                new char[]{'8', '.', '8'}
        );
        Template template = Template.create(chars);

        LabyrinthCreationException exception = assertThrows(LabyrinthCreationException.class, () -> {
            Labyrinth.create(template);
        });

        assertThat(exception).hasMessage("Ошибка при формировании лабиринта. Не задана конечная точка в либиринте. clientMessage='Не задана конечная точка в либиринте'");
    }

    @Test
    void shouldThrowExceptionWhenCreateLabyrinthAndItNotRectangular() {
        List<char[]> chars = Arrays.asList(
                new char[]{'8', 'A', '8'},
                new char[]{'8', '.', '8', '8'},
                new char[]{'8', '.', '8'}
        );
        Template template = Template.create(chars);

        LabyrinthCreationException exception = assertThrows(LabyrinthCreationException.class, () -> {
            Labyrinth.create(template);
        });

        assertThat(exception).hasMessage("Ошибка при формировании лабиринта. Некорректный размер. clientMessage='Длина строки 2 не соответсвует длине грани лабиринта - 3'");
    }

    @Test
    void shouldCreateLabyrinthWithoutExceptions() throws Exception {
        List<char[]> chars = Arrays.asList(
                new char[]{'8', 'A', '8'},
                new char[]{'8', '.', '8'},
                new char[]{'8', 'B', '8'},
                new char[]{'8', '.', '8'}
        );
        Template template = Template.create(chars);

        Labyrinth labyrinth = Labyrinth.create(template);

        assertThat(labyrinth)
                .isNotNull()
                .matches(l -> l.verticalSize() == 4)
                .matches(l -> l.horizontalSize() == 3)
                .matches(l -> l.startNode().nodeId().value() == 1)
                .matches(l -> l.endNode().nodeId().value() == 7)
                .matches(l -> l.nodes().size() == 12)
                .matches(l -> l.nodes().get(NodeId.create(4)).symbol() == '.');
    }

    @Test
    void shouldFindOptimalRoad() throws Exception {
        Template template = Template.create(
                Arrays.asList(
                        new char[]{'8', '8', '8', '8', '8'},
                        new char[]{'.', '.', 'A', '.', '8'},
                        new char[]{'8', '.', '8', '.', '8'},
                        new char[]{'8', 'B', '.', '.', '8'},
                        new char[]{'8', '8', '8', '.', '8'}
                )
        );
        Labyrinth labyrinth = Labyrinth.create(template);

        LabyrinthWithOptimalRoad labyrinthOptimalRoad = labyrinth.findOptimalRoad();

        assertThat(labyrinthOptimalRoad.optimalRoad().nodes())
                .isNotNull()
                .hasSize(4)
                .matches(n -> n.containsKey(NodeId.create(16)))
                .matches(n -> n.containsKey(NodeId.create(11)))
                .matches(n -> n.containsKey(NodeId.create(6)))
                .matches(n -> n.containsKey(NodeId.create(7)));
    }

    @Test
    void shouldThrowExceptionWhenNoRoad() throws Exception {
        Template template = Template.create(
                Arrays.asList(
                        new char[]{'8', '8', '8', '8', '8'},
                        new char[]{'.', '.', 'A', '.', '8'},
                        new char[]{'8', '8', '8', '.', '8'},
                        new char[]{'8', 'B', '8', '.', '8'},
                        new char[]{'8', '8', '8', '.', '8'}
                )
        );
        Labyrinth labyrinth = Labyrinth.create(template);

        LabyrinthNotFoundOptimalRoadException exception = assertThrows(LabyrinthNotFoundOptimalRoadException.class, () -> {
            labyrinth.findOptimalRoad();
        });

        assertThat(exception).hasMessage("Превышено максимальное ('625') количество попыток поиска прохода в лабиринте. clientMessage='Не удалось найти проход в лабиринте'");
    }
}