package ru.safonov.test_task.util;

import lombok.Value;
import lombok.experimental.UtilityClass;
import org.springframework.validation.BindingResult;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@UtilityClass
public class ErrorUtil {

    public static List<ValidationError> returnAllErrors(BindingResult bindingResult) {
        Stream<ValidationError> fieldErrors = bindingResult.getFieldErrors().stream()
                .map(error -> new ValidationError(
                        error.getField(),
                        error.getDefaultMessage(),
                        "FIELD"
                ));
        Stream<ValidationError> globalErrors = bindingResult.getGlobalErrors().stream()
                .map(error -> new ValidationError(
                        null,
                        error.getDefaultMessage(),
                        "GLOBAL"
                ));

        return Stream.concat(fieldErrors, globalErrors)
                .collect(Collectors.toList());
    }

    @Value
    public static class ValidationError {
        String field;
        String message;
        String errorType;
    }
}
