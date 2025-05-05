package ru.safonov.test_task.services;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import ru.safonov.test_task.dto.auth.JWTMailRequest;
import ru.safonov.test_task.dto.auth.JWTPhoneRequest;
import ru.safonov.test_task.dto.auth.JWTResponse;
import ru.safonov.test_task.mapper.EmailMapper;
import ru.safonov.test_task.mapper.PhoneMapper;
import ru.safonov.test_task.models.User;
import ru.safonov.test_task.security.expression.JwtTokenProvider;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final EmailService emailService;
    private final PhoneService phoneService;
    private final EmailMapper emailMapper;
    private final PhoneMapper phoneMapper;
    private final JwtTokenProvider jwtTokenProvider;

    public JWTResponse loginByMail(JWTMailRequest loginRequest) {
        JWTResponse jwtResponse = new JWTResponse();
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
        User user = userService.findByEmail(loginRequest.getEmail());
        return getJwtResponse(jwtResponse, user);
    }

    public JWTResponse loginByPhone(JWTPhoneRequest loginRequest) {
        JWTResponse jwtResponse = new JWTResponse();
        User user = phoneService.getUserByPhoneNum(loginRequest.getPhoneNumber());
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getEmails().stream().findFirst().get().getEmail(), loginRequest.getPassword()));

        return getJwtResponse(jwtResponse, user);
    }

    private JWTResponse getJwtResponse(JWTResponse jwtResponse, User user) {
        jwtResponse.setId(user.getId());
        jwtResponse.setName(user.getName());
        jwtResponse.setPhones(phoneMapper.toResp(user.getPhoneNumbers()));
        jwtResponse.setEmails(emailMapper.toResp(user.getEmails()));
        jwtResponse.setAccessToken(jwtTokenProvider.createAccessToken(user.getId(), user.getEmails().stream().findFirst().get().getEmail()));
        return jwtResponse;
    }

}
