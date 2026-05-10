package ru.mephi.trainer.models.tasks;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ErrorFindingModel {
    private String question;
    private String context;
    @JsonProperty("answer_choices")
    private ArrayList<AnswerChoices> answerChoices;
    @JsonProperty("expected_ordinals")
    private ArrayList<Integer> expectedOrdinals;
    private Integer points;
    @JsonProperty("mistake_cost")
    private Integer mistakeCost;
    @JsonProperty("max_attempts")
    private Integer maxAttempts;
}
