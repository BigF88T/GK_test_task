package ru.safonov.test_task.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.safonov.test_task.dto.emails.EmailRequest;
import ru.safonov.test_task.dto.emails.EmailResponse;
import ru.safonov.test_task.dto.emails.EmailUpdateRequest;
import ru.safonov.test_task.dto.phones.PhoneRequest;
import ru.safonov.test_task.dto.phones.PhoneResponse;
import ru.safonov.test_task.dto.phones.PhoneUpdateRequest;
import ru.safonov.test_task.dto.users.FoundedUsersResponse;
import ru.safonov.test_task.dto.users.UserResponse;
import ru.safonov.test_task.facade.UserFacade;
import ru.safonov.test_task.security.expression.JwtTokenProvider;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserFacade userFacade;
    private final JwtTokenProvider jwtTokenProvider;

    @GetMapping("/find")
    public ResponseEntity<List<UserResponse>> findUsers(@RequestParam(value = "page", defaultValue = "0", required = false) Integer page,
                                                        @RequestParam(value = "user_per_page", defaultValue = "10", required = false) Integer userPerPage,
                                                        @RequestParam(value = "date_of_birth", required = false) String dateOfBirth,
                                                        @RequestParam(value = "phone", required = false) String phoneNum,
                                                        @RequestParam(value = "name", required = false) String name,
                                                        @RequestParam(value = "email", required = false) String email) {
        List<UserResponse> resp = userFacade.findUsers(page, userPerPage, dateOfBirth, phoneNum, name, email);
        return new ResponseEntity<>(resp, HttpStatus.OK);
    }

    @PostMapping("/emails")
    public ResponseEntity<EmailResponse> addEmailToUser(@RequestBody @Valid EmailRequest req,
                                                        BindingResult bindingResult,
                                                        HttpServletRequest httpReq) {
        EmailResponse resp = userFacade.addEmail(jwtTokenProvider.getId(httpReq), req);
        return new ResponseEntity<>(resp, HttpStatus.OK);
    }

    @DeleteMapping("/emails")
    public ResponseEntity<EmailResponse> deleteEmailToUser(@RequestBody @Valid EmailRequest req,
                                                           BindingResult bindingResult,
                                                           HttpServletRequest httpReq) {
        EmailResponse resp = userFacade.deleteEmail(jwtTokenProvider.getId(httpReq), req);
        return new ResponseEntity<>(resp, HttpStatus.OK);
    }

    @PatchMapping("/emails")
    public ResponseEntity<EmailResponse> editEmailToUser(@RequestBody @Valid EmailUpdateRequest req,
                                                         BindingResult bindingResult,
                                                         HttpServletRequest httpReq) {
        EmailResponse resp = userFacade.editEmail(jwtTokenProvider.getId(httpReq), req);
        return new ResponseEntity<>(resp, HttpStatus.OK);

    }

    @PostMapping("/phones")
    public ResponseEntity<PhoneResponse> addPhoneNumToUser(@RequestBody @Valid PhoneRequest req,
                                                           BindingResult bindingResult,
                                                           HttpServletRequest httpReq) {
        PhoneResponse resp = userFacade.addPhone(jwtTokenProvider.getId(httpReq), req);
        return new ResponseEntity<>(resp, HttpStatus.OK);
    }

    @DeleteMapping("/phones")
    public ResponseEntity<PhoneResponse> deletePhoneNumToUser(@RequestBody @Valid PhoneRequest req,
                                                              BindingResult bindingResult,
                                                              HttpServletRequest httpReq) {
        PhoneResponse resp = userFacade.deletePhone(jwtTokenProvider.getId(httpReq), req);
        return new ResponseEntity<>(resp, HttpStatus.OK);
    }

    @PatchMapping("/phones")
    public ResponseEntity<PhoneResponse> editPhoneNumToUser(@RequestBody @Valid PhoneUpdateRequest req,
                                                            BindingResult bindingResult,
                                                            HttpServletRequest httpReq) {
        PhoneResponse resp = userFacade.editPhone(jwtTokenProvider.getId(httpReq), req);
        return new ResponseEntity<>(resp, HttpStatus.OK);
    }

}