package ru.dataart.labyrinth.domain;

import ru.dataart.labyrinth.domain.model.Template;

public interface TemplateParser {
    Template parse() throws TemplateParserException;
}