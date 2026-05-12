package ru.mephi.trainer.service.attempt;

import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;
import ru.mephi.trainer.entity.TaskAttemptEntity;
import ru.mephi.trainer.entity.TaskEntity;
import ru.mephi.trainer.entity.TaskTrainerEntity;
import ru.mephi.trainer.entity.UserEntity;
import ru.mephi.trainer.entity.enums.AttemptStatus;
import ru.mephi.trainer.exception.CurrentUserNotFoundException;
import ru.mephi.trainer.exception.EntityNotFoundException;
import ru.mephi.trainer.exception.LastTaskAttemptStatusNotFailedException;
import ru.mephi.trainer.exception.UserUseMaxAttemptsLimitException;
import ru.mephi.trainer.models.taskconfig.TaskConfig;
import ru.mephi.trainer.repository.ProfileRepository;
import ru.mephi.trainer.repository.TaskAttemptRepository;
import ru.mephi.trainer.repository.TaskRepository;
import ru.mephi.trainer.repository.TaskTrainerRepository;

import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
@RequiredArgsConstructor
public class TaskAttemptValidator {
    private final TaskTrainerRepository taskTrainerRepository;
    private final ProfileRepository profileRepository;
    private final TaskRepository taskRepository;
    private final TaskAttemptRepository taskAttemptRepository;

    public TaskTrainerEntity validateAndGetTaskTrainer(UUID trainerId, UUID taskId) {
        return taskTrainerRepository
                .findByTrainerIdAndTaskId(trainerId, taskId)
                .orElseThrow(() -> new EntityNotFoundException("Задача для отправки ответа не найдена"));
    }

    public UserEntity validateAndGetUser(UUID userId) {
        UserEntity user = profileRepository.getUserData(userId);
        if (user == null) {
            throw new CurrentUserNotFoundException("Пользователь не найден");
        }
        return user;
    }

    public TaskEntity validateAndGetTask(UUID taskId) {
        return taskRepository.findByIdOptional(taskId)
                .orElseThrow(() -> new EntityNotFoundException("Задача не найдена"));
    }

    public <T extends TaskConfig> void validateAttemptsLimit(UUID taskTrainerId, UUID userId, T config) {
        int attemptsCount = taskAttemptRepository.getAttemptsCount(taskTrainerId, userId);

        if (attemptsCount >= config.getMaxAttempts()) {
            throw new UserUseMaxAttemptsLimitException(
                    "Использованы все попытки (максимум: " + config.getMaxAttempts() + ")"
            );
        }
    }

    public void validateLastAttemptStatus(UUID userId, UUID taskTrainerId) {
        Optional<TaskAttemptEntity> lastAttempt = taskAttemptRepository
                .findLastAttempt(userId, taskTrainerId);

        if (lastAttempt.isEmpty()) {
            return;
        }

        TaskAttemptEntity attempt = lastAttempt.get();
        if (attempt.getStatus() == AttemptStatus.COMPLETED) {
            throw new LastTaskAttemptStatusNotFailedException("Задача уже решена правильно");
        }
        if (attempt.getStatus() == AttemptStatus.REVIEW) {
            throw new LastTaskAttemptStatusNotFailedException("Предыдущий ответ уже на проверке");
        }
    }
}