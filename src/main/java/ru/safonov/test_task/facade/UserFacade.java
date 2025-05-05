package ru.safonov.test_task.facade;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.safonov.test_task.dto.emails.EmailRequest;
import ru.safonov.test_task.dto.emails.EmailResponse;
import ru.safonov.test_task.dto.emails.EmailUpdateRequest;
import ru.safonov.test_task.dto.error.NotValidUserException;
import ru.safonov.test_task.dto.phones.PhoneRequest;
import ru.safonov.test_task.dto.phones.PhoneResponse;
import ru.safonov.test_task.dto.phones.PhoneUpdateRequest;
import ru.safonov.test_task.dto.users.UserResponse;
import ru.safonov.test_task.mapper.EmailMapper;
import ru.safonov.test_task.mapper.PhoneMapper;
import ru.safonov.test_task.mapper.UserMapper;
import ru.safonov.test_task.models.Email;
import ru.safonov.test_task.models.Phone;
import ru.safonov.test_task.models.User;
import ru.safonov.test_task.services.EmailService;
import ru.safonov.test_task.services.PhoneService;
import ru.safonov.test_task.services.UserService;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static java.util.Objects.isNull;
import static ru.safonov.test_task.util.PhoneNumberValidation.validate;

@Component
@RequiredArgsConstructor
public class UserFacade {
    private final UserService userService;
    private final EmailService emailService;
    private final PhoneService phoneService;
    private final UserMapper userMapper;
    private final EmailMapper emailMapper;
    private final PhoneMapper phoneMapper;


    public EmailResponse addEmail(long userId, EmailRequest req) {
        User updatedUser = userService.findOne(userId);
        Email newEmail = emailMapper.toEnt(req);

        emailService.checkAvailability(newEmail.getEmail());

        newEmail.setUser(updatedUser);
        emailService.save(newEmail);
        return emailMapper.toResp(updatedUser.getEmails());
    }

    public EmailResponse deleteEmail(long userId, EmailRequest req) {
        User updatedUser = userService.findOne(userId);
        Email findedEmail = emailService.findByEmail(req.email());

        if (updatedUser == findedEmail.getUser()) {
            updatedUser.getEmails().remove(findedEmail);
            emailService.delete(findedEmail);
        } else {
            throw new EntityNotFoundException(String.format("Email: %s не найден у пользователя: %s", findedEmail.getEmail(), updatedUser.getName()));
        }

        return emailMapper.toResp(updatedUser.getEmails());
    }

    public EmailResponse editEmail(long userId, EmailUpdateRequest req) {
        emailService.checkAvailability(req.newEmail());
        User updatedUser = userService.findOne(userId);

        Email newEmail = new Email();
        newEmail.setUser(updatedUser);
        newEmail.setEmail(req.newEmail());

        Email findedEmail = emailService.findByEmail(req.editableEmail());

        if (findedEmail.getUser().equals(updatedUser)) {
            emailService.update(findedEmail.getId(), newEmail);
        } else {
            throw new EntityNotFoundException(String.format("Email: %s не найден у пользователя: %s", findedEmail.getEmail(), updatedUser.getName()));
        }

        return emailMapper.toResp(updatedUser.getEmails());
    }

    public PhoneResponse addPhone(long userId, PhoneRequest req) {
        validate(req);

        User updatedUser = userService.findOne(userId);
        Phone newPhone = phoneMapper.toEnt(req);

        phoneService.checkAvailability(newPhone.getPhoneNumber());
        newPhone.setUser(updatedUser);
        phoneService.save(newPhone);

        return phoneMapper.toResp(updatedUser.getPhoneNumbers());
    }

    public PhoneResponse editPhone(long userId, PhoneUpdateRequest req) {
        phoneService.checkAvailability(req.newNumber());
        User updatedUser = userService.findOne(userId);
        Set<Phone> userPhoneNumbers = updatedUser.getPhoneNumbers();
        Phone newPhone = new Phone();
        newPhone.setUser(updatedUser);
        newPhone.setPhoneNumber(req.newNumber());

        Phone findedPhone = phoneService.findByPhoneNum(req.editableNumber());

        if (findedPhone.getUser() == updatedUser) {
            phoneService.update(findedPhone.getId(), newPhone);
        } else {
            throw new EntityNotFoundException(String.format("Номер телефона: %s не найден у пользователя: %s", newPhone.getPhoneNumber(), updatedUser.getName()));
        }

        return phoneMapper.toResp(updatedUser.getPhoneNumbers());
    }

    public PhoneResponse deletePhone(long userId, PhoneRequest req) {
        validate(req);
        User updatedUser = userService.findOne(userId);

        Phone findedPhone = phoneService.findByPhoneNum(req.phoneNumber());
        if (findedPhone.getUser() == updatedUser) {
            updatedUser.getPhoneNumbers().remove(findedPhone);
            phoneService.delete(findedPhone);
        } else {
            throw new EntityNotFoundException(String.format("Номер телефона: %s не найден у пользователя: %s", findedPhone.getPhoneNumber(), updatedUser.getName()));
        }

        return phoneMapper.toResp(updatedUser.getPhoneNumbers());
    }

    public List<UserResponse> findUsers(Integer page,
                                  Integer userPerPage,
                                  String dateOfBirth,
                                  String phoneNum,
                                  String name,
                                  String email) {

        List<User> foundedUsers = new ArrayList<>();

        if (!isNull(phoneNum)) {
            Phone findedPhone = phoneService.findByPhoneNum(phoneNum);
            return List.of(userMapper.toResp(findedPhone.getUser()));
        }

        if (!isNull(email)) {
            Email findedEmail = emailService.findByEmail(email);
            return List.of(userMapper.toResp(findedEmail.getUser()));
        }

        if(!isNull(dateOfBirth)) {
            LocalDate date;

            try {
                date = LocalDate.parse(dateOfBirth, DateTimeFormatter.ofPattern("dd.MM.yyyy"));
            } catch (DateTimeParseException e){
                throw new NotValidUserException("Неправильная дата. Проверьте формат даты: *дд.мм.гггг*. ");
            }

            if (!isNull(name))
                foundedUsers.addAll(userService.findUsersByNameAndBirthDay(page, userPerPage, name, date));
            else
                foundedUsers.addAll(userService.findUsersByDateOfBirth(page, userPerPage, date));
        } else if (!isNull(name)) {
            foundedUsers.addAll(userService.findUsersByName(page, userPerPage, name));
        }

        return userMapper.toResp(foundedUsers);
    }
}