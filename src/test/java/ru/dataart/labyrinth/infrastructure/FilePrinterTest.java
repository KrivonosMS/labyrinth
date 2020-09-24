package ru.dataart.labyrinth.infrastructure;

import org.junit.jupiter.api.Test;
import ru.dataart.labyrinth.domain.model.Labyrinth;
import ru.dataart.labyrinth.domain.model.LabyrinthWithOptimalRoad;
import ru.dataart.labyrinth.domain.model.Template;

import java.io.File;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

class FilePrinterTest {
    @Test
    void shouldPrintCorrectFile() throws Exception {
        Template template = Template.create(
                Arrays.asList(
                        new char[]{'8', '8', '8', '8', '8'},
                        new char[]{'.', '.', 'A', '.', '8'},
                        new char[]{'8', '8', '8', '.', '8'},
                        new char[]{'8', 'B', '.', '.', '8'},
                        new char[]{'8', '8', '8', '.', '8'}
                )
        );
        Labyrinth labyrinth = Labyrinth.create(template);
        LabyrinthWithOptimalRoad labyrinthWithOptimalRoad = labyrinth.findOptimalRoad();
        FilePrinter filePrinter = new FilePrinter(this.getClass().getResource("/actual.txt").getPath().substring(1));

        filePrinter.print(labyrinthWithOptimalRoad);

        File actualFile = new File(this.getClass().getResource("/actual.txt").getPath().substring(1));
        File expectedFile = new File(this.getClass().getResource("/expected.txt").getPath().substring(1));
        assertThat(actualFile).hasSameContentAs(expectedFile);
    }
}