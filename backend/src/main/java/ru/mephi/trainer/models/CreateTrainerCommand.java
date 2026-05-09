package ru.mephi.trainer.models;

import java.util.UUID;

public record CreateTrainerCommand(String name, UUID userId) {
}
