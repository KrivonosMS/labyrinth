package ru.dataart.labyrinth.domain.model;

public class LabyrinthNotFoundOptimalRoadException extends Exception {
    private final String clientMessage;

    LabyrinthNotFoundOptimalRoadException(final String clientMessage, final String detailContextMessage) {
        super(String.format("%s. clientMessage='%s'", detailContextMessage, clientMessage));

        this.clientMessage = clientMessage;
    }

    public String clientMessage() {
        return clientMessage;
    }
}