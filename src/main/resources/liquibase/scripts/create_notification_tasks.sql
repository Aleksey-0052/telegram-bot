-- liquibase formatted sql

-- changeset semenikhin:create_notification_tasks
CREATE TABLE notification_tasks (
    id SERIAL PRIMARY KEY,
    chat_id BIGINT NOT NULL,
    message TEXT NOT NULL,
    notification_date_time TIMESTAMP NOT NULL
);

