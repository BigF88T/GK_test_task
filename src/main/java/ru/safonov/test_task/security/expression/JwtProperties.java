package ru.safonov.test_task.security.expression;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Data
@ConfigurationProperties(prefix = "security.jwt")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class JwtProperties {
    String secret;
    long access;
    long refresh;
    long expirationTime = 8400000;
}


