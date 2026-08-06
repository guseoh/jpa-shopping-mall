CREATE TABLE members
(
    id         BIGINT       NOT NULL AUTO_INCREMENT,
    name       VARCHAR(50)  NOT NULL,
    email      VARCHAR(100) NOT NULL,
    created_at DATETIME(6)  NOT NULL,

    CONSTRAINT pk_members PRIMARY KEY (id),
    CONSTRAINT uk_members_email UNIQUE (email)
);