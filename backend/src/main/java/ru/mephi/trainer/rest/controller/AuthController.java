package ru.mephi.trainer.rest.controller;

import org.jboss.resteasy.reactive.RestResponse;
import ru.mephi.trainer.rest.api.AuthApi;
import ru.mephi.trainer.rest.dto.request.RegisterRequest;
import ru.mephi.trainer.rest.dto.response.HelloResponse;
import ru.mephi.trainer.rest.dto.response.RegisterResponse;

import java.time.Instant;

public class AuthController implements AuthApi {

    @Override
    public RestResponse<HelloResponse> hello() {
        return RestResponse.ok(HelloResponse.builder()
                .message("Привет!")
                .timestamp(Instant.now())
                .build()
        );
    }

    @Override
    public RestResponse<HelloResponse> error() {
        throw new IllegalStateException("Какая-то неожиданная ошибка");
    }

    @Override
    public RestResponse<RegisterResponse> register(RegisterRequest registerRequest) {
        return RestResponse.ok(RegisterResponse.builder()
                .message("Вы успешно зарегистрированы")
                .timestamp(Instant.now())
                .build());
    }
}
