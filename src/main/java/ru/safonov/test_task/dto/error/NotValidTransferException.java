package ru.safonov.test_task.dto.error;

public class NotValidTransferException extends RuntimeException {
    public NotValidTransferException(String msg) {
        super(msg);
    }
}
