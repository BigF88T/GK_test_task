package ru.safonov.test_task.facade;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.safonov.test_task.dto.error.NotValidTransferException;
import ru.safonov.test_task.dto.users.UserResponse;
import ru.safonov.test_task.mapper.UserMapper;
import ru.safonov.test_task.services.AccountService;
import ru.safonov.test_task.services.UserService;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class AccountFacade {
    private final AccountService accountService;
    private final UserService userService;
    private final UserMapper userMapper;

    public UserResponse transferFunds(Long senderId, Long recipientId, BigDecimal amount) {
        if (senderId.equals(recipientId))
            throw new NotValidTransferException("Нельзя передавать одинаковые id!");

        return userMapper.toResp(accountService.beginTransfer(senderId, recipientId, amount).getUser());

    }

}
