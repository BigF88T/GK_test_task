package ru.safonov.test_task.dto.phones;

import ru.safonov.test_task.models.Phone;

import java.util.Set;

public record PhoneResponse(
        Set<String> phones
) {
}
