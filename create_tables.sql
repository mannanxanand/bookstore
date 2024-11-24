CREATE DATABASE IF NOT EXISTS bookstore;

USE bookstore;

-- Users table
CREATE TABLE IF NOT EXISTS users (
                                     id INT AUTO_INCREMENT PRIMARY KEY,
                                     username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role ENUM('Buyer', 'Seller', 'Admin') NOT NULL DEFAULT 'Buyer'
    );

-- Books table
CREATE TABLE IF NOT EXISTS books (
                                     id INT AUTO_INCREMENT PRIMARY KEY,
                                     title VARCHAR(255) NOT NULL,
    author VARCHAR(255),
    category VARCHAR(255),
    book_condition VARCHAR(50),
    original_price DECIMAL(10,2),
    calculated_price DECIMAL(10,2),
    notes TEXT,
    image_url VARCHAR(255),
    seller_id INT,
    FOREIGN KEY (seller_id) REFERENCES users(id)
    );