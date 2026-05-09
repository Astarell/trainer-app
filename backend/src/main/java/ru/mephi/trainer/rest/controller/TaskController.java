package ru.mephi.trainer.rest.controller;

import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jboss.resteasy.reactive.RestResponse;
import ru.mephi.trainer.rest.api.TaskApi;
import ru.mephi.trainer.rest.dto.request.task.ErrorFindingTaskRequest;
import ru.mephi.trainer.rest.dto.request.task.MultipleChoiceTaskRequest;
import ru.mephi.trainer.rest.dto.request.task.OpenAnswerTaskRequest;
import ru.mephi.trainer.rest.dto.request.task.SingleChoiceTaskRequest;
import ru.mephi.trainer.rest.dto.response.task.TaskResponse;

import java.util.UUID;

@Slf4j
@ApplicationScoped
@RequiredArgsConstructor
public class TaskController implements TaskApi {

    @Override
    public RestResponse<TaskResponse> createSingleChoiceTask(SingleChoiceTaskRequest createRequest) {
        return null;
    }

    @Override
    public RestResponse<TaskResponse> createMultipleChoiceTask(MultipleChoiceTaskRequest createRequest) {
        return null;
    }

    @Override
    public RestResponse<TaskResponse> createErrorFindingTask(ErrorFindingTaskRequest createRequest) {
        return null;
    }

    @Override
    public RestResponse<TaskResponse> createOpenAnswerTask(OpenAnswerTaskRequest createRequest) {
        return null;
    }

    @Override
    public RestResponse<TaskResponse> updateSingleChoiceTask(UUID id, SingleChoiceTaskRequest updateRequest) {
        return null;
    }

    @Override
    public RestResponse<TaskResponse> updateMultipleChoiceTask(UUID id, MultipleChoiceTaskRequest updateRequest) {
        return null;
    }

    @Override
    public RestResponse<TaskResponse> updateErrorFindingTask(UUID id, ErrorFindingTaskRequest updateRequest) {
        return null;
    }

    @Override
    public RestResponse<TaskResponse> updateOpenAnswerTask(UUID id, OpenAnswerTaskRequest updateRequest) {
        return null;
    }
}
