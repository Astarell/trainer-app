package ru.mephi.trainer.rest.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import ru.mephi.trainer.entity.enums.AttemptStatus;

import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Запрос сохранения задачи для тренажера")
public class TaskSubmitRequest {
    private String userAnswer;
    private Double points;
    private AttemptStatus status;
    private OffsetDateTime createdAt;
    private UUID taskId;
    private UUID user;
}
