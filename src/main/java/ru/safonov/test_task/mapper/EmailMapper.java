package ru.safonov.test_task.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import ru.safonov.test_task.dto.emails.EmailRequest;
import ru.safonov.test_task.dto.emails.EmailResponse;
import ru.safonov.test_task.models.Email;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface EmailMapper {
    Email toEnt(EmailRequest dto);

    default EmailResponse toResp(Set<Email> emails) {
        return new EmailResponse(
                emails.stream()
                        .map(Email::getEmail)
                        .collect(Collectors.toSet())
        );
    }
}
