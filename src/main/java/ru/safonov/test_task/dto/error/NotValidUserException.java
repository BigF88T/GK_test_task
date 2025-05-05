package ru.safonov.test_task.dto.error;

public class NotValidUserException extends RuntimeException{
    public NotValidUserException(String msg) {
        super(msg);
    }
}
