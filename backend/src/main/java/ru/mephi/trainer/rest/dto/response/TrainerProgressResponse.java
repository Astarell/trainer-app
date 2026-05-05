package ru.mephi.trainer.rest.dto.response;

import lombok.Data;

import java.util.List;

@Data
public class TrainerProgressResponse {
    private String trainerId;
    private String trainerName;
    private Integer earnedScore;      // заработал
    private Integer maxPossibleScore; // максимум можно
    private Integer tasksCompleted;   // решено задач
    private Integer totalTasks;       // всего задач в тренажёре
    private List<CompletedTaskTrainerPointResponse> tasksInTrainer;  // баллы за тренажер
}
