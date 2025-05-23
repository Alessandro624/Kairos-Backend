package it.unical.demacs.informatica.KairosBackend.exception;

public class EmailNotSentException extends RuntimeException {
    public EmailNotSentException(String message) {
        super(message);
    }
}
