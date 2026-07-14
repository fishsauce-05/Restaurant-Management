CREATE DATABASE IF NOT EXISTS restaurant_db
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE restaurant_db;
SET NAMES utf8mb4;

CREATE TABLE IF NOT EXISTS tblUser (
    id INT AUTO_INCREMENT PRIMARY KEY,
    userCode VARCHAR(20) NULL UNIQUE,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    name VARCHAR(100) NOT NULL,
    role VARCHAR(20) NOT NULL,
    phone VARCHAR(20) NULL,
    email VARCHAR(100) NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS tblClient (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    phone VARCHAR(20) NULL,
    email VARCHAR(100) NULL,
    address VARCHAR(255) NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS tblTable (
    id INT AUTO_INCREMENT PRIMARY KEY,
    tableCode VARCHAR(20) NOT NULL UNIQUE,
    name VARCHAR(100) NULL,
    capacity INT NOT NULL,
    description VARCHAR(255) NULL,
    status VARCHAR(50) NOT NULL,
    isActive TINYINT(1) NOT NULL DEFAULT 1
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS tblDish (
    id INT AUTO_INCREMENT PRIMARY KEY,
    dishCode VARCHAR(20) NOT NULL UNIQUE,
    category VARCHAR(100) NULL,
    name VARCHAR(100) NOT NULL,
    description VARCHAR(255) NULL,
    price DOUBLE NOT NULL,
    status VARCHAR(50) NOT NULL DEFAULT 'active',
    isActive TINYINT(1) NOT NULL DEFAULT 1
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS tblBooking (
    id INT AUTO_INCREMENT PRIMARY KEY,
    bookDate DATE NOT NULL,
    bookTime VARCHAR(20) NOT NULL,
    quantity INT NOT NULL,
    status VARCHAR(50) NOT NULL,
    tblClientId INT NOT NULL,
    tblUserId INT NOT NULL,
    CONSTRAINT FK_tblBooking_tblClient FOREIGN KEY (tblClientId) REFERENCES tblClient(id),
    CONSTRAINT FK_tblBooking_tblUser FOREIGN KEY (tblUserId) REFERENCES tblUser(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS tblBookedTable (
    id INT AUTO_INCREMENT PRIMARY KEY,
    checkIn DATETIME NULL,
    checkOut DATETIME NULL,
    price DOUBLE NULL,
    isCheckedIn TINYINT(1) NOT NULL DEFAULT 0,
    tblBookingId INT NOT NULL,
    tblTableId INT NOT NULL,
    CONSTRAINT FK_tblBookedTable_tblBooking FOREIGN KEY (tblBookingId) REFERENCES tblBooking(id),
    CONSTRAINT FK_tblBookedTable_tblTable FOREIGN KEY (tblTableId) REFERENCES tblTable(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS tblOrder (
    id INT AUTO_INCREMENT PRIMARY KEY,
    orderTime DATETIME NOT NULL,
    totalAmount DOUBLE NOT NULL DEFAULT 0,
    status VARCHAR(50) NOT NULL DEFAULT 'Chưa thanh toán',
    isPaid TINYINT(1) NOT NULL DEFAULT 0,
    tblUserId INT NOT NULL,
    tblTableId INT NOT NULL,
    CONSTRAINT FK_tblOrder_tblUser FOREIGN KEY (tblUserId) REFERENCES tblUser(id),
    CONSTRAINT FK_tblOrder_tblTable FOREIGN KEY (tblTableId) REFERENCES tblTable(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS tblOrderDish (
    id INT AUTO_INCREMENT PRIMARY KEY,
    quantity INT NOT NULL,
    currentPrice DOUBLE NOT NULL,
    tblOrderId INT NOT NULL,
    tblDishId INT NOT NULL,
    CONSTRAINT FK_tblOrderDish_tblOrder FOREIGN KEY (tblOrderId) REFERENCES tblOrder(id),
    CONSTRAINT FK_tblOrderDish_tblDish FOREIGN KEY (tblDishId) REFERENCES tblDish(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS tblBill (
    id INT AUTO_INCREMENT PRIMARY KEY,
    createTime DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    totalAmount DOUBLE NOT NULL,
    paymentMethod VARCHAR(50) NULL,
    tblOrderId INT NOT NULL,
    tblUserId INT NOT NULL,
    CONSTRAINT FK_tblBill_tblOrder FOREIGN KEY (tblOrderId) REFERENCES tblOrder(id),
    CONSTRAINT FK_tblBill_tblUser FOREIGN KEY (tblUserId) REFERENCES tblUser(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE OR REPLACE VIEW tblDishStat AS
SELECT
    d.id,
    d.dishCode,
    d.category,
    d.name,
    d.price,
    COALESCE(SUM(od.quantity), 0) AS totalQuantity,
    COALESCE(SUM(od.quantity * od.currentPrice), 0) AS totalRevenue
FROM tblDish d
LEFT JOIN tblOrderDish od ON od.tblDishId = d.id
GROUP BY d.id, d.dishCode, d.category, d.name, d.price;

INSERT INTO tblUser (userCode, username, password, name, role, phone, email, status)
SELECT 'NV001', 'admin', '123456', 'System Manager', 'MANAGER', '0911000001', 'admin@restaurant.local', 'ACTIVE'
WHERE NOT EXISTS (SELECT 1 FROM tblUser WHERE username = 'admin');

INSERT INTO tblUser (userCode, username, password, name, role, phone, email, status)
SELECT 'NV002', 'staff01', '123456', 'Floor Staff One', 'STAFF', '0911000002', 'staff01@restaurant.local', 'ACTIVE'
WHERE NOT EXISTS (SELECT 1 FROM tblUser WHERE username = 'staff01');

INSERT INTO tblClient (name, phone, email, address, status)
SELECT 'Walk-in Customer', '0900000001', 'walkin@example.com', 'Restaurant counter', 'ACTIVE'
WHERE NOT EXISTS (SELECT 1 FROM tblClient WHERE phone = '0900000001');

INSERT INTO tblClient (name, phone, email, address, status)
SELECT 'Nguyen Van A', '0900000002', 'a.nguyen@example.com', '12 Le Loi, District 1', 'ACTIVE'
WHERE NOT EXISTS (SELECT 1 FROM tblClient WHERE phone = '0900000002');

INSERT IGNORE INTO tblTable (tableCode, name, capacity, description, status, isActive)
VALUES
    ('T001', 'Window table', 2, 'Near window', 'Trống', 1),
    ('T002', 'Family table', 4, 'Family area', 'Trống', 1),
    ('T003', 'Large table', 6, 'Large group', 'Trống', 1),
    ('T004', 'Occupied demo table', 4, 'Demo serving table', 'Đang phục vụ', 1);

INSERT IGNORE INTO tblDish (dishCode, category, name, description, price, status, isActive)
VALUES
    ('D001', 'Main', 'Fried rice', NULL, 45000, 'active', 1),
    ('D002', 'Main', 'Beef noodle', NULL, 60000, 'active', 1),
    ('D003', 'Drink', 'Iced tea', NULL, 10000, 'active', 1),
    ('D004', 'Dessert', 'Flan', NULL, 25000, 'active', 1);
