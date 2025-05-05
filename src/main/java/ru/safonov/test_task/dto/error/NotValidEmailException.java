package ru.safonov.test_task.dto.error;

public class NotValidEmailException extends RuntimeException{
    public NotValidEmailException(String msg) {
        super(msg);
    }
}
