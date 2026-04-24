package ru.mephi.trainer.rest.controller;

import org.jboss.resteasy.reactive.RestResponse;
import ru.mephi.trainer.rest.api.AuthApi;
import ru.mephi.trainer.rest.dto.request.RegisterRequest;
import ru.mephi.trainer.rest.dto.response.HelloResponse;
import ru.mephi.trainer.rest.dto.response.RegisterResponse;

public class AuthController implements AuthApi {

    @Override
    public RestResponse<HelloResponse> sayHello() {
        return RestResponse.ok(HelloResponse.builder()
                .message("Привет!")
                .build()
        );
    }

    @Override
    public RestResponse<HelloResponse> showError() {
        throw new IllegalStateException("Какая-то неожиданная ошибка");
    }

    @Override
    public RestResponse<RegisterResponse> registerUser(RegisterRequest registerRequest) {
        return RestResponse.status(RestResponse.Status.CREATED, RegisterResponse.builder()
                .message("Вы успешно зарегистрированы")
                .build());
    }
}
