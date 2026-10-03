-- Insert Root Admin User
INSERT INTO "users" ("id", "email", "passwordHash", "name", "role", "status", "updatedAt")
VALUES (
    'admin-root-om-001',
    'omspradippatil@gmail.com',
    '$2a$12$7/umxC5rdp0eJtvLuCYHJeNZJgvtTU/hQV1W2hccVOtepn6.IlHYu',
    'om',
    'ADMIN',
    'ACTIVE',
    CURRENT_TIMESTAMP
);
