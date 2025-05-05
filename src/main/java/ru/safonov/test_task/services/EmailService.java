package ru.safonov.test_task.services;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.safonov.test_task.dto.error.NotValidEmailException;
import ru.safonov.test_task.models.Email;
import ru.safonov.test_task.models.User;
import ru.safonov.test_task.repositories.EmailRepository;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class EmailService {
    private final EmailRepository emailRepository;

    public void checkAvailability(String email) {

        if (emailRepository.findByEmail(email).isPresent())
            throw new NotValidEmailException(String.format("Адрес электронной почты: %s уже занят", email));
    }

    @Cacheable("emails")
    public Email findByEmail(String email) {
        return emailRepository.findByEmail(email)
                .orElseThrow(() ->
                        new EntityNotFoundException(String.format("Пользователь с адресом почты: %s не найден.", email)));
    }

    public User getUserByEmail(String email) {
        return emailRepository.findByEmail(email)
                .orElseThrow(() ->
                        new EntityNotFoundException(String.format("Пользователь с адресом почты: %s не найден", email)))
                .getUser();
    }

    @Transactional
    @CachePut("emails")
    public Email save(Email email) {
        return emailRepository.save(email);
    }

    @Transactional
    @CacheEvict("emails")
    public Email update(Long emailId, Email updatedEmail) {
        updatedEmail.setId(emailId);
        return emailRepository.save(updatedEmail);
    }

    @Transactional
    @CacheEvict("emails")
    public void delete(Email email) {
        emailRepository.delete(email);
    }
}
