ALTER TABLE account
    DROP COLUMN IF EXISTS phone_number;

DROP INDEX IF EXISTS idx_account_phone_number;
