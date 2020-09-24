package ru.dataart.labyrinth.domain;

public class PrinterException extends Exception {
    private final String clientMessage;

    public PrinterException(final String clientMessage, final String detailContextMessage, final Throwable cause) {
        super(String.format("%s. clientMessage='%s'", detailContextMessage, clientMessage), cause);

        this.clientMessage = clientMessage;
    }

    public PrinterException(final String clientMessage, final String detailContextMessage) {
        super(String.format("%s. clientMessage='%s'", detailContextMessage, clientMessage));

        this.clientMessage = clientMessage;
    }

    public String clientMessage() {
        return clientMessage;
    }
}