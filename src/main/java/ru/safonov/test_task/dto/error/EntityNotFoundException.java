package ru.safonov.test_task.dto.error;

public class EntityNotFoundException extends RuntimeException {
    private final String entityType;

    public EntityNotFoundException(String entityType, String message) {
        super(message);
        this.entityType = entityType;
    }

    public String getEntityType() {
        return entityType;
    }
}
