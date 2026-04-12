CREATE DATABASE apartment_db;
USE apartment_db;

CREATE TABLE visitors (
    vis_id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100),
    flat_no VARCHAR(10),
    purpose VARCHAR(100),
    entry_time DATETIME DEFAULT NOW(),
    exit_time DATETIME DEFAULT NULL
);
