package ru.mephi.trainer.rest.dto.response;

import lombok.Data;

import java.util.List;

@Data
public class SimulatorProgressResponse {
    private Long simulatorId;
    private String simulatorName;
    private Integer earnedScore;      // заработал
    private Integer maxPossibleScore; // максимум можно
    private Integer tasksCompleted;   // решено задач
    private Integer totalTasks;       // всего задач в тренажёре
    private List<CompletedTaskSimulatorPointDto> tasksInSimulator;  // баллы за тренажер
}
