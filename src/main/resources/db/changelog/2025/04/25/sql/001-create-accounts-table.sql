CREATE TABLE IF NOT EXISTS gk_task.accounts (
                                                id BIGINT PRIMARY KEY GENERATED BY DEFAULT AS IDENTITY,
                                                user_id BIGINT UNIQUE REFERENCES gk_task.users(id),
                                                balance DECIMAL NOT NULL DEFAULT 0.0
                                            );