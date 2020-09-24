package ru.dataart.labyrinth.domain;

import ru.dataart.labyrinth.domain.model.LabyrinthWithOptimalRoad;

public interface Printer {
    void print(LabyrinthWithOptimalRoad labyrinthWithOptimalRoad) throws PrinterException;
}