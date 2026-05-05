package ru.mephi.trainer.rest.dto.response;

import lombok.Data;

@Data
public class CompletedTaskTrainerPointDto {
    private String id;
    private String name;
    private Integer point;
}