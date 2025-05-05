package ru.safonov.test_task.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.safonov.test_task.dto.accounts.TransferRequest;
import ru.safonov.test_task.dto.users.UserResponse;
import ru.safonov.test_task.facade.AccountFacade;
import ru.safonov.test_task.security.expression.JwtTokenProvider;

@RestController
@RequestMapping("/transfers")
@RequiredArgsConstructor
public class TransferController {

    private final AccountFacade accountFacade;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping
    public ResponseEntity<UserResponse> transferMoney(@Valid @RequestBody TransferRequest req,
                                                      HttpServletRequest httpReq) {

        UserResponse resp = accountFacade.transferFunds(jwtTokenProvider.getId(httpReq), req.recipientId(), req.amount());

        return new ResponseEntity<>(resp, HttpStatus.OK);
    }


}
