package ru.mephi.trainer.rest.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.jboss.resteasy.reactive.RestResponse;
import ru.mephi.trainer.rest.api.ProfileApi;
import ru.mephi.trainer.rest.dto.response.ProfileResponse;
import ru.mephi.trainer.rest.dto.response.TrainerProgressResponse;
import ru.mephi.trainer.service.CurrentUserService;
import ru.mephi.trainer.service.ProfileService;
import ru.mephi.trainer.service.TrainerProgressService;

import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
public class ProfileController implements ProfileApi {
    private final JsonWebToken jwt;
    private final CurrentUserService currentUserService;
    private final TrainerProgressService trainerProgressService;
    private final ProfileService profileService;

    @Override
    public RestResponse<ProfileResponse> getProfile() {
        log.info("Profile endpoint accessed by: {}", jwt.getName());

        UUID userId = currentUserService.getCurrentUserIdOrThrow();
        ProfileResponse response = profileService.getProfile(userId);
        log.info("Profile endpoint in successfully: {}", jwt.getName());

        return RestResponse.ok(response);
    }

    @Override
    public RestResponse<TrainerProgressResponse> getTrainerProgress(String trainerId) {
        log.info("Trainer progress endpoint accessed by: {}", jwt.getName());
        UUID userId = currentUserService.getCurrentUserIdOrThrow();
        UUID trainerUuid = UUID.fromString(trainerId);

        TrainerProgressResponse response = trainerProgressService.getTrainerProgress(userId, trainerUuid);
        log.info("Trainer progress in successfully:  {}", jwt.getName());

        return RestResponse.ok(response);
    }
}