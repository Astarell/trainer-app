package ru.mephi.trainer.rest.controller;

import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jboss.resteasy.reactive.RestResponse;
import ru.mephi.trainer.rest.api.TrainersAPI;
import ru.mephi.trainer.rest.dto.request.trainer.CreateTrainerRequest;
import ru.mephi.trainer.rest.dto.response.trainer.TrainerInfoResponse;
import ru.mephi.trainer.rest.dto.response.trainer.TrainerResponse;
import ru.mephi.trainer.service.TrainerService;

import java.util.List;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@ApplicationScoped
public class TrainersController implements TrainersAPI {

    private final TrainerService trainerService;

    @Override
    @PermitAll
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
    @PermitAll
    public RestResponse<TrainerInfoResponse> getTrainerInfo(UUID trainerId) {
        log.info("Get trainer: id={}", trainerId);
        TrainerInfoResponse trainerInfoResponse = trainerService.getTrainerInfo(trainerId);
        return RestResponse.ok(trainerInfoResponse);
    }

    @Override
    @RolesAllowed({"expert", "admin"})
    public RestResponse<TrainerInfoResponse> createTrainer(CreateTrainerRequest createTrainerRequest) {
        return null;
    }
}
