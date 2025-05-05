package ru.safonov.test_task.services;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.safonov.test_task.dto.error.NotValidUserException;
import ru.safonov.test_task.mapper.UserMapper;
import ru.safonov.test_task.models.User;
import ru.safonov.test_task.repositories.UserRepository;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

@Slf4j
@Service
@Transactional(readOnly = true)
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserService(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    public User findOne(long userId) {
        log.debug("Attempting to find user by ID: {}", userId);
        try {
            User user = userRepository.findById(userId)
                    .orElseThrow(() ->
                            new EntityNotFoundException(String.format("Пользователь с id %s не найден", userId))
                    );
            log.info("Successfully found user: {} (ID: {})", user.getName(), userId);
            return user;
        } catch (EntityNotFoundException ex) {
            log.warn("User not found with ID: {}", userId);
            throw ex;
        }
    }


    @Cacheable("users")
    public User findByEmail(String email) {
        log.debug("Searching user by email: {}", email);
        try {
            User findedUser = userRepository.findByEmail(email)
                    .orElseThrow(() ->
                            new EntityNotFoundException(String.format("Пользователь с email: %s не найден.", email)));
            log.info("User found by email: {}. User: id - {}, name - {}", email, findedUser.getName(), findedUser.getName());
            return findedUser;
        } catch (EntityNotFoundException ex) {
            log.error("User search failed for email: {}", email, ex);
            throw ex;
        }
    }


    @Transactional
    @CachePut(value = "users", key = "#newUser.id")
    public User save(User newUser) {
        log.info("Creating new user with ID: {} Name: {}", newUser.getId(), newUser.getName());
        User savedUser = userRepository.save(newUser);
        log.debug("User created successfully. ID: {}, Name: {}", savedUser.getId(), savedUser.getName());
        return savedUser;
    }

    @Transactional
    @CacheEvict("users")
    public User update(long userId, User updatedUser) {
        updatedUser.setId(userId);
        return userRepository.save(updatedUser);
    }

    @Caching(
            cacheable = {
                    @Cacheable("users"),
                    @Cacheable("emails"),
                    @Cacheable("phones")
            }
    )
    public List<User> findUsersByName(int page, int userPerPage, String name) {
        log.debug("Searching users by name: '{}' (Page: {}, Per page: {})", name, page, userPerPage);
        List<User> result = userRepository.findByNameContainsIgnoreCase(name, PageRequest.of(page, userPerPage));
        log.info("Found {} users by name: '{}'", result.size(), name);
        return result;
    }

    @Caching(
            cacheable = {
                    @Cacheable("users"),
                    @Cacheable("emails"),
                    @Cacheable("phones")
            }
    )
    public List<User> findUsersByNameAndBirthDay(int page, int userPerPage, String name, LocalDate dateOfBirth) {
        log.debug("Searching users by name: '{}' and birth date after: {} (Page: {}, Per page: {})",
                name, dateOfBirth, page, userPerPage);
        List<User> result = userRepository.findByNameContainsIgnoreCaseAndDateOfBirthAfter(name, dateOfBirth,
                PageRequest.of(page, userPerPage));
        log.info("Found {} users by name '{}' born after {}", result.size(), name, dateOfBirth);
        return result;
    }

    @Caching(
            cacheable = {
                    @Cacheable("users"),
                    @Cacheable("emails"),
                    @Cacheable("phones")
            }
    )
    public List<User> findUsersByDateOfBirth(int page, int userPerPage, LocalDate dateOfBirth) {
        log.debug("Searching users born after: {} (Page: {}, Per page: {})", dateOfBirth, page, userPerPage);
        List<User> result = userRepository.findByDateOfBirthAfter(dateOfBirth, PageRequest.of(page, userPerPage));
        log.info("Found {} users born after {}", result.size(), dateOfBirth);
        return result;
    }

    @Cacheable("users")
    public User findByIdWithLock(Long senderId) {
        return userRepository.findByIdWithLock(senderId)
                .orElseThrow(() ->
                        new EntityNotFoundException(String.format("Пользователь с id: %d не найден", senderId)));
    }
}