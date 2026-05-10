package ru.mephi.trainer.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import ru.mephi.trainer.entity.TaskEntity;
import ru.mephi.trainer.rest.dto.response.TaskResponse;

import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class TaskRepository implements PanacheRepositoryBase<TaskEntity, UUID> {

    public Optional<TaskResponse> getTask(UUID taskId) {
        String sql = """
                    SELECT 
                        t.id,
                        t.task_type,
                        t.config,
                        t.created_by,
                        t.created_at
                    FROM tasks t
                    WHERE t.id = ?1
                """;

        Object[] result = (Object[])getEntityManager()
                .createNativeQuery(sql)
                .setParameter(1, taskId)
                .getSingleResultOrNull();

        return Optional.ofNullable(result)
                .map(res -> TaskResponse.builder()
                    .id((UUID) res[0])
                    .taskType((String) res[1])
                    .config((String) res[2])
                    .createdBy((UUID) res[3])
                    .createdAt((OffsetDateTime) res[4])
                    .build());
    }
}
