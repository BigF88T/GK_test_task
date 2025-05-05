package ru.safonov.test_task.security.expression;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import ru.safonov.test_task.models.Email;
import ru.safonov.test_task.models.Phone;
import ru.safonov.test_task.models.User;
import ru.safonov.test_task.services.EmailService;
import ru.safonov.test_task.services.PhoneService;
import ru.safonov.test_task.services.UserService;

@Service
@RequiredArgsConstructor
public class JwtUserDetailsService implements UserDetailsService {

    private final EmailService emailService;
    private final PhoneService phoneService;
    private final UserService userService;

    @Override
    public UserDetails loadUserByUsername(String email) {

        User user = userService.findByEmail(email);
        Email e = new Email();
        e.setEmail(email);
        return JwtEntityFactory.create(user, e);
    }

    public UserDetails loadUserByPhone(String phone) {
        User user = phoneService.getUserByPhoneNum(phone);
        Phone p = new Phone();
        p.setPhoneNumber(phone);
        return JwtEntityFactory.create(user, p);
    }
}
