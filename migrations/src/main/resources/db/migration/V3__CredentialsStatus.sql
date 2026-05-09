CREATE TYPE credentials_status AS ENUM ('ACTIVE', 'SUSPENDED');

ALTER TABLE credentials
    ADD COLUMN IF NOT EXISTS status credentials_status NOT NULL DEFAULT 'ACTIVE',
    ADD COLUMN IF NOT EXISTS email_verified boolean NOT NULL DEFAULT false,
    ADD COLUMN IF NOT EXISTS phone_verified boolean NOT NULL DEFAULT false;
