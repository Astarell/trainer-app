package ru.mephi.trainer.service;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.mephi.trainer.entity.*;
import ru.mephi.trainer.entity.enums.AttemptStatus;
import ru.mephi.trainer.entity.enums.TaskType;
import ru.mephi.trainer.entity.enums.UserRole;
import ru.mephi.trainer.repository.ProfileRepository;
import ru.mephi.trainer.repository.TaskAttemptRepository;
import ru.mephi.trainer.repository.TaskRepository;
import ru.mephi.trainer.repository.TaskTrainerRepository;
import ru.mephi.trainer.rest.dto.response.test.MessageResponse;

import java.util.UUID;

@Slf4j
@ApplicationScoped
@RequiredArgsConstructor
public class TaskAttemptService {
    private final TaskAttemptRepository taskAttemptRepository;
    private final TaskTrainerRepository taskTrainerRepository;
    private final ProfileRepository profileRepository;
    private final TaskRepository taskRepository;

    public MessageResponse insertTaskAttempt(UUID userId, UUID trainerId, UUID taskId, TaskType type, String answer) {

        TaskTrainerEntity taskTrainer = taskTrainerRepository
                .findByTrainerIdAndTaskId(trainerId, taskId)
                .orElseThrow(() -> new RuntimeException("Задача не найдена в этом тренажёре"));
        UserEntity user = profileRepository.getUserData(userId);

        TaskEntity task = taskRepository.findByIdOptional(taskId)
                .orElseThrow(() -> new RuntimeException("Задача не найдена"));

        ObjectMapper mapper = new ObjectMapper();
        JsonNode config = mapper.readTree(task.getConfig());
        double attemptsCount = taskAttemptRepository.getAttemptsCount(taskId, userId);

        if (attemptsCount >= task.getConfig().)

        AttemptStatus status = AttemptStatus.REVIEW;;
        switch (task.getTaskType()) {
            case TaskType.OPEN_ANSWER -> {
            }
            case TaskType.SINGLE_CHOICE -> {

            }
        }

        TaskAttemptEntity attempt = TaskAttemptEntity.builder()
                .task(taskTrainer)
                .user(user)
                .userAnswer(answer)
                .points(0.0)
                .status(status)
                .build();
    }




}
