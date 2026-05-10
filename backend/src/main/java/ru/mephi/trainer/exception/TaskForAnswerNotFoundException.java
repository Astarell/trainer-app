package ru.mephi.trainer.exception;

public class TaskForAnswerNotFoundException extends RuntimeException {
    public TaskForAnswerNotFoundException(String message) {
        super(message);
    }
}