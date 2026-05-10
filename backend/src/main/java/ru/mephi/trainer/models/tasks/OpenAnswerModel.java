package ru.mephi.trainer.models.tasks;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OpenAnswerModel {
    private String question;
    private String context;
    private String answer;
    private Integer points;
    @JsonProperty("mistake_cost")
    private Integer mistakeCost;
    @JsonProperty("max_attempts")
    private Integer maxAttempts;
}
