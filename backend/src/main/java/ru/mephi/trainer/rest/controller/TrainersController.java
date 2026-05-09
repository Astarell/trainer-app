package ru.mephi.trainer.rest.controller;

import io.quarkus.security.Authenticated;
import jakarta.enterprise.context.ApplicationScoped;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jboss.resteasy.reactive.RestResponse;
import ru.mephi.trainer.rest.api.TrainersAPI;
import ru.mephi.trainer.rest.dto.request.AnswerRequest;
import ru.mephi.trainer.rest.dto.response.TrainerInfoResponse;
import ru.mephi.trainer.rest.dto.response.TrainerResponse;
import ru.mephi.trainer.rest.dto.response.test.MessageResponse;
import ru.mephi.trainer.service.CurrentUserService;
import ru.mephi.trainer.service.TaskAttemptService;
import ru.mephi.trainer.service.TrainerService;

import java.util.List;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@ApplicationScoped
public class TrainersController implements TrainersAPI {

    private final TrainerService trainerService;
    private final TaskAttemptService taskAttemptService;
    private final CurrentUserService currentUserService;

    @Override
    public RestResponse<List<TrainerResponse>> getTrainers() {
        log.info("Get all trainer");
        List<TrainerResponse> response = trainerService.getAllTrainers().stream()
                .map(t -> TrainerResponse.builder()
                        .id(t.getId())
                        .name(t.getName())
                        .createdAt(t.getCreatedAt())
                        .createdBy(t.getCreatedBy())
                        .build())
                .toList();
        return RestResponse.ok(response);
    }

    @Override
    public RestResponse<TrainerInfoResponse> getTrainerInfo(UUID trainerId) {
        log.info("Get trainer: id={}", trainerId);
        TrainerInfoResponse trainerInfoResponse = trainerService.getTrainerInfo(trainerId);
        return RestResponse.ok(trainerInfoResponse);
    }

    @Override
    @Authenticated
    public RestResponse<MessageResponse> insertTaskAttempt(UUID trainerId, UUID taskId, AnswerRequest request) {
        UUID userId = currentUserService.getCurrentUserIdOrThrow();
        log.info("Insert task attempt: userId={}, trainerId={}, taskId={}", userId, trainerId, taskId);
        MessageResponse response = taskAttemptService.insertTaskAttempt(userId, trainerId, taskId,
                request.getUserAnswer());
        return RestResponse.ok(response);
    }
}