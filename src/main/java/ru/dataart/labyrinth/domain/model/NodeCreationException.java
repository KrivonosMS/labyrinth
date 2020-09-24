package ru.dataart.labyrinth.domain.model;

public class NodeCreationException extends Exception {
    private final String clientMessage;

    NodeCreationException(final String clientMessage) {
        super(clientMessage);

        this.clientMessage = clientMessage;
    }

    public String clientMessage() {
        return clientMessage;
    }
}