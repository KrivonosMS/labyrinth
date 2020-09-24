package ru.dataart.labyrinth.domain.model;

public class LabyrinthCreationException extends Exception {
    private final String clientMessage;

    LabyrinthCreationException(final String clientMessage, final String detailContextMessage, final Throwable cause) {
        super(String.format("%s. clientMessage='%s'", detailContextMessage, clientMessage), cause);

        this.clientMessage = clientMessage;
    }

    LabyrinthCreationException(final String clientMessage, final String detailContextMessage) {
        super(String.format("%s. clientMessage='%s'", detailContextMessage, clientMessage));

        this.clientMessage = clientMessage;
    }

    public String clientMessage() {
        return clientMessage;
    }
}