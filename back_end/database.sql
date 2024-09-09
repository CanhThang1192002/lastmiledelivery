DROP DATABASE lastmiledelivery;
CREATE DATABASE lastmiledelivery;
USE lastmiledelivery;

-- Bảng Roles (Vai trò)
CREATE TABLE Roles (
    role_id INT PRIMARY KEY AUTO_INCREMENT,
    role_name VARCHAR(50) NOT NULL,
    description TEXT
);

-- Bảng Customers (Khách hàng)
CREATE TABLE Customers (
    customer_id INT PRIMARY KEY AUTO_INCREMENT,
	phone_number VARCHAR(20) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    role_id INT DEFAULT 2,
	email VARCHAR(100) UNIQUE,
    full_name VARCHAR(100) NOT NULL,
    gender ENUM('MALE', 'FEMALE', 'OTHER'),
    date_of_birth DATE,
    address VARCHAR(255) NOT NULL,
    google_account_id INT DEFAULT 0,
    is_active TINYINT(1) DEFAULT 1,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    is_delete TINYINT(1) DEFAULT(0) NOT NULL,
    FOREIGN KEY (role_id) REFERENCES Roles(role_id)
);

-- Bảng Shipper (Tài xế)
CREATE TABLE Shippers (
    shipper_id INT PRIMARY KEY AUTO_INCREMENT,
    phone_number VARCHAR(20) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    role_id INT DEFAULT 3,
	email VARCHAR(100) UNIQUE,
    full_name VARCHAR(100) NOT NULL,
    gender ENUM('MALE', 'FEMALE', 'OTHER'),
    date_of_birth DATE,
    address VARCHAR(255) NOT NULL,
    license_number VARCHAR(50) UNIQUE NOT NULL,
    vehicle_type VARCHAR(50),
    status ENUM('ONLINE', 'OFFLINE') DEFAULT 'OFFLINE',
    current_location POINT,
    is_active TINYINT(1) DEFAULT 1,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    is_delete TINYINT(1) DEFAULT(0) NOT NULL,
    FOREIGN KEY (role_id) REFERENCES Roles(role_id) 
);

CREATE TABLE CustomerTokens (
    token_id INT PRIMARY KEY AUTO_INCREMENT,
    customer_id INT,
    token VARCHAR(255) UNIQUE NOT NULL,
    token_type ENUM('ACCESS', 'REFRESH') NOT NULL,
    expires_date TIMESTAMP NOT NULL,
    revoked TINYINT(1) NOT NULL comment 'da thu hoi',
    refresh_token VARCHAR(255) UNIQUE NOT NULL,
    refresh_expires_date TIMESTAMP NOT NULL,
    FOREIGN KEY (customer_id) REFERENCES Customers(customer_id) 
);

-- Bảng UserTokens (Token người dùng)
CREATE TABLE ShipperTokens (
    token_id INT PRIMARY KEY AUTO_INCREMENT,
    shipper_id INT,
    token VARCHAR(255) UNIQUE NOT NULL,
    token_type ENUM('ACCESS', 'REFRESH') NOT NULL,
    expires_date TIMESTAMP NOT NULL,
    revoked TINYINT(1) NOT NULL comment 'da thu hoi',
    refresh_token VARCHAR(255) UNIQUE NOT NULL,
    refresh_expires_date TIMESTAMP NOT NULL,
    FOREIGN KEY (shipper_id) REFERENCES Shippers(shipper_id) 
);

-- BANG SOCIAL ACCOUNTS ho tro dang nhap tu google--soical_accounts
CREATE TABLE social_accounts (
    id INT PRIMARY KEY AUTO_INCREMENT,
    provider VARCHAR(20) NOT NULL COMMENT 'ten nha soical network vd:facebook, google',
    provider_id VARCHAR(50) NOT NULL,
    email VARCHAR(100) NOT NULL COMMENT 'email cua tai khoan',
    name VARCHAR(100) NOT NULL COMMENT 'ten cua tai khoan',
    customer_id INT,
    is_delete TINYINT(1) DEFAULT(0) NOT NULL,
    FOREIGN KEY (customer_id) REFERENCES Customers(customer_id)
);

-- Bảng Orders (Đơn hàng)
CREATE TABLE Orders (
    order_id INT PRIMARY KEY AUTO_INCREMENT,
    customer_id INT NOT NULL,
    status ENUM('PENDING','PROCESSING','SHIPPED','DELIVERED','CANCELED','RETURNED') NOT NULL DEFAULT 'PENDING',
    recipient VARCHAR(100) NOT NULL COMMENT 'ten nguoi nhan',
    recipient_phone VARCHAR(20) NOT NULL COMMENT 'sdt nguoi nhan',
    recipient_address VARCHAR(100) NOT NULL COMMENT 'dia chi nguoi nhan',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    is_delete TINYINT(1) DEFAULT(0) NOT NULL,
    FOREIGN KEY (customer_id) REFERENCES Customers(customer_id)
);

-- Bảng OrderItems (Chi tiết đơn hàng)
CREATE TABLE Order_details (
    item_id INT PRIMARY KEY AUTO_INCREMENT,
    order_id INT,
    product_name VARCHAR(100) NOT NULL,
    mass FLOAT NOT NULL COMMENT 'khoi luong',
    size FLOAT NOT NULL COMMENT 'kich thuoc',
    cod DECIMAL(10, 2) NOT NULL,
    shipping_fee DECIMAL(10, 2) NOT NULL,
    FOREIGN KEY (order_id) REFERENCES Orders(order_id)
);

-- Bảng DeliveryAssignments (Phân công giao hàng)
CREATE TABLE DeliveryAssignments (
    assignment_id INT PRIMARY KEY AUTO_INCREMENT,
    order_id INT,
    shipper_id INT,
    assigned_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    completed_at TIMESTAMP,
    status ENUM('ASSIGNED', 'IN_PROGRESS', 'COMPLETED', 'CANCELLED') DEFAULT 'ASSIGNED',
    is_delete TINYINT(1) DEFAULT(0) NOT NULL,
    FOREIGN KEY (order_id) REFERENCES Orders(order_id),
    FOREIGN KEY (shipper_id) REFERENCES Shippers(shipper_id)
);

-- Bảng SupportTickets (Hỗ trợ và khiếu nại)
CREATE TABLE SupportTickets (
    ticket_id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT,
    order_id INT,
    subject VARCHAR(100) NOT NULL,
    description TEXT,
    status ENUM('IN_PROGRESS', 'RESOLVED') DEFAULT 'IN_PROGRESS',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES Users(user_id),
    FOREIGN KEY (order_id) REFERENCES Orders(order_id)
);

-- Bảng Reports (Báo cáo)
CREATE TABLE Reports (
    report_id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT,
    report_type ENUM('ORDER', 'EARNINGS', 'PERFORMANCE') NOT NULL,
    report_data JSON,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES Users(user_id)
);


