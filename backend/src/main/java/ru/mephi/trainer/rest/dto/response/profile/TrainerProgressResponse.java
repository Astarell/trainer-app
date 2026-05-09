package ru.mephi.trainer.rest.dto.response.profile;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TrainerProgressResponse {
    private UUID trainerId;
    private String trainerName;
    private Double earnedScore;
    private Double maxPossibleScore;
    private Integer tasksCompleted;
    private Integer totalTasks;
    private List<CompletedTaskTrainerPointResponse> tasksInTrainer;
}
