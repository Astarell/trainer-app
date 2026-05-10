package ru.mephi.trainer.rest.api;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.jboss.resteasy.reactive.RestResponse;
import ru.mephi.trainer.entity.TrainerEntity;
import ru.mephi.trainer.rest.dto.request.TaskSubmitRequest;
import ru.mephi.trainer.rest.dto.response.TaskResponse;
import ru.mephi.trainer.rest.dto.response.TrainerInfoResponse;
import ru.mephi.trainer.rest.dto.response.ErrorResponse;
import ru.mephi.trainer.rest.dto.response.TrainerResponse;

import java.util.List;
import java.util.UUID;

@Path("/trainers")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Тренажеры", description = "Эндпоинты для тренажера")
public interface TrainersAPI {

    @GET
    @Path("/")
    @Operation(
            operationId = "getTrainers",
            summary = "Список тренажеров",
            description = "Получить информацию о тренажерах"
    )
    @APIResponses(value = {
            @APIResponse(
                    responseCode = "200",
                    description = "Данные о списке тренажеров успешно получены",
                    content = @Content(schema = @Schema(implementation = TrainerResponse[].class))
            ),
            @APIResponse(
                    responseCode = "500",
                    description = "Неожиданная ошибка",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    RestResponse<List<TrainerResponse>> getTrainers();

    @GET
    @Path("/{id}")
    @Operation(
            operationId = "getTrainerInfo",
            summary = "Информация о тренажере",
            description = "Получить информацию о тренажере"
    )
    @APIResponses(value = {
            @APIResponse(
                    responseCode = "200",
                    description = "Данные о прохождении тренажера успешно получены",
                    content = @Content(schema = @Schema(implementation = TrainerInfoResponse.class))
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
    RestResponse<TrainerInfoResponse> getTrainerInfo(@PathParam("id") UUID trainerId);

    @GET
    @Path("/{id}/tasks")
    @Operation(
            operationId = "getTrainerTasks",
            summary = "Информация о задачах для тренажера",
            description = "Получить информацию о задачах для тренажера"
    )
    @APIResponses(value = {
            @APIResponse(
                    responseCode = "200",
                    description = "Данные о задачах для тренажера успешно получены",
                    content = @Content(schema = @Schema(implementation = List.class))
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
    RestResponse<List<TaskResponse>> getTrainerTasks(@PathParam("id") UUID trainerId);

    @POST
    @Path("/{id}/tasks/{task_id}/submit")
    @Operation(
            operationId = "saveTrainerTasksSubmit",
            summary = "Сохранение информации об исполнении задачи для тренажера",
            description = "Сохранить информацию об исполнении задачи для тренажера"
    )
    @APIResponses(value = {
            @APIResponse(
                    responseCode = "200",
                    description = "Данные об исполнении задачи для тренажера успешно сохранены",
                    content = @Content(schema = @Schema(implementation = String.class))
            ),
            @APIResponse(
                    responseCode = "404",
                    description = "Объект не найден",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @APIResponse(
                    responseCode = "500",
                    description = "Неожиданная ошибка",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    RestResponse<String> saveTrainerTasksSubmit(@PathParam("id") UUID trainerId,
                                                @PathParam("task_id") UUID taskId,
                                                @RequestBody TaskSubmitRequest taskSubmitRequest);
}
