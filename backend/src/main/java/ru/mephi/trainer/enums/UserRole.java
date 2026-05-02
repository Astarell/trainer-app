package ru.mephi.trainer.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserRole {
    APP_USER("student"),
    APP_EXPERT("expert"),
    APP_ADMIN("admin");

    private final String securityRole;
}
