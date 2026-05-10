package ru.mephi.trainer.service;


import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.mephi.trainer.entity.TaskEntity;
import ru.mephi.trainer.entity.TaskTrainerEntity;
import ru.mephi.trainer.entity.TrainerEntity;
import ru.mephi.trainer.entity.UserEntity;
import ru.mephi.trainer.exception.TaskNotFoundException;
import ru.mephi.trainer.exception.TaskTrainerNotFoundException;
import ru.mephi.trainer.exception.TrainerNotFoundException;
import ru.mephi.trainer.exception.UserNotFoundException;
import ru.mephi.trainer.repository.*;
import ru.mephi.trainer.rest.dto.request.TaskSubmitRequest;
import ru.mephi.trainer.rest.dto.response.TaskResponse;
import ru.mephi.trainer.rest.dto.response.TrainerInfoResponse;

import java.util.List;
import java.util.UUID;

@Slf4j
@ApplicationScoped
@RequiredArgsConstructor
public class TrainerService {
    private final TrainerRepository trainerRepository;
    private final TaskRepository taskRepository;
    private final TaskTrainerRepository taskTrainerRepository;
    private final TaskAttemptRepository taskAttemptRepository;
    private final UserRepository userRepository;

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


    public List<TaskResponse> getTrainerTasks (UUID id) {
        log.info("Get task of trainer: id={}", id);

        // проверка наличия тренажера
        TrainerEntity entity = trainerRepository.findByIdOptional(id)
                .orElseThrow(() -> {
                    log.warn("Get trainer info failed - id not found: id={}", id);
                    return new TrainerNotFoundException("Тренажёр с id " + id + " не найден");
                });

        // получим задачи тренажера
        return taskTrainerRepository.getTrainerTasks(id);
    }

    @Transactional
    public String saveTrainerTasksSubmit (UUID trainerId, UUID taskId, TaskSubmitRequest taskSubmitRequest) {
        log.info("Save task of trainer: id={}, task_id", trainerId, taskId);

        // проверка наличия тренажера
        TrainerEntity trainerEntity = trainerRepository.findByIdOptional(trainerId)
                .orElseThrow(() -> {
                    log.warn("Get trainer info failed - id not found: id={}", trainerId);
                    return new TrainerNotFoundException("Тренажёр с id " + trainerId + " не найден");
                });

        // проверка наличия задачи
        TaskEntity taskEntity = taskRepository.findByIdOptional(taskId)
                .orElseThrow(() -> {
                    log.warn("Get task info failed - id not found: id={}", taskId);
                    return new TaskNotFoundException("Задача с id " + taskId + " не найдена");
                });

        // найдем запись объединения задачи и тренажера
        TaskTrainerEntity taskTrainerEntity = taskTrainerRepository.findByTaskAndTrainer(trainerId, taskId)
                .orElseThrow(() -> {
                    log.warn("Get task trainer info failed - id not found: trainer_id={}, task_id={}",
                            trainerId.toString(), taskId.toString());
                    return new TaskTrainerNotFoundException("Тренажер с id "
                            + trainerId.toString() + " и задача с id " + taskId.toString() + " не связаны");
                });

        // найдем пользователя
        UserEntity userEntity = userRepository.findByIdOptional(taskSubmitRequest.getUser())
                .orElseThrow(() -> {
                    log.warn("Get user info failed - id not found: id={}", taskSubmitRequest.getUser());
                    return new UserNotFoundException("Пользователь с id " + taskSubmitRequest.getUser() + " не найден");
                });

        // сохраняем данные о попытке
        taskAttemptRepository.saveTrainerTasksSubmit(taskTrainerEntity, userEntity, taskSubmitRequest);

        return "Ответ на задачу сохранен";
    }
}
