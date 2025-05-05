package ru.safonov.test_task.dto.auth;

import lombok.Data;
import ru.safonov.test_task.dto.emails.EmailResponse;
import ru.safonov.test_task.dto.phones.PhoneResponse;

import java.util.Set;

@Data
public class JWTResponse {
    private String accessToken;
    private Long id;
    private EmailResponse emails;
    private PhoneResponse phones;
    private String name;
}
