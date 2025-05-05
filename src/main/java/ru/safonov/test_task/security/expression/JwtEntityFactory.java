package ru.safonov.test_task.security.expression;

import ru.safonov.test_task.models.Email;
import ru.safonov.test_task.models.Phone;
import ru.safonov.test_task.models.User;

import java.util.Collections;

public class JwtEntityFactory {

    public static JwtEntity create(User user) {
        return new JwtEntity(
                user.getId(),
                user.getName(),
                user.getPassword(),
                Collections.emptyList());
    }

    public static JwtEntity create(User user, Email email) {
        return new JwtEntity(
                user.getId(),
                email.getEmail(),
                user.getPassword(),
                Collections.emptyList()
        );
    }

    public static JwtEntity create(User user, Phone phone) {
        return new JwtEntity(
                user.getId(),
                phone.getPhoneNumber(),
                user.getPassword(),
                Collections.emptyList()
        );
    }

}
