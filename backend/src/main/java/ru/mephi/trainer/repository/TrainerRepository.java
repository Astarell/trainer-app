package ru.mephi.trainer.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import ru.mephi.trainer.entity.TrainerEntity;
import ru.mephi.trainer.exception.TrainerNotFoundException;
import ru.mephi.trainer.rest.dto.response.TrainerInfoResponse;
import ru.mephi.trainer.rest.dto.response.TrainerListResponse;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@ApplicationScoped
public class TrainerRepository implements PanacheRepositoryBase<TrainerEntity, UUID> {

    public List<TrainerListResponse> getAllTrainers() {
        String sql = """
                    SELECT id, name, created_at, created_by
                    FROM trainers
                    ORDER BY created_at
                """;

        List<Object[]> rows = getEntityManager()
                .createNativeQuery(sql)
                .getResultList();

        return rows.stream()
                .map(row -> TrainerListResponse.builder()
                        .id(((UUID) row[0]).toString())
                        .name((String) row[1])
                        .createdAt(row[2].toString())
                        .createdBy(((UUID) row[3]).toString())
                        .build())
                .collect(Collectors.toList());
    }

    public Optional<TrainerInfoResponse> getTrainerInfo(UUID trainerId) {
        String sql = """
                    SELECT 
                        t.id,
                        t.name,
                        COUNT(tt.task_id) as total_tasks,
                        t.created_at
                    FROM trainers t
                    LEFT JOIN tasks_trainers tt ON tt.trainer_id = t.id
                    WHERE t.id = ?1
                    GROUP BY t.id, t.name, t.created_at
                """;

        try {
            Object[] result = (Object[]) getEntityManager()
                    .createNativeQuery(sql)
                    .setParameter(1, trainerId)
                    .getSingleResult();

            TrainerInfoResponse response = new TrainerInfoResponse();
            response.setId(((UUID) result[0]).toString());
            response.setName((String) result[1]);
            response.setTotalTasks(((Number) result[2]).intValue());
            response.setCreatedAt(result[3].toString());

            return Optional.of(response);
        }
        catch (TrainerNotFoundException e) {
            return Optional.empty();
        }
    }
}



