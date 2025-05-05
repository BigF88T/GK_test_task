package ru.safonov.test_task.util;

import lombok.experimental.UtilityClass;
import ru.safonov.test_task.dto.error.NotValidPhoneException;
import ru.safonov.test_task.dto.phones.PhoneRequest;

import java.util.Objects;

@UtilityClass
public class PhoneNumberValidation {
    String pattern = "^[78]\\d{10}$";

    public static void validate(PhoneRequest req) {
        String validatedNum = req.phoneNumber();

        if (Objects.isNull(validatedNum) || validatedNum.isEmpty())
            throw new NotValidPhoneException("Нельзя указывать пустой номер.");

        if(!validatedNum.matches(pattern))
            throw new NotValidPhoneException("Неправильный формат номера. Номер должен начинаться с 7 и состоять из 11 цифр.");

    }

}
