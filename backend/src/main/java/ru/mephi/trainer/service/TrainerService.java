package ru.mephi.trainer.service;


import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.mephi.trainer.entity.TrainerEntity;
import ru.mephi.trainer.exception.TrainerNotFoundException;
import ru.mephi.trainer.repository.TrainerRepository;
import ru.mephi.trainer.rest.dto.response.TrainerInfoResponse;
import ru.mephi.trainer.rest.dto.response.TrainerListResponse;

import java.util.List;
import java.util.UUID;

@Slf4j
@ApplicationScoped
@RequiredArgsConstructor
public class TrainerService {
    private final TrainerRepository trainerRepository;

    public List<TrainerListResponse> getAllTrainers() {
        return trainerRepository.getAllTrainers();
    }

    public TrainerInfoResponse getTrainerInfo(UUID id) {
        return trainerRepository.getTrainerInfo(id)
                .orElseThrow(() -> new TrainerNotFoundException("Тренажёр с id " + id + " не найден"));
    }
}
