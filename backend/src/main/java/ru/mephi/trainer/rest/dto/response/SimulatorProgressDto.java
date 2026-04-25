package ru.mephi.trainer.rest.dto.response;

import lombok.Data;

@Data
public class SimulatorProgressDto {
    private Long id;
    private String name;
    private Integer progressPercent;
}