package ru.dataart.labyrinth.infrastructure;

import org.assertj.core.data.Index;
import org.junit.jupiter.api.Test;
import ru.dataart.labyrinth.domain.TemplateParser;
import ru.dataart.labyrinth.domain.TemplateParserException;
import ru.dataart.labyrinth.domain.model.Template;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TxtFileTemplateParserTest {
    @Test
    void shouldThrowExceptionWhenFileIsNotExist() {
        String fakeFilePath = "/fake_file_name";
        TemplateParser templateParser = new TxtFileTemplateParser(fakeFilePath);

        TemplateParserException exception = assertThrows(TemplateParserException.class, () -> {
            templateParser.parse();
        });

        assertThat(exception).hasMessage("Ошибка при попытке обработки файла '/fake_file_name' парсером. Файл отсутсвует. clientMessage='Не удалось найти файл '/fake_file_name'. Проверьте наличие файла'");
    }

    @Test
    void shouldParsedFileWithoutExceptionsWhenFileExist() throws Exception {
        String filePath = this.getClass().getResource("/test.txt").getPath().substring(1);
        TemplateParser templateParser = new TxtFileTemplateParser(filePath);

        Template template = templateParser.parse();

        assertThat(template.chars())
                .isNotNull()
                .hasSize(2)
                .contains(new char[]{'8', '8'}, Index.atIndex(0))
                .contains(new char[]{'8', '.'}, Index.atIndex(1));
    }
}