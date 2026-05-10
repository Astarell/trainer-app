package ru.mephi.trainer.rest.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Ответ сохранения задачи для тренажера")
public class TaskSubmitResponse {
    private String userAnswer;
    private Double points;
    private UUID taskId;
    private UUID user;
}
