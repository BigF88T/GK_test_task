package ru.safonov.test_task.dto.accounts;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record TransferRequest(
        @NotNull(message = "Id пользователя не должен быть пустым")
        Long recipientId,

        @Positive(message = "Сумма должна быть положительной.")
        @Digits(integer = 10, fraction = 2, message = "Неверный формат суммы")
        BigDecimal amount
) {
}
