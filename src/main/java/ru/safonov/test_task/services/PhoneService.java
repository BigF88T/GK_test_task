package ru.safonov.test_task.services;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.safonov.test_task.dto.error.NotValidEmailException;
import ru.safonov.test_task.dto.error.NotValidPhoneException;
import ru.safonov.test_task.models.Phone;
import ru.safonov.test_task.models.User;
import ru.safonov.test_task.repositories.PhoneRepository;

import java.util.Optional;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PhoneService {
    private final PhoneRepository phoneRepository;

    public void checkAvailability(String phoneNum) {
        log.info("Checking availability for phone number: {}", phoneNum);
        try {
            Optional<Phone> existingPhone = phoneRepository.findByPhoneNumber(phoneNum);
            existingPhone.ifPresentOrElse(
                    phone -> {
                        log.warn("Phone number {} is already taken by user ID: {}", phoneNum, phone.getUser().getId());
                        throw new NotValidPhoneException(String.format("Номер телефона: %s уже занят", phoneNum));
                    },
                    () -> log.debug("Phone number {} is available", phoneNum)
            );
        } catch (Exception e) {
            log.error("Error checking phone number availability: {}. Message: {}", phoneNum, e.getMessage());
            throw e;
        }
    }

    public Phone findByPhoneNum(String phoneNum) {
        log.info("Searching phone by number: {}", phoneNum);
        try {
            Phone phone = phoneRepository.findByPhoneNumber(phoneNum)
                    .orElseThrow(() -> {
                        log.warn("Phone number not found: {}", phoneNum);
                        return new EntityNotFoundException(String.format("Пользователь с номером телефона: %s не найден.", phoneNum));
                    });
            log.debug("Found phone record: ID {}", phone.getId());
            return phone;
        } catch (Exception e) {
            log.error("Phone lookup failed for number: {}. Message: {}", phoneNum, e.getMessage());
            throw e;
        }
    }

    public User getUserByPhoneNum(String phoneNum) {
        log.info("Retrieving user by phone number: {}", phoneNum);
        try {
            User user = phoneRepository.findByPhoneNumber(phoneNum)
                    .orElseThrow(() -> {
                        log.warn("User lookup failed - phone not found: {}", phoneNum);
                        return new EntityNotFoundException(String.format("Пользователь с номером телефона: %s не найден.", phoneNum));
                    })
                    .getUser();
            log.debug("Retrieved user ID: {} for phone: {}", user.getId(), phoneNum);
            return user;
        } catch (Exception e) {
            log.error("Failed to get user by phone: {}. Message: {}", phoneNum, e.getMessage());
            throw e;
        }
    }

    @Transactional
    @CachePut(value = "phones", key = "#newPhone.phoneNumber")
    public Phone save(Phone newPhone) {
        log.info("Creating new phone record for number: {}", newPhone.getPhoneNumber());
        try {
            Phone savedPhone = phoneRepository.save(newPhone);
            log.info("Created phone record ID: {} for number: {}",
                    savedPhone.getId(), savedPhone.getPhoneNumber());
            return savedPhone;
        } catch (Exception e) {
            log.error("Failed to create phone record for number: {}. Message: {}",
                    newPhone.getPhoneNumber(), e.getMessage());
            throw e;
        }
    }

    @Transactional
    @CacheEvict("phones")
    public Phone update(long phoneId, Phone updatedPhone) {
        log.info("Updating phone ID: {}", phoneId);
        try {
            updatedPhone.setId(phoneId);
            Phone result = phoneRepository.save(updatedPhone);
            log.info("Updated phone ID: {}, new number: {}",
                    phoneId, updatedPhone.getPhoneNumber());
            return result;
        } catch (Exception e) {
            log.error("Failed to update phone ID: {}. Message: {}", phoneId, e.getMessage());
            throw e;
        }
    }


    @Transactional
    @CacheEvict("phones")
    public void delete(Phone deletedPhone) {
        log.info("Deleting phone ID: {} (number: {})",
                deletedPhone.getId(), deletedPhone.getPhoneNumber());
        try {
            phoneRepository.delete(deletedPhone);
            log.info("Successfully deleted phone ID: {}", deletedPhone.getId());
        } catch (Exception e) {
            log.error("Failed to delete phone ID: {}", deletedPhone.getId());
            throw e;
        }
    }
}
