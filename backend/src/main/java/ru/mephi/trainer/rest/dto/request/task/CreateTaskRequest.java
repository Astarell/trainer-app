package ru.mephi.trainer.rest.dto.request.task;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.util.Set;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract class CreateTaskRequest {

    @Schema(description = "Список ID тренажёров, к которым привязать задание",
            examples = "[\"29f18957-fffd-4d83-b738-9f6e0496bf3d\", \"8088c009-41c7-4a79-99e0-7d91705cf967\"]"
    )
    private Set<UUID> trainerIds;
}
