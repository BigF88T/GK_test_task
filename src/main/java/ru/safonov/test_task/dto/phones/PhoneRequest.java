package ru.safonov.test_task.dto.phones;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record PhoneRequest(
        @NotBlank(message = "Номер телефона не должен быть пустым")
        @Pattern(regexp = "^[78]\\d{10}$", message = "Номер телефона должен содержать 11 цифр и начинаться с 7 или 8")
        String phoneNumber
) {
}
