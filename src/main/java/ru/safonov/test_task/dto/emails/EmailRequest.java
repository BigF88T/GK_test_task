package ru.safonov.test_task.dto.emails;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record EmailRequest(
        @NotBlank(message = "Адрес электронной почты не должен быть пустым.")
        @Email(message = "Невалидный адрес электронной почты.")
        String email
) {
}
