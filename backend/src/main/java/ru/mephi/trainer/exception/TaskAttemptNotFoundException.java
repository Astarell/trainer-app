package ru.mephi.trainer.exception;

public class TaskAttemptNotFoundException extends RuntimeException {
    public TaskAttemptNotFoundException(String message) {
        super(message);
    }
}
