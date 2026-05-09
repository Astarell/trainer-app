package ru.mephi.trainer.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.NoResultException;
import ru.mephi.trainer.entity.TaskEntity;
import ru.mephi.trainer.entity.enums.AttemptStatus;
import ru.mephi.trainer.entity.enums.TaskType;
import ru.mephi.trainer.rest.dto.response.TaskResponse;

import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class TaskRepository implements PanacheRepositoryBase<TaskEntity, UUID> {

    public Optional<TaskResponse> getTaskWithUserAttempt(UUID userId, UUID trainerId, UUID taskId) {
        String sql = """
                SELECT
                    t.id,
                    t.task_type,
                    t.config->>'question' as question,
                    t.config->>'answer_choices' as answer_choices,
                    CAST(t.config->>'points' AS INTEGER) as points,
                    CAST(t.config->>'mistake_cost' AS INTEGER) as mistake_cost,
                    CAST(t.config->>'max_attempts' AS INTEGER) as max_attempts,
                    ta.points as user_points,
                    ta.status as attempt_status
                FROM tasks t
                JOIN tasks_trainers tt ON tt.task_id = t.id
                LEFT JOIN task_attempts ta ON ta.task_id = tt.id AND ta.user_id = ?1
                WHERE tt.trainer_id = ?2 AND t.id = ?3
                ORDER BY ta.created_at DESC
                LIMIT 1
                """;
        try {
            Object[] result = (Object[]) getEntityManager()
                    .createNativeQuery(sql)
                    .setParameter(1, userId)
                    .setParameter(2, trainerId)
                    .setParameter(3, taskId)
                    .getSingleResult();

            TaskResponse response = new TaskResponse();
            response.setId((UUID) result[0]);
            response.setTaskType(TaskType.valueOf((String) result[1]));
            response.setQuestion((String) result[2]);
            response.setAnswerChoices((String) result[3]);
            response.setPoints(((Number) result[4]).intValue());
            response.setMistakeCost(((Number) result[5]).intValue());
            response.setMaxAttempts(((Number) result[6]).intValue());

            if (result[7] != null) {
                response.setUserPoints(((Number) result[7]).intValue());
            }
            if (result[8] != null) {
                response.setAttemptStatus(AttemptStatus.valueOf((String) result[8]));
            }

            return Optional.of(response);
        }
        catch (NoResultException e) {
            return Optional.empty();
        }
    }

}
