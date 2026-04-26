package ru.mephi.trainer.config;

import jakarta.ws.rs.core.Application;
import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition;
import org.eclipse.microprofile.openapi.annotations.info.Info;
import org.eclipse.microprofile.openapi.annotations.security.SecurityRequirement;

@OpenAPIDefinition(
        info = @Info(
                title = "API Тренажера",
                version = "1.0.0",
                description = "API для учебного проекта хакатона"
        ),
        security = @SecurityRequirement(name = "bearerAuth")
)
public class OpenApiConfig extends Application {
}
