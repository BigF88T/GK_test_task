package ru.safonov.test_task.dto.accounts;

import java.math.BigDecimal;

public interface AccountProjection {
    Long getId();
    BigDecimal getBalance();
    BigDecimal getStartBalance();
}