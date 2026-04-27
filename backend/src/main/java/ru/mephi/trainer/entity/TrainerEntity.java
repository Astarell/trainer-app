package ru.mephi.trainer.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;

import java.time.OffsetDateTime;
import java.util.Set;
import java.util.UUID;

import static java.util.stream.Collectors.joining;

@Data
@Entity
@Builder
@Table(name = "trainers")
public class TrainerEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String name;

    @Column(name = "created_by")
    private UUID createdBy;

    @Column(name = "created_at")
    private OffsetDateTime createdAt;

    @ManyToMany(cascade = CascadeType.REMOVE)
    @JoinTable(
            name = "tasks_trainers",
            joinColumns = {@JoinColumn(name = "trainer_id")},
            inverseJoinColumns = {@JoinColumn(name = "task_id")}
    )
    private Set<TaskEntity> tasks;

    @Override
    public String toString() {
        return "TrainerEntity{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", createdBy=" + createdBy +
                ", createdAt=" + createdAt +
                ", tasks=" + getTasksIds() +
                '}';
    }

    private String getTasksIds() {
        return tasks.stream().map(TaskEntity::getId).map(String::valueOf).collect(joining(","));
    }
}
