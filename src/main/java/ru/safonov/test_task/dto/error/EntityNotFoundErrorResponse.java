package ru.safonov.test_task.dto.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class EntityNotFoundErrorResponse {
    private String message;
    private String date;
}

