-- Migration script to update the column `day_of_week` from BINARY(1) to INT
ALTER TABLE repetition
    MODIFY COLUMN day_of_week INT;