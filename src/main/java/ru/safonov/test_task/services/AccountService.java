package ru.safonov.test_task.services;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import ru.safonov.test_task.dto.accounts.AccountProjection;
import ru.safonov.test_task.dto.accounts.AccountUpdateRequest;
import ru.safonov.test_task.dto.error.NotValidTransferException;
import ru.safonov.test_task.models.Account;
import ru.safonov.test_task.repositories.AccountRepository;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final JdbcTemplate jdbcTemplate;

    @Scheduled(fixedDelay = 30_000)
    @Transactional(
            isolation = Isolation.REPEATABLE_READ,
            rollbackFor = {Exception.class}
    )
    public void beginIncreaseBalance() {
        log.info("=== Starting scheduled balance increase ===");
        long startTime = System.currentTimeMillis();
        int totalAccounts = 0;
        int updatedCount = 0;
        int skippedZeroBalance = 0;
        int skippedMaxLimit = 0;

        List<Account> accounts = accountRepository.findAll();
        totalAccounts = accounts.size();
        log.info("Found {} accounts to process", totalAccounts);

        for (Account account : accounts) {
            BigDecimal startBalance = account.getStartBalance();

            if (startBalance.compareTo(BigDecimal.ZERO) ==0) {
                skippedZeroBalance++;
                continue;
            }

            BigDecimal balance = account.getBalance();
            BigDecimal maxBalanceBound = startBalance.multiply(new BigDecimal("3.07"));
            log.debug("Account {}: current={}, max_allowed={}",
                    account.getId(), balance, maxBalanceBound);

            if(balance.compareTo(maxBalanceBound) >= 0) {
                skippedMaxLimit++;
                continue;
            }

            BigDecimal newBalance = balance
                    .multiply(new BigDecimal("1.10"))
                    .setScale(4, RoundingMode.HALF_UP);

            if(newBalance.compareTo(maxBalanceBound) > 0) {
                newBalance = maxBalanceBound.setScale(4, RoundingMode.HALF_UP);
            }

            account.setBalance(newBalance);
            updatedCount++;
        }
        log.debug("Saving updated accounts...");
        accountRepository.saveAll(accounts);

        log.info("Successfully saved {} accounts", accounts.size());

        log.info(
                "=== Balance increase completed ===\n" +
                        "Duration: {} ms\n" +
                        "Total accounts: {}\n" +
                        "Updated: {}\n" +
                        "Skipped (zero balance): {}\n" +
                        "Skipped (max limit): {}\n",
                System.currentTimeMillis() - startTime,
                accounts.size(),
                updatedCount,
                skippedZeroBalance,
                skippedMaxLimit
        );
    }

    public Account getAccountByIdWithLock(Long userId) {
        return accountRepository.findByUserIdWithLock(userId)
                .orElseThrow(() ->
                        new EntityNotFoundException(String.format("Аккаунт пользователя с id: %d не найден.", userId)));
    }

    @Transactional(
            isolation = Isolation.REPEATABLE_READ,
            rollbackFor = {Exception.class}
    )
    public Account beginTransfer(Long senderId, Long recipientId, BigDecimal amount) {

        Account sendersAcc = getAccountByIdWithLock(senderId);

        if (sendersAcc.getBalance().compareTo(amount) < 0)
            throw new NotValidTransferException("На балансе пользователя недостаточно средств!");

        Account recipientAcc = getAccountByIdWithLock(recipientId);

        sendersAcc.setBalance(sendersAcc.getBalance().subtract(amount));
        recipientAcc.setBalance(recipientAcc.getBalance().add(amount));

        accountRepository.saveAll(List.of(sendersAcc, recipientAcc));
        return sendersAcc;
    }
}