package ru.safonov.test_task.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import ru.safonov.test_task.dto.users.FoundedUsersResponse;
import ru.safonov.test_task.dto.users.UserRegisterRequest;
import ru.safonov.test_task.dto.users.UserRegisterResponse;
import ru.safonov.test_task.dto.users.UserResponse;
import ru.safonov.test_task.models.Email;
import ru.safonov.test_task.models.Phone;
import ru.safonov.test_task.models.User;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {

    User toEnt(UserRegisterRequest dto);

    UserRegisterResponse toRegResp(User user);

    default UserResponse toResp(User user) {
        return new UserResponse(
                user.getId(),
                user.getName(),
                user.getDateOfBirth().toString(),
                user.getEmails()
                        .stream()
                        .map(Email::getEmail)
                        .collect(Collectors.toSet()),
                user.getPhoneNumbers().stream()
                        .map(Phone::getPhoneNumber)
                        .collect(Collectors.toSet()),
                user.getAccount().getBalance()
        );
    }

    default List<UserResponse> toResp(List<User> users) {
        return users.stream()
                .map(user -> new UserResponse(
                        user.getId(),
                        user.getName(),
                        user.getDateOfBirth().toString(),
                        user.getEmails()
                                .stream()
                                .map(Email::getEmail)
                                .collect(Collectors.toSet()),
                        user.getPhoneNumbers().stream()
                                .map(Phone::getPhoneNumber)
                                .collect(Collectors.toSet()),
                        user.getAccount().getBalance()
                ))
                .collect(Collectors.toList());
    }
}
