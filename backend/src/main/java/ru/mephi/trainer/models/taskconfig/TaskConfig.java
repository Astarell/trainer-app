package ru.mephi.trainer.models.taskconfig;

import ru.mephi.trainer.entity.enums.TaskType;

public interface TaskConfig {
    TaskType getTaskType();
    Integer getPoints();
    Integer getMaxAttempts();
    Integer getMistakeCost();
}
