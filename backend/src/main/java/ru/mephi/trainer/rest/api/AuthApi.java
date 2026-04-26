package ru.mephi.trainer.rest.api;

import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.jboss.resteasy.reactive.RestResponse;
import ru.mephi.trainer.rest.dto.request.RegisterRequest;
import ru.mephi.trainer.rest.dto.response.ErrorResponse;
import ru.mephi.trainer.rest.dto.response.HelloResponse;
import ru.mephi.trainer.rest.dto.response.RegisterResponse;

@Path("/auth")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Аутентификация", description = "Эндпоинты для регистрации и входа в систему")
public interface AuthApi {

    @GET
    @Path("/hello")
    @Operation(
            operationId = "sayHello",
            summary = "Проверка работы API",
            description = "Тестовый эндпоинт для проверки доступности сервиса"
    )
    @APIResponse(
            responseCode = "200",
            description = "Сервис работает",
            content = @Content(schema = @Schema(implementation = HelloResponse.class))
    )
    RestResponse<HelloResponse> sayHello();

    @GET
    @Path("/error")
    @Operation(
            operationId = "showError",
            summary = "Проверка обработчика ошибок",
            description = "Тестовый эндпоинт для проверки обработчика ошибок"
    )
    @APIResponse(
            responseCode = "500",
            description = "Неожиданная ошибка",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
    )
    RestResponse<HelloResponse> showError();

    @POST
    @Path("/register")
    @Operation(
            operationId = "registerUser",
            summary = "Регистрация пользователя",
            description = "Создание новой учетной записи пользователя"
    )
    @APIResponses(value = {
            @APIResponse(
                    responseCode = "201",
                    description = "Пользователь успешно зарегистрирован",
                    content = @Content(schema = @Schema(implementation = RegisterResponse.class))
            ),
            @APIResponse(
                    responseCode = "400",
                    description = "Неверные данные запроса",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @APIResponse(
                    responseCode = "409",
                    description = "Пользователь с таким email уже существует",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @APIResponse(
                    responseCode = "500",
                    description = "Неожиданная ошибка",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    RestResponse<RegisterResponse> registerUser(@Valid RegisterRequest registerRequest);
}
