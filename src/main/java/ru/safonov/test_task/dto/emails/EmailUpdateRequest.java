package ru.safonov.test_task.dto.emails;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;


public record EmailUpdateRequest(
        @NotBlank(message = "Изменяемый адрес не должен быть пустым.")
        @Email(message = "Невалидный адрес электронной почты.")
        String editableEmail,

        @NotBlank(message = "Новый адрес не должен быть пустым.")
        @Email(message = "Невалидный адрес электронной почты.")
        String newEmail
) {
}
