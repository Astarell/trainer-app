package ru.mephi.trainer.rest.dto.response;

import lombok.Data;

@Data
public class SimulatorProgressPercentResponse {
    private String id;
    private String name;
    private Integer progressPercent;
}