package ru.mephi.trainer.rest.dto.response;

import lombok.Data;

import java.util.List;

@Data
public class SimulatorsResponse {
    private List<SimulatorDto> simulators;
}
