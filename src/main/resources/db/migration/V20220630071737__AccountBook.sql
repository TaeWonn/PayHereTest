CREATE TABLE account_book (
    id BIGINT NOT NULL AUTO_INCREMENT,
    pay_date DATE NOT NULL,
    memo VARCHAR(300) NULL,
    created_at DATETIME NOT NULL,
    updated_at DATETIME NULL,
    user_id BIGINT NOT NULL ,
    PRIMARY KEY (id),
    FOREIGN KEY (user_id) REFERENCES user(id)
);

CREATE TABLE account_book_category (
    id BIGINT NOT NULL AUTO_INCREMENT,
    name VARCHAR(20) NOT NULL,
    depth INT NOT NULL,
    user_id BIGINT NULL,
    parent_id BIGINT NULL,
    deletable BOOLEAN NOT NULL,
    created_at DATETIME NOT NULL,
    updated_at DATETIME NULL,
    deleted_at DATETIME NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (user_id) REFERENCES user(id)
        ON DELETE NO ACTION,
    FOREIGN KEY (parent_id) REFERENCES account_book_category(id)
        ON DELETE CASCADE
        ON UPDATE NO ACTION
);

CREATE TABLE account_book_history(
    id BIGINT NOT NULL AUTO_INCREMENT,
    cash DECIMAL(16, 2) NOT NULL ,
    card DECIMAL(16, 2) NOT NULL,
    account_book_id BIGINT NOT NULL,
    account_book_category_id BIGINT NOT NULL,
    created_at DATETIME NOT NULL,
    updated_at DATETIME NULL,
    deleted_at DATETIME NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (account_book_id) REFERENCES account_book(id)
        ON DELETE NO ACTION,
    FOREIGN KEY (account_book_category_id) REFERENCES account_book_category(id)
        ON DELETE NO ACTION
);