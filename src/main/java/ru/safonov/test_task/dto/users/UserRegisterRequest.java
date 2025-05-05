package ru.safonov.test_task.dto.users;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UserRegisterRequest(

        @Email
        String email,
        @Pattern(regexp = "^[78]\\d{10}$", message = "Номер телефона должен содержать 11 цифр и начинаться с 7 или 8.")
        String phoneNumber,
        @Size(min = 8, message = "Пароль должен содержать 8 символов.")
        String password
) {
}
