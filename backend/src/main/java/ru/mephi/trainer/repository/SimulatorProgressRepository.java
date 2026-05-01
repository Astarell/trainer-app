package ru.mephi.trainer.repository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import ru.mephi.trainer.rest.dto.response.CompletedTaskSimulatorPointResponse;
import ru.mephi.trainer.rest.dto.response.SimulatorProgressResponse;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@ApplicationScoped
public class SimulatorProgressRepository {

    @PersistenceContext
    EntityManager em;

    public SimulatorProgressResponse getSimulatorProgress(UUID userId, UUID simulatorId) {
        String sql = """
            SELECT 
                t.id as simulator_id,
                t.name as simulator_name,
                COALESCE(SUM(ta.points), 0) as earned_score,
                COALESCE(SUM(task.max_score), 0) as max_possible_score,
                COUNT(DISTINCT ta.id) as tasks_completed,
                COUNT(DISTINCT task.id) as total_tasks
            FROM trainers t
            LEFT JOIN tasks_trainers tt ON tt.trainer_id = t.id
            LEFT JOIN tasks task ON task.id = tt.task_id
            LEFT JOIN task_attempts ta ON ta.task_id = task.id AND ta.user_id = ?1 AND ta.status = 'COMPLETED'
            WHERE t.id = ?2
            GROUP BY t.id, t.name
        """;

        Object[] result = (Object[]) em.createNativeQuery(sql)
                .setParameter(1, userId)
                .setParameter(2, simulatorId)
                .getSingleResult();

        SimulatorProgressResponse response = new SimulatorProgressResponse();
        response.setSimulatorId(((UUID) result[0]).toString());
        response.setSimulatorName((String) result[1]);
        response.setEarnedScore(((Number) result[2]).intValue());
        response.setMaxPossibleScore(((Number) result[3]).intValue());
        response.setTasksCompleted(((Number) result[4]).intValue());
        response.setTotalTasks(((Number) result[5]).intValue());

        return response;
    }


    public List<CompletedTaskSimulatorPointResponse> getCompletedTaskSimulator(UUID userId, UUID simulatorId) {
        String sql = """
            SELECT
                t.id,
                t.config->>'title' as name,
                COALESCE(ta.points, 0) as point
            FROM tasks t
            JOIN tasks_trainers tt ON tt.task_id = t.id
            LEFT JOIN task_attempts ta ON ta.task_id = t.id AND ta.user_id = ?1 AND ta.status = 'COMPLETED'
            WHERE tt.trainer_id = ?2
            ORDER BY t.created_at
        """;

        List<Object[]> rows = em.createNativeQuery(sql)
                .setParameter(1, userId.toString())
                .setParameter(2, simulatorId)
                .getResultList();

        return rows.stream()
                .map(row -> {
                    CompletedTaskSimulatorPointResponse dto = new CompletedTaskSimulatorPointResponse();
                    dto.setId(((UUID) row[0]).toString());
                    dto.setName((String) row[1]);
                    dto.setPoint(((Number) row[2]).intValue());
                    return dto;
                })
                .collect(Collectors.toList());
    }
}