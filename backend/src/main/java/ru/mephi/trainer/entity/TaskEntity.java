package ru.mephi.trainer.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import ru.mephi.trainer.entity.enums.TaskType;

import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static java.util.stream.Collectors.joining;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tasks", schema = "public")
public class TaskEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(name = "task_type", columnDefinition = "task_types", nullable = false)
    private TaskType taskType;

    @Column(nullable = false)
    @JdbcTypeCode(SqlTypes.JSON)
    private String config;

    @Column(name = "created_by", nullable = false)
    private UUID createdBy;

    @CreationTimestamp
    @Column(name = "created_at")
    private OffsetDateTime createdAt;

    @Builder.Default
    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<TaskTrainerEntity> trainers = new HashSet<>();

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
        return trainers.stream().map(TaskTrainerEntity::getTrainer).map(TrainerEntity::getId).map(String::valueOf).collect(joining(","));
    }
}
