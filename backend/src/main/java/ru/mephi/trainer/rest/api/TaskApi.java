package ru.mephi.trainer.rest.api;

import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
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
import ru.mephi.trainer.rest.dto.request.task.ErrorFindingTaskRequest;
import ru.mephi.trainer.rest.dto.request.task.MultipleChoiceTaskRequest;
import ru.mephi.trainer.rest.dto.request.task.OpenAnswerTaskRequest;
import ru.mephi.trainer.rest.dto.request.task.SingleChoiceTaskRequest;
import ru.mephi.trainer.rest.dto.response.ErrorResponse;
import ru.mephi.trainer.rest.dto.response.task.TaskResponse;

import java.util.UUID;

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
                    responseCode = "400",
                    description = "Неверные параметры запроса",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @APIResponse(
                    responseCode = "401",
                    description = "Не авторизован",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @APIResponse(
                    responseCode = "403",
                    description = "Доступ запрещён",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @APIResponse(
                    responseCode = "404",
                    description = "Тренажёр не найден",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @APIResponse(
                    responseCode = "500",
                    description = "Неожиданная ошибка",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    RestResponse<TaskResponse> createSingleChoiceTask(@RequestBody @Valid SingleChoiceTaskRequest createRequest);

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
                    responseCode = "400",
                    description = "Неверные параметры запроса",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @APIResponse(
                    responseCode = "401",
                    description = "Не авторизован",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @APIResponse(
                    responseCode = "403",
                    description = "Доступ запрещён",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @APIResponse(
                    responseCode = "404",
                    description = "Тренажёр не найден",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @APIResponse(
                    responseCode = "500",
                    description = "Неожиданная ошибка",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    RestResponse<TaskResponse> createMultipleChoiceTask(@RequestBody @Valid MultipleChoiceTaskRequest createRequest);  // ✅ исправлен тип

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
                    responseCode = "400",
                    description = "Неверные параметры запроса",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @APIResponse(
                    responseCode = "401",
                    description = "Не авторизован",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @APIResponse(
                    responseCode = "403",
                    description = "Доступ запрещён",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @APIResponse(
                    responseCode = "404",
                    description = "Тренажёр не найден",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @APIResponse(
                    responseCode = "500",
                    description = "Неожиданная ошибка",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    RestResponse<TaskResponse> createErrorFindingTask(@RequestBody @Valid ErrorFindingTaskRequest createRequest);

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
                    responseCode = "400",
                    description = "Неверные параметры запроса",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @APIResponse(
                    responseCode = "401",
                    description = "Не авторизован",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @APIResponse(
                    responseCode = "403",
                    description = "Доступ запрещён",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @APIResponse(
                    responseCode = "404",
                    description = "Тренажёр не найден",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @APIResponse(
                    responseCode = "500",
                    description = "Неожиданная ошибка",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    RestResponse<TaskResponse> createOpenAnswerTask(@RequestBody @Valid OpenAnswerTaskRequest createRequest);

    @PUT
    @Path("/single-choice/{id}")
    @Operation(
            operationId = "updateSingleChoiceTask",
            summary = "Обновление задания с одиночным выбором",
            description = "Обновить существующее задание с одиночным выбором"
    )
    @SecurityRequirement(name = "bearerAuth")
    @APIResponses(value = {
            @APIResponse(
                    responseCode = "200",
                    description = "Задание успешно обновлено",
                    content = @Content(schema = @Schema(implementation = TaskResponse.class))
            ),
            @APIResponse(
                    responseCode = "400",
                    description = "Неверные параметры запроса",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @APIResponse(
                    responseCode = "401",
                    description = "Не авторизован",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @APIResponse(
                    responseCode = "403",
                    description = "Доступ запрещён",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @APIResponse(
                    responseCode = "404",
                    description = "Задание или тренажёр не найдены",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @APIResponse(
                    responseCode = "500",
                    description = "Неожиданная ошибка",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    RestResponse<TaskResponse> updateSingleChoiceTask(
            @PathParam("id") UUID id,
            @RequestBody @Valid SingleChoiceTaskRequest updateRequest
    );

    @PUT
    @Path("/multiple-choice/{id}")
    @Operation(
            operationId = "updateMultipleChoiceTask",
            summary = "Обновление задания с множественным выбором",
            description = "Обновить существующее задание с множественным выбором"
    )
    @SecurityRequirement(name = "bearerAuth")
    @APIResponses(value = {
            @APIResponse(
                    responseCode = "200",
                    description = "Задание успешно обновлено",
                    content = @Content(schema = @Schema(implementation = TaskResponse.class))
            ),
            @APIResponse(
                    responseCode = "400",
                    description = "Неверные параметры запроса",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @APIResponse(
                    responseCode = "401",
                    description = "Не авторизован",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @APIResponse(
                    responseCode = "403",
                    description = "Доступ запрещён",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @APIResponse(
                    responseCode = "404",
                    description = "Задание или тренажёр не найдены",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @APIResponse(
                    responseCode = "500",
                    description = "Неожиданная ошибка",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    RestResponse<TaskResponse> updateMultipleChoiceTask(
            @PathParam("id") UUID id,
            @RequestBody @Valid MultipleChoiceTaskRequest updateRequest
    );

    @PUT
    @Path("/error-finding/{id}")
    @Operation(
            operationId = "updateErrorFindingTask",
            summary = "Обновление задания на поиск ошибок",
            description = "Обновить существующее задание на поиск ошибок"
    )
    @SecurityRequirement(name = "bearerAuth")
    @APIResponses(value = {
            @APIResponse(
                    responseCode = "200",
                    description = "Задание успешно обновлено",
                    content = @Content(schema = @Schema(implementation = TaskResponse.class))
            ),
            @APIResponse(
                    responseCode = "400",
                    description = "Неверные параметры запроса",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @APIResponse(
                    responseCode = "401",
                    description = "Не авторизован",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @APIResponse(
                    responseCode = "403",
                    description = "Доступ запрещён",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @APIResponse(
                    responseCode = "404",
                    description = "Задание или тренажёр не найдены",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @APIResponse(
                    responseCode = "500",
                    description = "Неожиданная ошибка",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    RestResponse<TaskResponse> updateErrorFindingTask(
            @PathParam("id") UUID id,
            @RequestBody @Valid ErrorFindingTaskRequest updateRequest
    );

    @PUT
    @Path("/open-answer/{id}")
    @Operation(
            operationId = "updateOpenAnswerTask",
            summary = "Обновление задания с открытым ответом",
            description = "Обновить существующее задание с открытым ответом"
    )
    @SecurityRequirement(name = "bearerAuth")
    @APIResponses(value = {
            @APIResponse(
                    responseCode = "200",
                    description = "Задание успешно обновлено",
                    content = @Content(schema = @Schema(implementation = TaskResponse.class))
            ),
            @APIResponse(
                    responseCode = "400",
                    description = "Неверные параметры запроса",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @APIResponse(
                    responseCode = "401",
                    description = "Не авторизован",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @APIResponse(
                    responseCode = "403",
                    description = "Доступ запрещён",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @APIResponse(
                    responseCode = "404",
                    description = "Задание или тренажёр не найдены",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @APIResponse(
                    responseCode = "500",
                    description = "Неожиданная ошибка",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    RestResponse<TaskResponse> updateOpenAnswerTask(
            @PathParam("id") UUID id,
            @RequestBody @Valid OpenAnswerTaskRequest updateRequest
    );
}
