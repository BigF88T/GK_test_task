package ru.safonov.test_task.repositories;

import jakarta.persistence.LockModeType;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.safonov.test_task.models.User;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Query
    List<User> findByNameContainsIgnoreCase(String query, PageRequest pageRequest);

    List<User> findByNameContainsIgnoreCaseAndDateOfBirthAfter(String name, LocalDate dateOfBirth, PageRequest pageRequest);

    List<User> findByDateOfBirthAfter(LocalDate dateOfBirth, PageRequest pageRequest);

    @Query("SELECT DISTINCT u FROM User u " +
            "LEFT JOIN FETCH u.emails e " +
            "WHERE e.email = :email")
    Optional<User> findByEmail(@Param("email") String email);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT u FROM User u WHERE u.id = :id")
    Optional<User> findByIdWithLock(@Param("id") Long senderId);
}
