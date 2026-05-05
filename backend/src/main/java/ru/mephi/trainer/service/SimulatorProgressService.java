package ru.mephi.trainer.service;

import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.mephi.trainer.repository.SimulatorProgressRepository;
import ru.mephi.trainer.rest.dto.response.CompletedTaskSimulatorPointResponse;
import ru.mephi.trainer.rest.dto.response.ProfileResponse;
import ru.mephi.trainer.rest.dto.response.SimulatorProgressResponse;

import java.util.List;
import java.util.UUID;

@Slf4j
@ApplicationScoped
@RequiredArgsConstructor
public class SimulatorProgressService {
    private final SimulatorProgressRepository simulatorProgressRepository;

    public SimulatorProgressResponse getSimulatorProgress(UUID userId, UUID simulatorId) {
        log.info("User info requested for simulator progress: {}", userId);

        SimulatorProgressResponse response = simulatorProgressRepository.getSimulatorProgress(userId, simulatorId);
        List<CompletedTaskSimulatorPointResponse> tasks = simulatorProgressRepository.getCompletedTaskSimulator(userId, simulatorId);
        response.setTasksInSimulator(tasks);
        return response;
    }
}