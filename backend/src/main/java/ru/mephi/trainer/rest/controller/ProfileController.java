package ru.mephi.trainer.rest.controller;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.SecurityContext;
import org.jboss.resteasy.reactive.RestResponse;
import ru.mephi.trainer.rest.api.ProfileApi;
import ru.mephi.trainer.rest.dto.response.ProfileResponse;
import ru.mephi.trainer.rest.dto.response.SimulatorProgressResponse;

@ApplicationScoped
public class ProfileController implements ProfileApi {


    @Context
    SecurityContext securityContext;

    @Override
    public RestResponse<ProfileResponse> getProfile() {

        /*
        UUID userId = getCurrentUserId();

        SELECT u.id, u.first_name, u.last_name, u.email, u.created_at,
            COALESCE(SUM(ta.points), 0) as total_score
        FROM users u
        LEFT JOIN task_attempts ta ON ta.user_id = u.id AND ta.status = 'COMPLETED'
        WHERE u.id = :userId
        GROUP BY u.id, u.first_name, u.last_name, u.email, u.created_at

        SELECT 
            t.id,
            t.name,
            COALESCE(
                (SUM(ta.points) * 100) / NULLIF(SUM(task.max_score), 0), 
                0
            ) as progress_percent
        FROM trainers t
        LEFT JOIN tasks_trainers tt ON tt.trainer_id = t.id
        LEFT JOIN tasks task ON task.id = tt.task_id
        LEFT JOIN task_attempts ta ON ta.task_id = task.id AND ta.user_id = :userId AND ta.status = 'COMPLETED'
        GROUP BY t.id, t.name

         */
        // TODO: временная заглушка
        ProfileResponse response = new ProfileResponse();
        response.setId("91efc660-a126-4bc3-845a-4c12ac5b755c");
        response.setEmail("test@example.com");
        response.setFirstName("Иван");
        response.setLastName("Иванович");
        response.setTotalScore(125);
        response.setCreatedAt("2024-01-15T10:00:00");
        return RestResponse.ok(response);
    }

    @Override
    public RestResponse<SimulatorProgressResponse> getSimulatorProgress(String simulatorId) {
        // TODO: временная заглушка


        /*

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
        LEFT JOIN task_attempts ta ON ta.task_id = task.id AND ta.user_id = userId AND ta.status = 'COMPLETED'
        WHERE t.id = simulatorId
        GROUP BY t.id, t.name


        SELECT 
            t.id,
            t.config->>'title' as name,
            COALESCE(ta.points, 0) as point
        FROM tasks t
        JOIN tasks_trainers tt ON tt.task_id = t.id
        LEFT JOIN task_attempts ta ON ta.task_id = t.id AND ta.user_id = userId AND ta.status = 'COMPLETED'
        WHERE tt.trainer_id = simulatorId
        ORDER BY t.created_at
        */
        SimulatorProgressResponse response = new SimulatorProgressResponse();
        response.setSimulatorId(simulatorId);
        response.setSimulatorName("SQL для аналитиков");
        response.setEarnedScore(45);
        response.setMaxPossibleScore(60);
        response.setTasksCompleted(3);
        response.setTotalTasks(4);
        return RestResponse.ok(response);
    }
}