package ru.safonov.test_task.dto.auth;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class JWTMailRequest {
    @NotNull(message = "Адрес электронной почты не должен быть пустым.")
    private String email;
    @NotNull(message = "Пароль не должен быть пустым.")
    private String password;
}
