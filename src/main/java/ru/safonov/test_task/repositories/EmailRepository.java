package ru.safonov.test_task.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.safonov.test_task.models.Email;

import java.util.Optional;

public interface EmailRepository extends JpaRepository<Email, Long> {
    Optional<Email> findByEmail(String email);

}
