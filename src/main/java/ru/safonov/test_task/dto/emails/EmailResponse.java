package ru.safonov.test_task.dto.emails;

import java.util.Set;

public record EmailResponse(
        Set<String> email
) {
}
