package ru.mephi.trainer.rest.controller;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.SecurityContext;
import org.jboss.resteasy.reactive.RestResponse;
import ru.mephi.trainer.rest.api.ProfileApi;
import ru.mephi.trainer.rest.dto.response.ProfileResponse;
import ru.mephi.trainer.rest.dto.response.SimulatorProgressResponse;

@ApplicationScoped
public class ProfileController implements ProfileApi {


    @Context
    SecurityContext securityContext;

    @Override
    public RestResponse<ProfileResponse> getProfile() {
        // TODO: временная заглушка
        ProfileResponse response = new ProfileResponse();
        response.setEmail("test@example.com");
        response.setFirstName("Иван");
        response.setLastName("Иванович");
        response.setTotalScore(125);
        response.setCreatedAt("2024-01-15T10:00:00");
        return RestResponse.ok(response);
    }

    @Override
    public RestResponse<SimulatorProgressResponse> getSimulatorProgress(Long simulatorId) {
        // TODO: временная заглушка
        SimulatorProgressResponse response = new SimulatorProgressResponse();
        response.setSimulatorId(simulatorId);
        response.setSimulatorName("SQL для аналитиков");
        response.setEarnedScore(45);
        response.setMaxPossibleScore(60);
        response.setTasksCompleted(3);
        response.setTotalTasks(4);
        return RestResponse.ok(response);
    }
}