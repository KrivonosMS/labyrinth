package ru.dataart.labyrinth.domain.model;

import java.util.Objects;

public class StepLength {
    private final int length;

    private StepLength(final int length) {
        this.length = length;
    }

    static StepLength create(final int length) {
        return new StepLength(length);
    }

    public int value() {
        return length;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepLength that = (StepLength) o;
        return length == that.length;
    }

    @Override
    public int hashCode() {
        return Objects.hash(length);
    }
}