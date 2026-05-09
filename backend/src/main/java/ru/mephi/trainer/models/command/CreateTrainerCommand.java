package ru.mephi.trainer.models.command;

import java.util.UUID;

public record CreateTrainerCommand(String name, UUID userId) {
}
