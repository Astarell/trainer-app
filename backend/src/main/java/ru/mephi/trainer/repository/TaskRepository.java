package ru.mephi.trainer.repository;

import jakarta.data.repository.CrudRepository;
import jakarta.data.repository.Repository;
import ru.mephi.trainer.entity.TaskEntity;

import java.util.UUID;

@Repository
public interface TaskRepository extends CrudRepository<TaskEntity, UUID> {
}
