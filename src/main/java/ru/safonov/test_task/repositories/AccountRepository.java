package ru.safonov.test_task.repositories;

import jakarta.persistence.LockModeType;
import org.hibernate.annotations.BatchSize;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.safonov.test_task.models.Account;
import ru.safonov.test_task.dto.accounts.AccountProjection;

import java.util.List;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT a FROM Account a WHERE a.user.id = :id")
    Optional<Account> findByUserIdWithLock(@Param("id") Long userId);

    @Query("SELECT a.id as id, a.balance as balance, a.startBalance as startBalance FROM Account a")
    List<AccountProjection> findAllForUpdate();

    @Modifying
    @Query(value = """
    UPDATE gk_task.accounts 
    SET balance = ?2 
    WHERE id = ?1 
      AND balance = ?3
    """, nativeQuery = true)
    @Transactional
    @BatchSize(size = 100)
    int[] batchUpdateBalances(List<Object[]> params);
}