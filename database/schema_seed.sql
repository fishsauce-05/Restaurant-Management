DROP DATABASE IF EXISTS restaurant_db;

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
    name NVARCHAR(100) NOT NULL,
    role VARCHAR(20) NOT NULL,
    phone VARCHAR(20) NULL,
    email VARCHAR(100) NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS tblClient (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name NVARCHAR(100) NOT NULL,
    phone VARCHAR(20) NULL,
    email VARCHAR(100) NULL,
    address VARCHAR(255) NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS tblTable (
    id INT AUTO_INCREMENT PRIMARY KEY,
    tableCode VARCHAR(20) NOT NULL UNIQUE,
    name NVARCHAR(100) NULL,
    capacity INT NOT NULL,
    description VARCHAR(255) NULL,
    status VARCHAR(50) NOT NULL,
    isActive TINYINT(1) NOT NULL DEFAULT 1
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS tblDish (
    id INT AUTO_INCREMENT PRIMARY KEY,
    dishCode VARCHAR(20) NOT NULL UNIQUE,
    category NVARCHAR(100) NULL,
    name NVARCHAR(100) NOT NULL,
    description NVARCHAR(255) NULL,
    price INT NOT NULL,
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
    price INT NULL,
    isCheckedIn TINYINT(1) NOT NULL DEFAULT 0,
    tblBookingId INT NOT NULL,
    tblTableId INT NOT NULL,
    CONSTRAINT FK_tblBookedTable_tblBooking FOREIGN KEY (tblBookingId) REFERENCES tblBooking(id),
    CONSTRAINT FK_tblBookedTable_tblTable FOREIGN KEY (tblTableId) REFERENCES tblTable(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS tblOrder (
    id INT AUTO_INCREMENT PRIMARY KEY,
    orderTime DATETIME NOT NULL,
    totalAmount INT NOT NULL DEFAULT 0,
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
    currentPrice INT NOT NULL,
    tblOrderId INT NOT NULL,
    tblDishId INT NOT NULL,
    CONSTRAINT FK_tblOrderDish_tblOrder FOREIGN KEY (tblOrderId) REFERENCES tblOrder(id),
    CONSTRAINT FK_tblOrderDish_tblDish FOREIGN KEY (tblDishId) REFERENCES tblDish(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS tblBill (
    id INT AUTO_INCREMENT PRIMARY KEY,
    createTime DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    totalAmount INT NOT NULL,
    paymentMethod VARCHAR(50) NULL,
    tblOrderId INT NOT NULL,
    tblUserId INT NOT NULL,
    CONSTRAINT FK_tblBill_tblOrder FOREIGN KEY (tblOrderId) REFERENCES tblOrder(id),
    CONSTRAINT FK_tblBill_tblUser FOREIGN KEY (tblUserId) REFERENCES tblUser(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;



INSERT INTO tblUser (userCode, username, password, name, role, phone, email, status)
SELECT 'NV001', 'admin', '123456', 'System Manager', 'MANAGER', '0911000001', 'admin@restaurant.local', 'ACTIVE'
WHERE NOT EXISTS (SELECT 1 FROM tblUser WHERE username = 'admin');

INSERT INTO tblUser (userCode, username, password, name, role, phone, email, status)
SELECT 'NV002', 'staff01', '123456', 'Floor Staff One', 'STAFF', '0911000002', 'staff01@restaurant.local', 'ACTIVE'
WHERE NOT EXISTS (SELECT 1 FROM tblUser WHERE username = 'staff01');

INSERT IGNORE INTO tblClient (name, phone, email, address, status)
VALUES
    ('Trần Thị Bích', '0912345678', 'bich.tran@example.com', 'KĐT Times City, Hai Bà Trưng, Hà Nội', 'ACTIVE'),
    ('Lê Hoàng Long', '0987654321', 'long.le@example.com', 'Quận 3, TP. Hồ Chí Minh', 'ACTIVE'),
    ('Phạm Văn Cường', '0909112233', 'cuong.pham@example.com', '15 Cầu Giấy, Hà Nội', 'ACTIVE'),
    ('Nguyễn Mai Phương', '0933445566', 'phuong.nguyen@example.com', 'Đống Đa, Hà Nội', 'ACTIVE'),
    ('Vũ Đức Trí', '0977889900', 'tri.vu@example.com', 'Hải Châu, Đà Nẵng', 'ACTIVE'),
    ('Hoàng Tùng Anh', '0868123123', 'tunghoang@example.com', 'KĐT Vinhomes Ocean Park, Gia Lâm', 'ACTIVE'),
    ('Đinh Thu Hà', '0888999777', 'hathu.dinh@example.com', 'Quận 1, TP. Hồ Chí Minh', 'ACTIVE'),
    ('Ngô Bảo Châu', '0945678901', 'chau.ngo@example.com', 'Ba Đình, Hà Nội', 'ACTIVE'),
    ('Đoàn Khắc Việt', '0922334455', 'viet.doan@example.com', 'Thanh Xuân, Hà Nội', 'INACTIVE'),
    ('Lý Mạc Sầu', '0966554433', 'sau.ly@example.com', 'Tây Hồ, Hà Nội', 'ACTIVE');

INSERT IGNORE INTO tblTable (tableCode, name, capacity, description, status, isActive)
VALUES
    ('T001', 'Window table', 2, 'Bàn cạnh cửa sổ', 'Trống', 1),
    ('T002', 'Family table', 4, 'Khu vực gia đình', 'Trống', 1),
    ('T003', 'Large table', 6, 'Nhóm lớn', 'Trống', 1),
    ('T004', 'Bàn đôi Ban công', 2, 'Khu vực ban công tầng 2, view đẹp', 'Trống', 1),
    ('T005', 'Bàn đôi Ban công', 2, 'Khu vực ban công tầng 2, view đẹp', 'Trống', 1),
    ('T006', 'Bàn tròn Tầng 1', 6, 'Khu vực sảnh chính tầng 1', 'Trống', 1),
    ('T007', 'Bàn tròn Tầng 1', 6, 'Khu vực sảnh chính tầng 1', 'Đang dọn', 1),
    ('T008', 'Bàn dài Gia đình', 8, 'Khu vực Tầng 2, yên tĩnh', 'Trống', 1),
    ('T009', 'Phòng VIP 1', 10, 'Phòng kín, có điều hòa, cách âm', 'Trống', 1),
    ('T010', 'Phòng VIP 2', 12, 'Phòng kín, máy chiếu, phù hợp liên hoan', 'Trống', 1),
    ('T011', 'Bàn Sân vườn 1', 4, 'Khu vực ngoài trời, không hút thuốc', 'Trống', 1),
    ('T012', 'Bàn Sân vườn 2', 4, 'Khu vực ngoài trời, cho phép hút thuốc', 'Trống', 1),
    ('T013', 'Bàn góc Tầng 1', 4, 'Góc khuất, riêng tư', 'Bảo trì', 0);

INSERT IGNORE INTO tblDish (dishCode, category, name, description, price, status, isActive)
VALUES
    ('D001', 'Main', 'Cơm rang', NULL, 45000, 'active', 1),
    ('D002', 'Main', 'Mì xào bò', NULL, 60000, 'active', 1),
    ('D003', 'Drink', 'Trà đá', NULL, 10000, 'active', 1),
    ('D004', 'Dessert', 'Bán bông lan', NULL, 25000, 'active', 1),
    -- Khai vị
    ('D005', 'Appetizer', 'Súp cua tuyết nhĩ', 'Súp cua nấu với nấm tuyết, thịt gà xé', 45000, 'active', 1),
    ('D006', 'Appetizer', 'Nộm ngó sen tôm thịt', 'Ngó sen giòn, tôm sú, thịt ba chỉ, đậu phộng', 85000, 'active', 1),
    ('D007', 'Appetizer', 'Khoai tây chiên bơ tỏi', 'Khoai tây cắt lát chiên giòn sốt bơ tỏi', 40000, 'active', 1),
    
    -- Món chính
    ('D008', 'Main', 'Gà nướng mật ong', '1/2 con gà nướng than hoa, phết mật ong rừng', 150000, 'active', 1),
    ('D009', 'Main', 'Bò lúc lắc khoai tây', 'Thịt bò Úc thái hạt lựu xào ớt chuông', 180000, 'active', 1),
    ('D010', 'Main', 'Cá chép om dưa', 'Cá chép nguyên con om dưa chua, thịt ba chỉ', 220000, 'active', 1),
    ('D011', 'Main', 'Mực ống hấp gừng', 'Mực ống tươi hấp hành gừng tươi', 160000, 'active', 1),
    ('D012', 'Main', 'Cơm chiên hải sản', 'Cơm rang với tôm, mực, chả lụa và đậu cô ve', 75000, 'active', 1),
    
    -- Lẩu
    ('D013', 'Hotpot', 'Lẩu Thái Tomyum (Nhỏ)', 'Lẩu vị chua cay, kèm hải sản tôm mực ngao', 250000, 'active', 1),
    ('D014', 'Hotpot', 'Lẩu bò nhúng dấm (Lớn)', 'Bò bắp, bò gân nhúng dấm chua thanh, cuốn bánh tráng', 350000, 'active', 1),
    
    -- Đồ uống
    ('D015', 'Drink', 'Nước ép dưa hấu', 'Nước ép dưa hấu tươi không đường', 35000, 'active', 1),
    ('D016', 'Drink', 'Sinh tố bơ', 'Bơ sáp Mộc Châu xay nhuyễn với sữa đặc', 45000, 'active', 1),
    ('D017', 'Drink', 'Bia Heineken', 'Bia Heineken lon 330ml', 25000, 'active', 1),
    ('D018', 'Drink', 'Nước suối Aquafina', 'Nước tinh khiết 500ml', 15000, 'active', 1),
    
    -- Tráng miệng
    ('D019', 'Dessert', 'Chè khúc bạch', 'Chè khúc bạch nhãn nhục, hạnh nhân xắt lát', 35000, 'active', 1),
    ('D020', 'Dessert', 'Đĩa trái cây theo mùa', 'Dưa hấu, xoài, ổi, nho mỹ', 80000, 'active', 1),
    ('D021', 'Dessert', 'Kem Vani', '2 viên kem Vani phủ sốt chocolate', 30000, 'out_of_stock', 1);