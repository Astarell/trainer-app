package ru.mephi.trainer.rest.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.jboss.resteasy.reactive.RestResponse;
import ru.mephi.trainer.rest.api.ProfileApi;
import ru.mephi.trainer.rest.dto.response.ProfileResponse;
import ru.mephi.trainer.rest.dto.response.SimulatorProgressResponse;
import ru.mephi.trainer.service.CurrentUserService;
import ru.mephi.trainer.service.ProfileService;
import ru.mephi.trainer.service.SimulatorProgressService;

import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
public class ProfileController implements ProfileApi {
    private final JsonWebToken jwt;
    private final CurrentUserService currentUserService;
    private final SimulatorProgressService simulatorProgressService;
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
    public RestResponse<SimulatorProgressResponse> getSimulatorProgress(String simulatorId) {
        log.info("Simulator progress endpoint accessed by: {}", jwt.getName());
        UUID userId = currentUserService.getCurrentUserIdOrThrow();
        UUID simulatorUuid = UUID.fromString(simulatorId);

        SimulatorProgressResponse response = simulatorProgressService.getSimulatorProgress(userId, simulatorUuid);
        log.info("Simulator progress in successfully:  {}", jwt.getName());

        return RestResponse.ok(response);
    }
}