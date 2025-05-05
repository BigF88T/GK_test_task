package ru.safonov.test_task.dto.users;

public record UserRegisterResponse(
        String email,
        String phoneNumber
) {
}
