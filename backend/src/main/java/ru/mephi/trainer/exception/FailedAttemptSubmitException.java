package ru.mephi.trainer.exception;

public class FailedAttemptSubmitException extends RuntimeException {
    public FailedAttemptSubmitException(String message) {
        super(message);
    }
}
