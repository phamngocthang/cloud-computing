CREATE DATABASE REGISTER_SUBJECT CHARACTER SET utf8mb4 COLLATE utf8mb4_vietnamese_ci;

USE REGISTER_SUBJECT;

SET NAMES utf8mb4;

CREATE TABLE PERSISTENT_LOGIN
(
    username  VARCHAR(64)                           NOT NULL,
    series    VARCHAR(64)                           NOT NULL PRIMARY KEY,
    token     VARCHAR(64)                           NOT NULL,
    last_used TIMESTAMP DEFAULT current_timestamp() NOT NULL ON UPDATE current_timestamp()
);

CREATE table ROLE
(
    id          INT AUTO_INCREMENT PRIMARY KEY,
    description VARCHAR(400) NOT NULL,
    name        VARCHAR(40)  NOT NULL,
    CONSTRAINT UK_ofx66keruapi6vyqpv6f2or37 UNIQUE (name)
);

CREATE TABLE SUBJECT
(
    id               INT AUTO_INCREMENT PRIMARY KEY,
    category_subject VARCHAR(128) NOT NULL,
    name_subject     VARCHAR(128) NOT NULL,
    total_credits    INT          NULL
);

create table USER
(
    id       INT AUTO_INCREMENT PRIMARY KEY,
    class    VARCHAR(45)  NULL,
    enabled  BIT          NOT NULL,
    name     VARCHAR(45)  NULL,
    password VARCHAR(64)  NOT NULL,
    username VARCHAR(128) NOT NULL
);

create table REGISTER
(
    user_id    INT NOT NULL,
    subject_id INT NOT NULL,
    PRIMARY KEY (user_id, subject_id),
    CONSTRAINT FK9ufm9ox1x9ojcsopq3b2uw1fh FOREIGN KEY (user_id) REFERENCES USER (id),
    CONSTRAINT FKlwefj8rpoiedof7k5oh3393ao FOREIGN KEY (subject_id) REFERENCES SUBJECT (id)
);

create table USER_ROLE
(
    user_id INT NOT NULL,
    role_id INT NOT NULL,
    PRIMARY KEY (user_id, role_id),
    CONSTRAINT FKj6m8fwv7oqv74fcehir1a9ffy FOREIGN key (role_id) REFERENCES ROLE (id),
    CONSTRAINT FKml90kef4w2jy7oxyqv742tsfc FOREIGN key (user_id) REFERENCES USER (id)
);


INSERT INTO REGISTER_SUBJECT.ROLE (id, description, name)
VALUES (1, 'abc', 'ROLE_ADMIN');
INSERT INTO REGISTER_SUBJECT.ROLE (id, description, name)
VALUES (2, 'abc', 'ROLE_USER');

INSERT INTO REGISTER_SUBJECT.USER (id, class, enabled, name, password, username)
VALUES (1, 'abc
abcd', true, 'Bao1', '$2a$10$PrI5Gk9L.tSZiW9FXhTS8O8Mz9E97k2FZbFvGFFaSsiTUIl.TCrFu', 'dbadmin1');
INSERT INTO REGISTER_SUBJECT.USER (id, class, enabled, name, password, username)
VALUES (2, 'abc
abcd', true, 'Bao222', '$2a$10$PrI5Gk9L.tSZiW9FXhTS8O8Mz9E97k2FZbFvGFFaSsiTUIl.TCrFu', 'dbuser1');

INSERT INTO REGISTER_SUBJECT.SUBJECT (id, category_subject, name_subject, total_credits)
VALUES (1, N'Đại cương', N'Vật lí 1', 3);
INSERT INTO REGISTER_SUBJECT.SUBJECT (id, category_subject, name_subject, total_credits)
VALUES (2, N'Đại cương', N'Toán 1', 3);
INSERT INTO REGISTER_SUBJECT.SUBJECT (id, category_subject, name_subject, total_credits)
VALUES (3, N'Đại cương ', N'Toán 2', 3);
INSERT INTO REGISTER_SUBJECT.SUBJECT (id, category_subject, name_subject, total_credits)
VALUES (4, N'Đại cương', N'Xác suất thống kê - ứng dụng', 3);
INSERT INTO REGISTER_SUBJECT.SUBJECT (id, category_subject, name_subject, total_credits)
VALUES (5, N'Chuyên ngành', N'Nhập môn lập trình ', 3);
INSERT INTO REGISTER_SUBJECT.SUBJECT (id, category_subject, name_subject, total_credits)
VALUES (6, N'Chuyên ngành', N'Kỹ thuật lập trình', 3);
INSERT INTO REGISTER_SUBJECT.SUBJECT (id, category_subject, name_subject, total_credits)
VALUES (7, N'Chuyên ngành ', N'Nhập môn ngành CNTT', 3);
INSERT INTO REGISTER_SUBJECT.SUBJECT (id, category_subject, name_subject, total_credits)
VALUES (8, N'Chuyên ngành', N'Cơ sở dữ liệu ', 3);
INSERT INTO REGISTER_SUBJECT.SUBJECT (id, category_subject, name_subject, total_credits)
VALUES (9, N'Chuyên ngành', N'An toàn thông tin', 3);
INSERT INTO REGISTER_SUBJECT.SUBJECT (id, category_subject, name_subject, total_credits)
VALUES (10, N'Chuyên ngành', N'Hệ quản trị CSDL', 3);


INSERT INTO REGISTER_SUBJECT.REGISTER (user_id, subject_id)
VALUES (2, 1);
INSERT INTO REGISTER_SUBJECT.REGISTER (user_id, subject_id)
VALUES (2, 2);
INSERT INTO REGISTER_SUBJECT.REGISTER (user_id, subject_id)
VALUES (2, 4);
INSERT INTO REGISTER_SUBJECT.REGISTER (user_id, subject_id)
VALUES (2, 5);
INSERT INTO REGISTER_SUBJECT.REGISTER (user_id, subject_id)
VALUES (2, 8);

INSERT INTO REGISTER_SUBJECT.USER_ROLE (user_id, role_id)
VALUES (1, 1);
INSERT INTO REGISTER_SUBJECT.USER_ROLE (user_id, role_id)
VALUES (1, 2);
INSERT INTO REGISTER_SUBJECT.USER_ROLE (user_id, role_id)
VALUES (2, 2);

INSERT INTO REGISTER_SUBJECT.PERSISTENT_LOGIN (username, series, token, last_used)
VALUES ('dbuser1', '3nHvHc6jyvXb0QseFxohnQ==', '/8fpGZBgoKjzqnZf4O0TWQ==', '2021-11-25 15:21:03');
INSERT INTO REGISTER_SUBJECT.PERSISTENT_LOGIN (username, series, token, last_used)
VALUES ('dbuser1', '9TMxKcUUvBFGg9WZFG0LUg==', 'GNGqT5VIpiIXxD8ms3kCsw==', '2021-11-25 15:21:03');