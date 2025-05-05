package ru.safonov.test_task.dto.users;

import java.math.BigDecimal;
import java.util.Set;

public record UserResponse(
        Long id,

        String Name,

        String dateOfBirth,

        Set<String> emails,

        Set<String> phones,

        BigDecimal balance
) {
}
