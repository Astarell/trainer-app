package ru.mephi.trainer.service;

import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;
import ru.mephi.trainer.entity.User;
import ru.mephi.trainer.repository.ProfileRepository;
import ru.mephi.trainer.rest.dto.response.ProfileResponse;
import ru.mephi.trainer.rest.dto.response.SimulatorProgressPercentResponse;

import java.util.List;
import java.util.UUID;


@ApplicationScoped
@RequiredArgsConstructor
public class ProfileService {
    private final ProfileRepository profileRepository;

    public ProfileResponse getProfile(UUID userId) {
        User user = profileRepository.getUserData(userId);
        List<SimulatorProgressPercentResponse> progress = profileRepository.getUserSimulatorsProgress(userId);
        Integer score = profileRepository.getUserTotalScore(userId);

        var response = new ProfileResponse();
        response.setId(user.getId().toString());
        response.setEmail(user.getEmail());
        response.setFirstName(user.getFirstName());
        response.setLastName(user.getLastName());
        response.setCreatedAt(user.getCreatedAt().toString());
        response.setTotalScore(score);
        response.setSimulatorProgressPercent(progress);

        return response;
    }
}