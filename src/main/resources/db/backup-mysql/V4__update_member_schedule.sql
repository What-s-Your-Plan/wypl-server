-- Flyway migration script to add the write_review column to the member_schedule table
ALTER TABLE member_schedule
    ADD COLUMN write_review BOOLEAN NOT NULL DEFAULT FALSE;