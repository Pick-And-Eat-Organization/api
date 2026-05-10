ALTER TABLE credentials
    ADD COLUMN IF NOT EXISTS phone_number VARCHAR(20);

UPDATE credentials c
SET phone_number = a.phone_number
FROM account a
WHERE a.credentials_id = c.credentials_id
  AND c.phone_number IS NULL;

ALTER TABLE credentials
    ALTER COLUMN phone_number SET NOT NULL;

CREATE UNIQUE INDEX IF NOT EXISTS uq_credentials_phone_number ON credentials(phone_number);
