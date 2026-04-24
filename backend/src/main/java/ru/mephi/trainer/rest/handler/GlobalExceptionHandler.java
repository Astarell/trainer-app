package ru.mephi.trainer.rest.handler;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;
import lombok.extern.slf4j.Slf4j;
import org.jboss.resteasy.reactive.RestResponse;
import org.jboss.resteasy.reactive.server.ServerExceptionMapper;
import ru.mephi.trainer.rest.dto.response.ErrorResponse;

@Slf4j
@Provider
public class GlobalExceptionHandler {

    @ServerExceptionMapper
    public RestResponse<ErrorResponse> handleException(Exception e) {
        log.error("Unexpected exception: ", e);

        ErrorResponse response = ErrorResponse.builder()
                .message("Упс! Что-то пошло не так")
                .build();

        return RestResponse.status(Response.Status.INTERNAL_SERVER_ERROR, response);
    }
}
