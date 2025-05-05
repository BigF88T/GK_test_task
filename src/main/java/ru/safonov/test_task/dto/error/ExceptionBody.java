package ru.safonov.test_task.dto.error;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Map;

/**
 * Класс ExceptionBody используется для передачи информации об исключении или ошибке.
 * Содержит сообщение об ошибке и, возможно, словарь ошибок, связанных с конкретными полями или параметрами.
 */
@Data
@AllArgsConstructor
public class ExceptionBody {
    /**
     * Сообщение об ошибке.
     */
    private String message;
    /**
     * Словарь ошибок, связанных с конкретными полями или параметрами.
     * Ключ словаря представляет имя поля или параметра, а значение - текстовое описание ошибки.
     */
    private Map<String,String> errors;

    /**
     * Конструктор класса ExceptionBody с параметрами.
     * @param message сообщение об ошибке.
     */
    public ExceptionBody(String message) {
        this.message = message;
    }
}