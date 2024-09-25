
-- Drop the existing user_role column if it exists
ALTER TABLE user
DROP COLUMN user_role;

ALTER TABLE user
ADD COLUMN user_role ENUM('ROLE_VENDOR', 'ROLE_ADMIN', 'ROLE_CUSTOMER') NOT NULL;