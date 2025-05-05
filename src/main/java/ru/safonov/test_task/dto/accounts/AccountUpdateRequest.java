package ru.safonov.test_task.dto.accounts;

import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;

public record AccountUpdateRequest(
        Long accountId,
        BigDecimal oldBalance,
        BigDecimal newBalance
) {
        public Object[] toUpdateParams() {
                return new Object[]{
                        this.accountId,
                        this.newBalance,
                        this.oldBalance
                };
        }
}