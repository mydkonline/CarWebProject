-- SPIKE: schema mirrors the legacy MySQL views while the app runs locally on H2 for reproducible demos.
DROP VIEW IF EXISTS drive_schedule_view;
DROP VIEW IF EXISTS car_adminlist_view;
DROP VIEW IF EXISTS car_option_view;
DROP VIEW IF EXISTS car_list_view;
DROP VIEW IF EXISTS car_product_list_view;

DROP TABLE IF EXISTS schedule_drive;
DROP TABLE IF EXISTS kakaouserinfos;
DROP TABLE IF EXISTS centers;
DROP TABLE IF EXISTS car_options;
DROP TABLE IF EXISTS cars;
DROP TABLE IF EXISTS car_brands;
DROP TABLE IF EXISTS admin;

CREATE TABLE admin (
    id VARCHAR(64) PRIMARY KEY,
    username VARCHAR(80) NOT NULL UNIQUE,
    password VARCHAR(120) NOT NULL
);

CREATE TABLE car_brands (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(80) NOT NULL UNIQUE
);

CREATE TABLE cars (
    id INT AUTO_INCREMENT PRIMARY KEY,
    car_brand_id INT NOT NULL,
    name VARCHAR(120) NOT NULL,
    CONSTRAINT fk_cars_brand FOREIGN KEY (car_brand_id) REFERENCES car_brands(id)
);

CREATE TABLE car_options (
    id INT AUTO_INCREMENT PRIMARY KEY,
    car_id INT NOT NULL,
    color VARCHAR(120) NOT NULL,
    cc INT NOT NULL,
    km INT NOT NULL,
    price DECIMAL(12, 2) NOT NULL,
    grade VARCHAR(120) NOT NULL,
    CONSTRAINT fk_options_car FOREIGN KEY (car_id) REFERENCES cars(id)
);

CREATE TABLE centers (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(120) NOT NULL,
    address VARCHAR(255) NOT NULL,
    number VARCHAR(40) NOT NULL
);

CREATE TABLE kakaouserinfos (
    id BIGINT PRIMARY KEY,
    nickname VARCHAR(120) NOT NULL,
    connected_at TIMESTAMP NOT NULL
);

CREATE TABLE schedule_drive (
    id INT AUTO_INCREMENT PRIMARY KEY,
    center_id INT NOT NULL,
    kakaouser_id BIGINT NOT NULL,
    car_option_id INT NOT NULL,
    reservation_date DATE,
    state BOOLEAN NOT NULL DEFAULT FALSE,
    CONSTRAINT fk_schedule_center FOREIGN KEY (center_id) REFERENCES centers(id),
    CONSTRAINT fk_schedule_kakao FOREIGN KEY (kakaouser_id) REFERENCES kakaouserinfos(id),
    CONSTRAINT fk_schedule_option FOREIGN KEY (car_option_id) REFERENCES car_options(id)
);

CREATE VIEW car_product_list_view AS
SELECT
    c.id,
    c.name
FROM cars c;

CREATE VIEW car_list_view AS
SELECT
    c.id,
    b.name AS brand,
    c.name AS model
FROM cars c
JOIN car_brands b ON b.id = c.car_brand_id;

CREATE VIEW car_option_view AS
SELECT
    o.id,
    o.car_id,
    b.name AS brand,
    c.name AS model,
    o.color,
    o.cc,
    o.km,
    CAST(o.price AS VARCHAR) AS price,
    o.grade
FROM car_options o
JOIN cars c ON c.id = o.car_id
JOIN car_brands b ON b.id = c.car_brand_id;

CREATE VIEW car_adminlist_view AS
SELECT
    o.id,
    o.car_id,
    b.name AS brand,
    c.name AS model,
    o.color,
    o.cc,
    o.km,
    o.price,
    o.grade
FROM car_options o
JOIN cars c ON c.id = o.car_id
JOIN car_brands b ON b.id = c.car_brand_id;

CREATE VIEW drive_schedule_view AS
SELECT
    sd.id,
    o.id AS option_id,
    sd.reservation_date AS date,
    c.name AS model,
    ku.nickname,
    o.cc,
    o.color,
    o.grade,
    o.km,
    o.price,
    sd.state
FROM schedule_drive sd
JOIN kakaouserinfos ku ON ku.id = sd.kakaouser_id
JOIN car_options o ON o.id = sd.car_option_id
JOIN cars c ON c.id = o.car_id;
