package ru.mephi.trainer.rest.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TrainerListResponse {
    private String id;
    private String name;
    private String createdAt;
    private String createdBy;
}