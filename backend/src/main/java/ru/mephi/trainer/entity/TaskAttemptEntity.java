package ru.mephi.trainer.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@Entity
@Builder
@Table(name = "task_attempts")
public class TaskAttemptEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @JdbcTypeCode(SqlTypes.JSON)
    private String user_answer;

    private double points;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(name = "status", columnDefinition = "task_attempts_status")
    private AttemptStatus status;

    @Column(name = "created_at")
    private OffsetDateTime createdAt;

    @ManyToOne
    @JoinTable(
            name = "tasks_trainers",
            joinColumns = {@JoinColumn(name = "id")}
    )
    private TaskEntity task;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @Override
    public String toString() {
        return "TaskAttempt{" +
                "id=" + id +
                ", user_answer='" + user_answer + '\'' +
                ", points=" + points +
                ", status=" + status +
                ", createdAt=" + createdAt +
                ", taskId=" + task.getId() +
                ", userId=" + user.getId() +
                '}';
    }

    public enum AttemptStatus {
        REVIEW, COMPLETED, FAILED
    }
}
