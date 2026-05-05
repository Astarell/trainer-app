package ru.mephi.trainer.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import ru.mephi.trainer.entity.UserEntity;
import ru.mephi.trainer.rest.dto.response.TrainerProgressPercentResponse;

import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class ProfileRepository implements PanacheRepositoryBase<UserEntity, UUID> {

    public UserEntity getUserData(UUID userId) {
        return find("id", userId).firstResult();
    }

    public Integer getUserTotalScore(UUID userId) {
        String sql = """
            SELECT COALESCE(SUM(ta.points), 0)
            FROM task_attempts ta
            WHERE ta.user_id = ?1 AND ta.status = 'COMPLETED'
        """;
        Object result = getEntityManager()
                .createNativeQuery(sql)
                .setParameter(1, userId.toString())
                .getSingleResult();

        return ((Number) result).intValue();
    }

    public List<TrainerProgressPercentResponse> getUserTrainersProgress(UUID userId) {
        String sql = """
            SELECT 
                t.id,
                t.name,
                COALESCE(
                    (SUM(ta.points) * 100) / NULLIF(SUM(task.max_score), 0), 
                    0
                ) as progressPercent
            FROM trainers t
            LEFT JOIN tasks_trainers tt ON tt.trainer_id = t.id
            LEFT JOIN tasks task ON task.id = tt.task_id
            LEFT JOIN task_attempts ta ON ta.task_id = task.id AND ta.user_id = ?1 AND ta.status = 'COMPLETED'
            GROUP BY t.id, t.name
        """;

        return getEntityManager()
                .createNativeQuery(sql, "TrainerProgressMapping")
                .setParameter(1, userId.toString())
                .getResultList();
    }
}
