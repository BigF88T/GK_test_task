package ru.safonov.test_task.dto.error;

public class NotValidPhoneException extends RuntimeException{

    public NotValidPhoneException(String msg) {
        super(msg);
    }
}