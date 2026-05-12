package ru.mephi.trainer.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.mephi.trainer.entity.TaskEntity;
import ru.mephi.trainer.entity.TaskTrainerEntity;
import ru.mephi.trainer.entity.UserEntity;
import ru.mephi.trainer.entity.enums.TaskType;
import ru.mephi.trainer.exception.EntityNotFoundException;
import ru.mephi.trainer.models.attempt.answer.UserAnswer;
import ru.mephi.trainer.models.taskconfig.TaskConfig;
import ru.mephi.trainer.repository.TaskTrainerRepository;
import ru.mephi.trainer.repository.UserRepository;
import ru.mephi.trainer.rest.dto.response.MessageResponse;
import ru.mephi.trainer.service.attempt.TaskAttemptHandler;
import ru.mephi.trainer.service.attempt.TaskAttemptValidator;
import ru.mephi.trainer.service.attempt.UserAnswerParser;

import java.util.UUID;

@Slf4j
@ApplicationScoped
@RequiredArgsConstructor
public class TaskAttemptService {

    private final TaskAttemptValidator validator;
    private final TaskConfigFactory configFactory;
    private final UserAnswerParser answerParser;
    private final TaskAttemptHandler attemptHandler;
    private final TaskService taskService;
    private final UserRepository userRepository;
    private final TaskTrainerRepository taskTrainerRepository;

    @Transactional
    public MessageResponse submitTaskAttempt(UUID userId, UUID trainerId, UUID taskId, String answer) {
        TaskTrainerEntity taskTrainer = getTaskTrainerLink(trainerId, taskId);
        UserEntity user = getUser(userId);
        TaskEntity task = taskService.getTask(taskId);

        TaskConfig config = configFactory.createConfig(task.getConfig(), task.getTaskType());

        validator.validateAttemptsLimit(taskTrainer.getId(), userId, config);
        validator.validateLastAttemptStatus(userId, taskTrainer.getId());

        UserAnswer parsedAnswer = answerParser.parse(answer, task.getTaskType());

        if (task.getTaskType() == TaskType.OPEN_ANSWER) {
            return attemptHandler.handleReviewAttempt(taskTrainer, user, parsedAnswer);
        }

        return attemptHandler.handleAutoCheckAttempt(
                taskTrainer, user, config, parsedAnswer, task.getTaskType()
        );
    }

    private UserEntity getUser(UUID userId) {
        UserEntity user = userRepository.findById(userId);
        if (user == null) {
            throw new EntityNotFoundException("Пользователь не найден");
        }
        return user;
    }

    public TaskTrainerEntity getTaskTrainerLink(UUID trainerId, UUID taskId) {
        return taskTrainerRepository
                .findByTrainerIdAndTaskId(trainerId, taskId)
                .orElseThrow(() -> new EntityNotFoundException("Задача для отправки ответа не найдена"));
    }
}
