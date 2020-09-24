package ru.dataart.labyrinth;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.dataart.labyrinth.config.Configuration;
import ru.dataart.labyrinth.domain.Printer;
import ru.dataart.labyrinth.domain.PrinterException;
import ru.dataart.labyrinth.domain.TemplateParser;
import ru.dataart.labyrinth.domain.TemplateParserException;
import ru.dataart.labyrinth.domain.model.*;
import ru.dataart.labyrinth.infrastructure.FilePrinter;
import ru.dataart.labyrinth.infrastructure.TxtFileTemplateParser;

public class App {
    private static final Logger LOG = LoggerFactory.getLogger(App.class);

    public static void main(final String[] args) {
        try {
            TemplateParser templateParser = new TxtFileTemplateParser(Configuration.INPUT_FILE_PATH);
            Template template = templateParser.parse();
            Labyrinth labyrinth = Labyrinth.create(template);
            LabyrinthWithOptimalRoad labyrinthWithOptimalRoad = labyrinth.findOptimalRoad();
            Printer printer = new FilePrinter(Configuration.OUTPUT_FILE_PATH);
            labyrinthWithOptimalRoad.print(printer);
        } catch (TemplateParserException e) {
            LOG.error(e.clientMessage(), e);
        } catch (LabyrinthCreationException e) {
            LOG.error(e.clientMessage(), e);
        } catch (LabyrinthNotFoundOptimalRoadException e) {
            LOG.error(e.clientMessage(), e);
        } catch (PrinterException e) {
            LOG.error(e.clientMessage(), e);
        }
    }
}