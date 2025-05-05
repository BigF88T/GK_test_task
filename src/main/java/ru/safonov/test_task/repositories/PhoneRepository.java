package ru.safonov.test_task.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.safonov.test_task.models.Phone;

import java.util.Optional;

public interface PhoneRepository extends JpaRepository<Phone, Long> {
    Optional<Phone> findByPhoneNumber(String phoneNum);
}
