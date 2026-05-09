package ru.mephi.trainer.rest.controller;

import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jboss.resteasy.reactive.RestResponse;
import ru.mephi.trainer.rest.api.TaskApi;
import ru.mephi.trainer.rest.dto.request.task.CreateErrorFindingTaskRequest;
import ru.mephi.trainer.rest.dto.request.task.CreateOpenAnswerTaskRequest;
import ru.mephi.trainer.rest.dto.request.task.CreateSingleChoiceTaskRequest;
import ru.mephi.trainer.rest.dto.response.task.TaskResponse;
import ru.mephi.trainer.service.TaskService;

@Slf4j
@ApplicationScoped
@RequiredArgsConstructor
public class TaskController implements TaskApi {

    private final TaskService taskService;

    @Override
    public RestResponse<TaskResponse> createSingleChoiceTask(CreateSingleChoiceTaskRequest createRequest) {
        return null;
    }

    @Override
    public RestResponse<TaskResponse> createMultipleChoiceTask(CreateSingleChoiceTaskRequest createRequest) {
        return null;
    }

    @Override
    public RestResponse<TaskResponse> createErrorFindingTask(CreateErrorFindingTaskRequest createRequest) {
        return null;
    }

    @Override
    public RestResponse<TaskResponse> createOpenAnswerTask(CreateOpenAnswerTaskRequest createRequest) {
        return null;
    }
}
