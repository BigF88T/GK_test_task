package ru.safonov.test_task.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.safonov.test_task.dto.auth.JWTMailRequest;
import ru.safonov.test_task.dto.auth.JWTPhoneRequest;
import ru.safonov.test_task.dto.auth.JWTResponse;
import ru.safonov.test_task.services.AuthService;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication Controller", description = "Authentication API")
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login/email")
    public JWTResponse loginByMail(@Validated @RequestBody JWTMailRequest loginRequest) {
        return authService.loginByMail(loginRequest);
    }

    @PostMapping("/login/phone")
    public JWTResponse loginByPhoneNum(@Validated @RequestBody JWTPhoneRequest loginRequest) {
        return authService.loginByPhone(loginRequest);
    }

}