package ru.mephi.trainer.service;

import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.mephi.trainer.entity.UserEntity;
import ru.mephi.trainer.repository.ProfileRepository;
import ru.mephi.trainer.repository.TaskRepository;
import ru.mephi.trainer.rest.dto.response.ProfileResponse;
import ru.mephi.trainer.rest.dto.response.TaskResponse;
import ru.mephi.trainer.rest.dto.response.TrainerProgressPercentResponse;

import java.util.List;
import java.util.UUID;

@Slf4j
@ApplicationScoped
@RequiredArgsConstructor
public class TaskService {
    private final TaskRepository taskRepository;

    public TaskResponse getTaskWithAttempt(UUID userId, UUID trainerId, UUID taskId) {
        log.info("Get task with user attempt: userId={}, trainerId={}, taskId={}", userId, trainerId, taskId);
        TaskResponse taskResponse = taskRepository.getTaskWithUserAttempt(userId, trainerId, taskId).orElseThrow(() -> {
            log.warn("Task not found: taskId={}, trainerId={}", taskId, trainerId);
            return new RuntimeException("Задача не найдена в этом тренажёре");
        });

        log.info("Task retrieved successfully: {}", taskResponse.getId());
        return taskResponse;
    }
}