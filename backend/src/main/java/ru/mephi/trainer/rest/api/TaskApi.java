package ru.mephi.trainer.rest.api;

import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.security.SecurityRequirement;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.jboss.resteasy.reactive.RestResponse;
import ru.mephi.trainer.rest.dto.request.task.CreateErrorFindingTaskRequest;
import ru.mephi.trainer.rest.dto.request.task.CreateOpenAnswerTaskRequest;
import ru.mephi.trainer.rest.dto.request.task.CreateSingleChoiceTaskRequest;
import ru.mephi.trainer.rest.dto.response.ErrorResponse;
import ru.mephi.trainer.rest.dto.response.task.TaskResponse;

@Path("/tasks")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Задания", description = "Управление заданиями")
public interface TaskApi {

    @POST
    @Path("/single-choice")
    @Operation(
            operationId = "createSingleChoiceTask",
            summary = "Создание задания с одиночным выбором",
            description = "Создать задание с одиночным выбором"
    )
    @SecurityRequirement(name = "bearerAuth")
    @APIResponses(value = {
            @APIResponse(
                    responseCode = "201",
                    description = "Задание успешно создано",
                    content = @Content(schema = @Schema(implementation = TaskResponse.class))
            ),
            @APIResponse(
                    responseCode = "500",
                    description = "Неожиданная ошибка",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    RestResponse<TaskResponse> createSingleChoiceTask(@RequestBody @Valid CreateSingleChoiceTaskRequest createRequest);

    @POST
    @Path("/multiple-choice")
    @Operation(
            operationId = "createMultipleChoiceTask",
            summary = "Создание задания с множественным выбором",
            description = "Создать задание с множественным выбором"
    )
    @SecurityRequirement(name = "bearerAuth")
    @APIResponses(value = {
            @APIResponse(
                    responseCode = "201",
                    description = "Задание успешно создано",
                    content = @Content(schema = @Schema(implementation = TaskResponse.class))
            ),
            @APIResponse(
                    responseCode = "500",
                    description = "Неожиданная ошибка",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    RestResponse<TaskResponse> createMultipleChoiceTask(@RequestBody @Valid CreateSingleChoiceTaskRequest createRequest);

    @POST
    @Path("/error-finding")
    @Operation(
            operationId = "createErrorFindingTask",
            summary = "Создание задания на поиск ошибок",
            description = "Создать задание на поиск ошибок"
    )
    @SecurityRequirement(name = "bearerAuth")
    @APIResponses(value = {
            @APIResponse(
                    responseCode = "201",
                    description = "Задание успешно создано",
                    content = @Content(schema = @Schema(implementation = TaskResponse.class))
            ),
            @APIResponse(
                    responseCode = "500",
                    description = "Неожиданная ошибка",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    RestResponse<TaskResponse> createErrorFindingTask(@RequestBody @Valid CreateErrorFindingTaskRequest createRequest);

    @POST
    @Path("/open-answer")
    @Operation(
            operationId = "createOpenAnswerTask",
            summary = "Создание задания с открытым ответом",
            description = "Создать задание с открытым ответом"
    )
    @SecurityRequirement(name = "bearerAuth")
    @APIResponses(value = {
            @APIResponse(
                    responseCode = "201",
                    description = "Задание успешно создано",
                    content = @Content(schema = @Schema(implementation = TaskResponse.class))
            ),
            @APIResponse(
                    responseCode = "500",
                    description = "Неожиданная ошибка",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    RestResponse<TaskResponse> createOpenAnswerTask(@RequestBody @Valid CreateOpenAnswerTaskRequest createRequest);
}
