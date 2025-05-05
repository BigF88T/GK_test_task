package ru.safonov.test_task.controller.error;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.safonov.test_task.dto.error.*;
import ru.safonov.test_task.util.ErrorUtil;

import javax.naming.AuthenticationException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class ControllerAdvice {

    @ExceptionHandler(value = {EntityNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    private ResponseEntity<EntityNotFoundErrorResponse> handleException(EntityNotFoundException e) {
        EntityNotFoundErrorResponse response = new EntityNotFoundErrorResponse(
                e.getMessage(),
                LocalDateTime.now().toString()
        );
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = {ConstraintViolationException.class, jakarta.validation.ConstraintViolationException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public List<ErrorUtil.ValidationError> handleMethodArgumentNotValidException(ConstraintViolationException e) {
        return e.getConstraintViolations().stream()
                .map(violation -> new ErrorUtil.ValidationError(
                        violation.getPropertyPath().toString(),
                        violation.getMessage(),
                        "FIELD"))
                .collect(Collectors.toList());
    }

    @ExceptionHandler(value = {NotValidTransferException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    private ResponseEntity<EntityNotFoundErrorResponse> handleException(NotValidTransferException e) {
        EntityNotFoundErrorResponse response = new EntityNotFoundErrorResponse(
                e.getMessage(),
                LocalDateTime.now().toString()
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {NotValidPhoneException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    private ResponseEntity<EntityNotFoundErrorResponse> handleException(NotValidPhoneException e) {
        EntityNotFoundErrorResponse response = new EntityNotFoundErrorResponse(
                e.getMessage(),
                LocalDateTime.now().toString()
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {NotValidEmailException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    private ResponseEntity<EntityNotFoundErrorResponse> handleException(NotValidEmailException e) {
        EntityNotFoundErrorResponse response = new EntityNotFoundErrorResponse(
                e.getMessage(),
                LocalDateTime.now().toString()
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(IllegalStateException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionBody handleIllegalState(IllegalStateException e) {
        return new ExceptionBody(e.getMessage());
    }

    @ExceptionHandler(AuthenticationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionBody handleAuthentication(AuthenticationException e){
        return new ExceptionBody("Аутентификация провалена! Упс....");
    }
}
