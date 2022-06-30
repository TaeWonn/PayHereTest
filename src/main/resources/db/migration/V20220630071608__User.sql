CREATE TABLE IF NOT EXISTS user (
    id BIGINT NOT NULL AUTO_INCREMENT,
    email VARCHAR(50) NOT NULL,
    password VARCHAR(255) NOT NULL ,
    created_at DATETIME NOT NULL,
    mail_authentication BOOLEAN NOT NULL,
    PRIMARY KEY (id),
    UNIQUE KEY uk_email (email)
);