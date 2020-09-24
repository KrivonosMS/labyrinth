package ru.dataart.labyrinth.infrastructure;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.dataart.labyrinth.domain.TemplateParser;
import ru.dataart.labyrinth.domain.TemplateParserException;
import ru.dataart.labyrinth.domain.model.Template;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

public class TxtFileTemplateParser implements TemplateParser {
    private static final Logger LOG = LoggerFactory.getLogger(TxtFileTemplateParser.class);

    private final String filePath;

    public TxtFileTemplateParser(final String filePath) {
        this.filePath = filePath;
    }

    @Override
    public Template parse() throws TemplateParserException {
        long start = System.currentTimeMillis();
        LOG.debug("method=parse action=\"начало парсинга шаблона\" filePath={}", filePath);

        Path templatePath = path();
        List<char[]> chars = parse(templatePath);

        long end = System.currentTimeMillis();
        LOG.debug("method=parse action=\"парсинг шаблона завершен\" filePath={} lineSize={} time={}ms", filePath, chars.size(), end - start);

        return Template.create(chars);
    }

    private Path path() throws TemplateParserException {
        try {
            Path path = Paths.get(filePath);
            if (Files.notExists(path)) {
                throw new TemplateParserException(
                        String.format("Не удалось найти файл '%s'. Проверьте наличие файла", filePath),
                        String.format("Ошибка при попытке обработки файла '%s' парсером. Файл отсутсвует", filePath)
                );
            }

            return path;
        } catch (TemplateParserException e) {
            throw e;
        } catch (Exception e) {
            throw new TemplateParserException(
                    String.format("Ошибка при обращении к файлу '%s'", filePath),
                    String.format("Непредвиденная ошибка при попытке получить путь к файлу '%s' парсером", filePath),
                    e
            );
        }
    }

    private List<char[]> parse(final Path templatePath) throws TemplateParserException {
        try {
            List<String> lines = Files.readAllLines(templatePath);
            List<char[]> chars = lines
                    .stream()
                    .map(String::toCharArray)
                    .collect(Collectors.toList());

            return chars;
        } catch (IOException e) {
            String errorMessage = String.format("Не удалось обработать файл '%s'", filePath);
            String detailContextMessage = String.format("Непредвиденная ошибка во время парсинга файла '%s'", filePath);
            throw new TemplateParserException(errorMessage, detailContextMessage, e);
        }
    }
}