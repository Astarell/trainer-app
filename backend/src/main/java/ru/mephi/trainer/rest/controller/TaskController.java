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
import ru.mephi.trainer.rest.dto.response.task.admin.TaskAdminResponse;

import java.util.UUID;

@Slf4j
@ApplicationScoped
@RequiredArgsConstructor
public class TaskController implements TaskApi {

    @Override
    public RestResponse<TaskAdminResponse> createSingleChoiceTask(SingleChoiceTaskRequest createRequest) {
        return null;
    }

    @Override
    public RestResponse<TaskAdminResponse> createMultipleChoiceTask(MultipleChoiceTaskRequest createRequest) {
        return null;
    }

    @Override
    public RestResponse<TaskAdminResponse> createErrorFindingTask(ErrorFindingTaskRequest createRequest) {
        return null;
    }

    @Override
    public RestResponse<TaskAdminResponse> createOpenAnswerTask(OpenAnswerTaskRequest createRequest) {
        return null;
    }

    @Override
    public RestResponse<TaskAdminResponse> updateSingleChoiceTask(UUID id, SingleChoiceTaskRequest updateRequest) {
        return null;
    }

    @Override
    public RestResponse<TaskAdminResponse> updateMultipleChoiceTask(UUID id, MultipleChoiceTaskRequest updateRequest) {
        return null;
    }

    @Override
    public RestResponse<TaskAdminResponse> updateErrorFindingTask(UUID id, ErrorFindingTaskRequest updateRequest) {
        return null;
    }

    @Override
    public RestResponse<TaskAdminResponse> updateOpenAnswerTask(UUID id, OpenAnswerTaskRequest updateRequest) {
        return null;
    }
}
