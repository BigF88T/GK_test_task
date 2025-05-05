package ru.safonov.test_task.dto.auth;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class JWTPhoneRequest {
    @NotNull(message = "Номер телефона не должен быть пустым.")
    @Pattern(regexp = "^[78]\\d{10}$", message = "Номер телефона должен содержать 11 цифр и начинаться с 7 или 8")
    private String phoneNumber;
    @NotNull(message = "Пароль не должен быть пустым.")
    private String password;
}
