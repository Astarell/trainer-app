package ru.mephi.trainer.models.tasks;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AnswerChoices {
    private Integer ordinal;
    private String choice;
    private Boolean checked;
}
