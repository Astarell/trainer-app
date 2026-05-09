package ru.mephi.trainer.rest.controller;

import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jboss.resteasy.reactive.RestResponse;
import ru.mephi.trainer.rest.api.ExpertApi;
import ru.mephi.trainer.rest.dto.request.SetPointRequest;
import ru.mephi.trainer.rest.dto.response.AnswerTaskResponse;
import ru.mephi.trainer.rest.dto.response.ReviewTaskResponse;
import ru.mephi.trainer.rest.dto.response.test.MessageResponse;
import ru.mephi.trainer.service.ExpertService;

import java.util.List;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@ApplicationScoped
public class ExpertController implements ExpertApi {
    private final ExpertService expertService;

    @Override
    @RolesAllowed({"expert"})
    public RestResponse<List<ReviewTaskResponse>> getListExamination() {
        log.info("Expert endpoint list examination accessed");
        List<ReviewTaskResponse> response = expertService.getReviewTask();
        log.info("Expert endpoint list examination in successfully");
        return RestResponse.ok(response);
    }

    @Override
    @RolesAllowed({"expert"})
    public RestResponse<AnswerTaskResponse> getTaskForCheck(UUID taskAttemptId) {
        log.info("Expert endpoint get task for check accessed: id={}", taskAttemptId);
        AnswerTaskResponse response = expertService.getTaskForCheck(taskAttemptId);
        log.info("Expert endpoint get task for check in successfully: id={}", taskAttemptId);
        return RestResponse.ok(response);
    }

    @Override
    @RolesAllowed({"expert"})
    public RestResponse<MessageResponse> setPointForTask(UUID taskAttemptId, SetPointRequest request) {
        log.info("Expert endpoint set task point accessed: id={}, point={}", taskAttemptId, request.getPoints());
        MessageResponse response = expertService.setPointsForTask(taskAttemptId, request.getPoints());
        log.info("Expert endpoint set task point in successfully: id={}, point={}", taskAttemptId, request.getPoints());
        return RestResponse.ok(response);
    }


}