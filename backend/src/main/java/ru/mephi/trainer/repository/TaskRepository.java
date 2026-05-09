package ru.mephi.trainer.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import ru.mephi.trainer.entity.TaskEntity;

import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class TaskRepository implements PanacheRepositoryBase<TaskEntity, UUID> {

    /**
     * Находит задание по ID с предварительной загрузкой связей с тренажерами (TaskTrainer).
     * НЕ делает второй join fetch для загрузки самих тренажеров (Trainer).
     */
    public Optional<TaskEntity> findByIdWithTrainersLinks(UUID id) {
        return find("FROM TaskEntity t LEFT JOIN FETCH t.trainers WHERE t.id = ?1", id)
                .firstResultOptional();
    }
}
