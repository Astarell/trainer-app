package ru.mephi.trainer.rest.controller;

import jakarta.enterprise.context.ApplicationScoped;

import lombok.RequiredArgsConstructor;
import org.jboss.resteasy.reactive.RestResponse;
import ru.mephi.trainer.entity.TrainerEntity;
import ru.mephi.trainer.rest.api.TrainersAPI;
import ru.mephi.trainer.rest.dto.response.TrainerInfoResponse;
import ru.mephi.trainer.rest.dto.response.TrainerListResponse;
import ru.mephi.trainer.service.TrainerService;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@ApplicationScoped
public class TrainersController implements TrainersAPI {

    private final TrainerService trainerService;

    @Override
    public RestResponse<List<TrainerListResponse>> getTrainers() {
        List<TrainerListResponse> response = trainerService.getAllTrainers();
        return RestResponse.ok(response);
    }

    @Override
    public RestResponse<TrainerInfoResponse> getTrainerInfo(String trainerId) {
        TrainerInfoResponse response = trainerService.getTrainerInfo(UUID.fromString(trainerId));
        return RestResponse.ok(response);
    }
}