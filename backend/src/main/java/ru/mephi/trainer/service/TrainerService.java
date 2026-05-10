package ru.mephi.trainer.service;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.mephi.trainer.entity.TaskEntity;
import ru.mephi.trainer.entity.TaskTrainerEntity;
import ru.mephi.trainer.entity.TrainerEntity;
import ru.mephi.trainer.entity.UserEntity;
import ru.mephi.trainer.entity.enums.AttemptStatus;
import ru.mephi.trainer.entity.enums.TaskType;
import ru.mephi.trainer.exception.*;
import ru.mephi.trainer.models.tasks.*;
import ru.mephi.trainer.repository.*;
import ru.mephi.trainer.rest.dto.request.TaskSubmitRequest;
import ru.mephi.trainer.rest.dto.response.TaskResponse;
import ru.mephi.trainer.rest.dto.response.TrainerInfoResponse;

import java.util.HashMap;
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
    private final ObjectMapper mapper;

    private final static String LIMIT = "Превышено количество ответов на задачу";
    private final static String INCORRECT_TYPE = "Некорректный тип задачи";

    private final static String SUCCESS = "Попытка успешна";
    private final static String REVIEW = "Попытка принята и направлена на проверку";
    private final static String FAIL = "Попытка не успешна";

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

        // преобразуем задачу
        try {
            TaskResponse task = taskRepository.getTask(taskId)
                    .orElseThrow(() -> {
                        log.warn("Get task info failed - id not found: id={}", taskId);
                        return new TaskNotFoundException("Задача с id " + taskId + " не найдена");
                    });

            if (TaskType.SINGLE_CHOICE.name().equals(task.getTaskType())) {
                // прочитаем задачу
                SingleChoiceModel taskModel = mapper.readValue(task.getConfig(), SingleChoiceModel.class);

                // прочитаем ответ пользователя
                SingleChoiceModel answerModel = mapper.readValue(taskSubmitRequest.getUserAnswer(), SingleChoiceModel.class);

                // превышено количество попыток ответа на вопрос?
                Integer minusPoints = 0;
                Integer attemptsCount = taskAttemptRepository.getAttemptsCount(taskEntity.getId(), userEntity.getId());
                if (attemptsCount >= taskModel.getMaxAttempts()) {
                    throw new FailedAttemptSubmitException(LIMIT);
                } else {
                    // получить сколько оставлось возможно баллов за задачу
                    minusPoints = taskAttemptRepository.getMistakeCost(taskEntity.getId());
                }

                // проверим
                boolean valid = false;
                for (AnswerChoices ac : answerModel.getAnswerChoices()) {
                    if (ac.getOrdinal() == taskModel.getExpectedOrdinal() && ac.getChecked()) {
                        // правильно
                        valid = true;
                        break;
                    }
                }

                if (valid) {
                    // сохранение попытки
                    taskAttemptRepository.saveTrainerTasksSubmit(taskTrainerEntity, userEntity,
                            taskSubmitRequest.getUserAnswer(),
                            taskModel.getPoints() - minusPoints,
                            AttemptStatus.COMPLETED);

                    return SUCCESS;
                } else {
                    // сохранение попытки
                    taskAttemptRepository.saveTrainerTasksSubmit(taskTrainerEntity, userEntity,
                            taskSubmitRequest.getUserAnswer(),
                            0,
                            AttemptStatus.FAILED);

                    return FAIL;
                }

            } else if (TaskType.MULTIPLE_CHOICE.name().equals(task.getTaskType())) {
                // прочитаем задачу
                MultiChoiceModel taskModel = mapper.readValue(task.getConfig(), MultiChoiceModel.class);

                // прочитаем ответ пользователя
                MultiChoiceModel answerModel = mapper.readValue(taskSubmitRequest.getUserAnswer(), MultiChoiceModel.class);

                // превышено количество попыток ответа на вопрос?
                Integer minusPoints = 0;
                Integer attemptsCount = taskAttemptRepository.getAttemptsCount(taskEntity.getId(), userEntity.getId());
                if (attemptsCount >= taskModel.getMaxAttempts()) {
                    throw new FailedAttemptSubmitException(LIMIT);
                } else {
                    // получить сколько оставлось возможно баллов за задачу
                    minusPoints = taskAttemptRepository.getMistakeCost(taskEntity.getId());
                }

                // проверим
                boolean valid = true;
                HashMap<Integer, Boolean> mapTrueAnswer = new HashMap<>();
                if (taskModel.getExpectedOrdinals() != null) {
                    for (Integer expectedOrdinal : taskModel.getExpectedOrdinals()) {
                        mapTrueAnswer.put(expectedOrdinal, Boolean.TRUE);
                    }
                }

                for (AnswerChoices ac : answerModel.getAnswerChoices()) {
                    if (mapTrueAnswer.get(ac.getOrdinal()) == null) {
                        // неправильно
                        valid = false;
                        break;
                    }
                    if (ac.getChecked() && mapTrueAnswer.get(ac.getOrdinal()) == null) {
                        // неправильно
                        valid = false;
                        break;
                    }
                }

                if (valid) {
                    // сохранение попытки
                    taskAttemptRepository.saveTrainerTasksSubmit(taskTrainerEntity, userEntity,
                            taskSubmitRequest.getUserAnswer(),
                            taskModel.getPoints() - minusPoints,
                            AttemptStatus.COMPLETED);

                    return SUCCESS;
                } else {
                    // сохранение попытки
                    taskAttemptRepository.saveTrainerTasksSubmit(taskTrainerEntity, userEntity,
                            taskSubmitRequest.getUserAnswer(),
                            0,
                            AttemptStatus.FAILED);

                    return FAIL;
                }

            } else if (TaskType.ERROR_FINDING.name().equals(task.getTaskType())) {
                // прочитаем задачу
                ErrorFindingModel taskModel = mapper.readValue(task.getConfig(), ErrorFindingModel.class);

                // прочитаем ответ пользователя
                ErrorFindingModel answerModel = mapper.readValue(taskSubmitRequest.getUserAnswer(), ErrorFindingModel.class);

                // превышено количество попыток ответа на вопрос?
                Integer minusPoints = 0;
                Integer attemptsCount = taskAttemptRepository.getAttemptsCount(taskEntity.getId(), userEntity.getId());
                if (attemptsCount >= taskModel.getMaxAttempts()) {
                    throw new FailedAttemptSubmitException(LIMIT);
                } else {
                    // получить сколько оставлось возможно баллов за задачу
                    minusPoints = taskAttemptRepository.getMistakeCost(taskEntity.getId());
                }

                // проверим
                boolean valid = true;
                HashMap<Integer, Boolean> mapTrueAnswer = new HashMap<>();
                if (taskModel.getExpectedOrdinals() != null) {
                    for (Integer expectedOrdinal : taskModel.getExpectedOrdinals()) {
                        mapTrueAnswer.put(expectedOrdinal, Boolean.TRUE);
                    }
                }

                for (AnswerChoices ac : answerModel.getAnswerChoices()) {
                    if (mapTrueAnswer.get(ac.getOrdinal()) == null) {
                        // неправильно
                        valid = false;
                        break;
                    }
                }

                if (valid) {
                    // сохранение попытки
                    taskAttemptRepository.saveTrainerTasksSubmit(taskTrainerEntity, userEntity,
                            taskSubmitRequest.getUserAnswer(),
                            taskModel.getPoints() - minusPoints,
                            AttemptStatus.COMPLETED);

                    return SUCCESS;
                } else {
                    // сохранение попытки
                    taskAttemptRepository.saveTrainerTasksSubmit(taskTrainerEntity, userEntity,
                            taskSubmitRequest.getUserAnswer(),
                            0,
                            AttemptStatus.FAILED);

                    return FAIL;
                }

            } else if (TaskType.OPEN_ANSWER.name().equals(task.getTaskType())) {
                // прочитаем задачу
                OpenAnswerModel taskModel = mapper.readValue(task.getConfig(), OpenAnswerModel.class);

                // прочитаем ответ пользователя
                OpenAnswerModel answerModel = mapper.readValue(taskSubmitRequest.getUserAnswer(), OpenAnswerModel.class);

                // превышено количество попыток ответа на вопрос?
                Integer minusPoints = 0;
                Integer attemptsCount = taskAttemptRepository.getAttemptsCount(taskEntity.getId(), userEntity.getId());
                if (attemptsCount >= taskModel.getMaxAttempts()) {
                    throw new FailedAttemptSubmitException(LIMIT);
                } else {
                    // получить сколько оставлось возможно баллов за задачу
                    minusPoints = taskAttemptRepository.getMistakeCost(taskEntity.getId());
                }

                // сохранение попытки
                taskAttemptRepository.saveTrainerTasksSubmit(taskTrainerEntity, userEntity,
                        taskSubmitRequest.getUserAnswer(),
                        0,
                        AttemptStatus.REVIEW);

                return REVIEW;

            } else {
                throw new FailedAttemptSubmitException(INCORRECT_TYPE);
            }
        } catch (JsonProcessingException exc) {
            log.error(exc.getMessage(), exc);
            throw new FailedAttemptSubmitException(exc.getMessage());
        }
    }
}
