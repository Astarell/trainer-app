package ru.mephi.trainer.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.OffsetDateTime;
import java.util.Set;
import java.util.UUID;

import static java.util.stream.Collectors.joining;

@Data
@Entity
@Builder
@Table(name = "tasks")
public class TaskEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(name = "task_type", columnDefinition = "task_types")
    private TaskType taskType;

    @JdbcTypeCode(SqlTypes.JSON)
    private String config;

    @Column(name = "created_by")
    private UUID createdBy;

    @Column(name = "created_at")
    private OffsetDateTime createdAt;

    @ManyToMany(mappedBy = "tasks", cascade = CascadeType.REMOVE)
    private Set<TrainerEntity> trainers;

    @Override
    public String toString() {
        return "TaskEntity{" +
                "id=" + id +
                ", taskType=" + taskType +
                ", config='" + config + '\'' +
                ", createdBy=" + createdBy +
                ", createdAt=" + createdAt +
                ", trainerIds=" + getTrainerIds() +
                '}';
    }

    private String getTrainerIds() {
        return trainers.stream().map(TrainerEntity::getId).map(String::valueOf).collect(joining(","));
    }

    public enum TaskType {
        SINGLE_CHOICE, MULTIPLE_CHOICE, ERROR_FINDING, OPEN_ANSWER, SQL_QUERY
    }
}
