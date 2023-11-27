-- liquibase formatted sql
-- changeset ifeanyichukwuOtiwa:2023-26-11_update_individual

ALTER TABLE individual
    ADD COLUMN title VARCHAR(20);

UPDATE individual
SET title =
        CASE
            WHEN age < 50 AND gender = 'female' THEN 'Miss'
            WHEN age >= 50 AND gender = 'female' THEN 'Mrs'
            ELSE 'Mr'  -- If none of the conditions are met, keep the existing title
            END;
