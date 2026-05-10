package ru.mephi.trainer.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import ru.mephi.trainer.entity.TaskTrainerEntity;
import ru.mephi.trainer.rest.dto.response.TaskResponse;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@ApplicationScoped
public class TaskTrainerRepository implements PanacheRepositoryBase<TaskTrainerEntity, UUID> {

    public long deleteAllByTaskId(UUID taskId){
        return delete("DELETE FROM TaskTrainerEntity WHERE task.id = ?1", taskId);
    }

    public long deleteAllByTrainerId(UUID trainerId){
        return delete("DELETE FROM TaskTrainerEntity WHERE trainer.id = ?1", trainerId);
    }

    public List<TaskResponse> getTrainerTasks(UUID trainerId) {
        String sql = """
                    SELECT 
                        t.id,
                        t.task_type,
                        t.config,
                        t.created_by,
                        t.created_at
                    FROM tasks t
                    JOIN tasks_trainers tt 
                        ON tt.trainer_id = t.id
                        WHERE tt.trainer_id = ?1
                """;

        List<Object[]> results = getEntityManager()
                .createNativeQuery(sql)
                .setParameter(1, trainerId)
                .getResultList();

        return results.stream()
                .map(row -> TaskResponse.builder()
                        .id((UUID) row[0])
                        .taskType((String) row[1])
                        .config((String) row[2])
                        .createdBy((UUID) row[3])
                        .createdAt((OffsetDateTime) row[4])
                        .build())
                .collect(Collectors.toList());
    }

    public Optional<TaskTrainerEntity> findByTaskAndTrainer (UUID trainerId, UUID taskId) {
        return find("SELECT * FROM TaskTrainerEntity WHERE trainerId = ?1 AND taskId = ?2)",
                trainerId.toString(), taskId.toString())
                .firstResultOptional();
    }
}
