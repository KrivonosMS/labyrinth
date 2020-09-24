package ru.dataart.labyrinth.domain.model;

import java.util.List;

public class Template {
    private final List<char[]> chars;

    private Template(final List<char[]> chars) {
        this.chars = chars;
    }

    public static Template create(final List<char[]> chars) {
        return new Template(chars);
    }

    public List<char[]> chars() {
        return chars;
    }
}