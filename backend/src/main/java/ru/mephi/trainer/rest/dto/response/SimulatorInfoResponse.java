package ru.mephi.trainer.rest.dto.response;

import lombok.Data;

@Data
public class SimulatorInfoResponse {
    private String id;
    private String name;
    private Integer totalTasks;   // количество заданий
    private String createdAt;
}
