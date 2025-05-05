package ru.safonov.test_task.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import ru.safonov.test_task.dto.phones.PhoneRequest;
import ru.safonov.test_task.dto.phones.PhoneResponse;
import ru.safonov.test_task.models.Phone;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface PhoneMapper {

    Phone toEnt(PhoneRequest dto);

    default PhoneResponse toResp(Set<Phone> phoneNumbers) {
        return new PhoneResponse(
                phoneNumbers.stream()
                        .map(Phone::getPhoneNumber)
                        .collect(Collectors.toSet())
        );
    }

}
