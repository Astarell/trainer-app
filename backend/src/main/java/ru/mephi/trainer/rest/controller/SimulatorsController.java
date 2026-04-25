package ru.mephi.trainer.rest.controller;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.SecurityContext;
import org.jboss.resteasy.reactive.RestResponse;
import ru.mephi.trainer.rest.api.SimulatorsAPI;
import ru.mephi.trainer.rest.dto.response.SimulatorDto;
import ru.mephi.trainer.rest.dto.response.SimulatorInfoResponse;
import ru.mephi.trainer.rest.dto.response.SimulatorsResponse;

import java.util.ArrayList;

@ApplicationScoped
public class SimulatorsController implements SimulatorsAPI {


    @Context
    SecurityContext securityContext;

    @Override
    public RestResponse<SimulatorsResponse> getSimulators() {
        // TODO: временная заглушка
        SimulatorDto simulatorDto = new SimulatorDto();
        simulatorDto.setId((long) 1);
        simulatorDto.setName("SQL для аналитиков");
        ArrayList<SimulatorDto> simulatorDtos = new ArrayList<>();
        simulatorDtos.add(simulatorDto);
        SimulatorsResponse response = new SimulatorsResponse();
        response.setSimulators(simulatorDtos);
        return RestResponse.ok(response);
    }

    @Override
    public RestResponse<SimulatorInfoResponse> getSimulatorInfo(Long simulatorId) {
        // TODO: временная заглушка
        SimulatorInfoResponse response = new SimulatorInfoResponse();
        response.setId(simulatorId);
        response.setName("SQL для аналитиков");
        response.setTotalTasks(4);
        return RestResponse.ok(response);
    }
}