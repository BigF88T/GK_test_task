package ru.safonov.test_task.dto.users;

import java.util.List;

public record FoundedUsersResponse(
        List<UserResponse> users
) {
}
