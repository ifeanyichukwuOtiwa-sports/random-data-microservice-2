-- liquibase formatted sql
-- changeset ifeanyichukwuOtiwa:2023-24-11-init-schema


CREATE TABLE IF NOT EXISTS individual (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    age INTEGER NOT NULL,
    phone_number VARCHAR(255),
    gender VARCHAR(255) NOT NULL,
    nationality VARCHAR(255),
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP(),
    date_of_birth DATETIME NOT NULL
);