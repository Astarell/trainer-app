package ru.mephi.trainer.service;


import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.mephi.trainer.entity.TrainerEntity;
import ru.mephi.trainer.exception.InvalidCommandException;
import ru.mephi.trainer.exception.TrainerNotFoundException;
import ru.mephi.trainer.models.command.CreateTrainerCommand;
import ru.mephi.trainer.repository.TrainerRepository;
import ru.mephi.trainer.rest.dto.response.trainer.TrainerInfoResponse;

import java.util.List;
import java.util.UUID;

@Slf4j
@ApplicationScoped
@RequiredArgsConstructor
public class TrainerService {
    private final TrainerRepository trainerRepository;

    public List<TrainerEntity> getAllTrainers() {
        log.info("Get all trainers");
        return trainerRepository.listAll();
    }

    public TrainerInfoResponse getTrainerInfo(UUID id) {
        log.info("Get trainer info: id={}", id);

        TrainerEntity entity = trainerRepository.findByIdOptional(id)
                .orElseThrow(() -> {
                    log.warn("Get trainer info failed - id not found: id={}", id);
                    return new TrainerNotFoundException("Тренажёр с id " + id + " не найден");
                });

        Integer totalTasks = trainerRepository.getTotalTasks(id);

        return TrainerInfoResponse.builder()
                .id(entity.getId())
                .name(entity.getName())
                .totalTasks(totalTasks)
                .createdAt(entity.getCreatedAt())
                .createdBy(entity.getCreatedBy())
                .build();
    }

    @Transactional
    public TrainerEntity createTrainer(CreateTrainerCommand createTrainerCommand) {
        log.info("User {} creating trainer {}", createTrainerCommand.userId(), createTrainerCommand.name());
        validateCreateTrainerCommand(createTrainerCommand);

        TrainerEntity trainer = buildTrainerEntity(createTrainerCommand);
        trainerRepository.persist(trainer);

        return trainer;
    }

    private void validateCreateTrainerCommand(CreateTrainerCommand command) {
        if (command == null) {
            throw new InvalidCommandException("Command can not be null");
        }
        if (command.name() == null || command.name().trim().length() < 3) {
            throw new InvalidCommandException("Trainer name must contain at least 3 characters");
        }
    }

    private TrainerEntity buildTrainerEntity(CreateTrainerCommand createTrainerCommand) {
        return TrainerEntity.builder()
                .name(createTrainerCommand.name())
                .createdBy(createTrainerCommand.userId())
                .build();
    }
}
