ALTER TABLE admins ADD COLUMN IF NOT EXISTS invite_code VARCHAR(16);
UPDATE admins SET invite_code = COALESCE(invite_code, 'xk201225');
