package ru.mephi.trainer.rest.controller;

import lombok.RequiredArgsConstructor;
import org.jboss.resteasy.reactive.RestResponse;
import ru.mephi.trainer.rest.api.ProfileApi;
import ru.mephi.trainer.rest.dto.response.ProfileResponse;
import ru.mephi.trainer.rest.dto.response.SimulatorProgressResponse;
import ru.mephi.trainer.service.CurrentUserService;
import ru.mephi.trainer.service.ProfileService;
import ru.mephi.trainer.service.SimulatorProgressService;

import java.util.UUID;


@RequiredArgsConstructor
public class ProfileController implements ProfileApi {

    private final CurrentUserService currentUserService;
    private final SimulatorProgressService simulatorProgressService;
    private final ProfileService profileService;

    @Override
    public RestResponse<ProfileResponse> getProfile() {
        UUID userId = currentUserService.getCurrentUserIdOrThrow();
        ProfileResponse response = profileService.getProfile(userId);
        return RestResponse.ok(response);
    }

    @Override
    public RestResponse<SimulatorProgressResponse> getSimulatorProgress(String simulatorId) {
        UUID userId = currentUserService.getCurrentUserIdOrThrow();
        UUID simulatorUuid = UUID.fromString(simulatorId);

        SimulatorProgressResponse response = simulatorProgressService.getSimulatorProgress(userId, simulatorUuid);

        return RestResponse.ok(response);
    }
}