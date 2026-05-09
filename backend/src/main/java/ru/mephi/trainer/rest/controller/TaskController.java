package ru.mephi.trainer.rest.controller;

import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jboss.resteasy.reactive.RestResponse;
import ru.mephi.trainer.mapper.TaskMapper;
import ru.mephi.trainer.models.command.SaveTaskCommand;
import ru.mephi.trainer.rest.api.TaskApi;
import ru.mephi.trainer.rest.dto.request.task.ErrorFindingTaskRequest;
import ru.mephi.trainer.rest.dto.request.task.MultipleChoiceTaskRequest;
import ru.mephi.trainer.rest.dto.request.task.OpenAnswerTaskRequest;
import ru.mephi.trainer.rest.dto.request.task.SingleChoiceTaskRequest;
import ru.mephi.trainer.rest.dto.response.task.admin.TaskAdminResponse;
import ru.mephi.trainer.service.CurrentUserService;
import ru.mephi.trainer.service.TaskService;

import java.util.UUID;

@Slf4j
@ApplicationScoped
@RequiredArgsConstructor
public class TaskController implements TaskApi {

    private final TaskService taskService;
    private final CurrentUserService currentUserService;
    private final TaskMapper taskMapper;

    @Override
    @RolesAllowed({"expert", "admin"})
    public RestResponse<TaskAdminResponse> createSingleChoiceTask(SingleChoiceTaskRequest request) {
        log.info("Creating SINGLE_CHOICE task");

        SaveTaskCommand command = taskMapper.toCommand(request);
        UUID currentUserId = currentUserService.getCurrentUserIdOrThrow();

        taskService.createTask(command, currentUserId);

        return null;
    }

    @Override
    @RolesAllowed({"expert", "admin"})
    public RestResponse<TaskAdminResponse> createMultipleChoiceTask(MultipleChoiceTaskRequest request) {
        log.info("Creating MULTIPLE_CHOICE task");

        SaveTaskCommand command = taskMapper.toCommand(request);
        UUID currentUserId = currentUserService.getCurrentUserIdOrThrow();

        taskService.createTask(command, currentUserId);

        return null;
    }

    @Override
    @RolesAllowed({"expert", "admin"})
    public RestResponse<TaskAdminResponse> createErrorFindingTask(ErrorFindingTaskRequest request) {
        log.info("Creating ERROR_FINDING task");

        SaveTaskCommand command = taskMapper.toCommand(request);
        UUID currentUserId = currentUserService.getCurrentUserIdOrThrow();

        taskService.createTask(command, currentUserId);

        return null;
    }

    @Override
    @RolesAllowed({"expert", "admin"})
    public RestResponse<TaskAdminResponse> createOpenAnswerTask(OpenAnswerTaskRequest request) {
        log.info("Creating OPEN_ANSWER task");

        SaveTaskCommand command = taskMapper.toCommand(request);
        UUID currentUserId = currentUserService.getCurrentUserIdOrThrow();

        taskService.createTask(command, currentUserId);

        return null;
    }

    @Override
    @RolesAllowed({"expert", "admin"})
    public RestResponse<TaskAdminResponse> updateSingleChoiceTask(UUID id, SingleChoiceTaskRequest request) {
        log.info("Updating SINGLE_CHOICE task: id={}", id);

        SaveTaskCommand command = taskMapper.toCommand(request);
        taskService.updateTask(id, command);

        return null;
    }

    @Override
    @RolesAllowed({"expert", "admin"})
    public RestResponse<TaskAdminResponse> updateMultipleChoiceTask(UUID id, MultipleChoiceTaskRequest request) {
        log.info("Updating MULTIPLE_CHOICE task: id={}", id);

        SaveTaskCommand command = taskMapper.toCommand(request);
        taskService.updateTask(id, command);

        return null;
    }

    @Override
    @RolesAllowed({"expert", "admin"})
    public RestResponse<TaskAdminResponse> updateErrorFindingTask(UUID id, ErrorFindingTaskRequest request) {
        log.info("Updating ERROR_FINDING task: id={}", id);

        SaveTaskCommand command = taskMapper.toCommand(request);
        taskService.updateTask(id, command);

        return null;
    }

    @Override
    @RolesAllowed({"expert", "admin"})
    public RestResponse<TaskAdminResponse> updateOpenAnswerTask(UUID id, OpenAnswerTaskRequest request) {
        log.info("Updating OPEN_ANSWER task: id={}", id);

        SaveTaskCommand command = taskMapper.toCommand(request);
        taskService.updateTask(id, command);

        return null;
    }
}
