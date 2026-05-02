package ru.mephi.trainer.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import ru.mephi.trainer.entity.enums.AttemptStatus;

import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "task_attempts", schema = "public")
public class TaskAttemptEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "user_answer", nullable = false)
    private String userAnswer;

    @Column(nullable = false)
    private double points;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(name = "status", columnDefinition = "task_attempts_status", nullable = false)
    private AttemptStatus status;

    @CreationTimestamp
    @Column(name = "created_at")
    private OffsetDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "task_id", nullable = false)
    private TaskTrainerEntity task;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @Override
    public String toString() {
        return "TaskAttempt{" +
                "id=" + id +
                ", userAnswer='" + userAnswer + '\'' +
                ", points=" + points +
                ", status=" + status +
                ", createdAt=" + createdAt +
                ", taskId=" + (task == null ? "null" : task.getId()) +
                ", userId=" + (user == null ? "null" : user.getId()) +
                '}';
    }
}
