package ru.mephi.trainer.rest.api;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.jboss.resteasy.reactive.RestResponse;
import ru.mephi.trainer.rest.dto.response.SimulatorsResponse;
import ru.mephi.trainer.rest.dto.response.SimulatorInfoResponse;
import ru.mephi.trainer.rest.dto.response.ErrorResponse;

@Path("/simulators")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Тренажеры", description = "Эндпоинты для тренажера")
public interface SimulatorsAPI {

    @GET
    @Path("/")
    @Operation(
            operationId = "getSimulators",
            summary = "Список тренажеров",
            description = "Получить информацию о тренажерах"
    )
    @APIResponses(value = {
            @APIResponse(
                    responseCode = "200",
                    description = "Данные о списке тренажеров успешно получены",
                    content = @Content(schema = @Schema(implementation = SimulatorsResponse.class))
            ),
            @APIResponse(
                    responseCode = "400",
                    description = "Неверный запрос",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @APIResponse(
                    responseCode = "401",
                    description = "Пользователь не авторизован",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @APIResponse(
                    responseCode = "500",
                    description = "Неожиданная ошибка",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    RestResponse<SimulatorsResponse> getSimulators();

    @GET
    @Path("/{id}")
    @Operation(
            operationId = "getSimulatorInfo",
            summary = "Информация о тренажере",
            description = "Получить информацию о тренажере"
    )
    @APIResponses(value = {
            @APIResponse(
                    responseCode = "200",
                    description = "Данные о прохождении тренажера успешно получены",
                    content = @Content(schema = @Schema(implementation = SimulatorInfoResponse.class))
            ),
            @APIResponse(
                    responseCode = "400",
                    description = "Неверные данные запроса",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @APIResponse(
                responseCode = "401",
                description = "Пользователь не авторизован",
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
    RestResponse<SimulatorInfoResponse> getSimulatorInfo(@PathParam("id") String simulatorId);
}
