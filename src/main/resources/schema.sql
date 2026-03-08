-- Hotel Admin Database Initialization Script

-- Create database if not exists
CREATE DATABASE IF NOT EXISTS hotel_admin DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE hotel_admin;

-- Room Type Table
CREATE TABLE IF NOT EXISTS room_type (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    type_name VARCHAR(100) NOT NULL COMMENT '房间类型名称',
    price DECIMAL(10, 2) NOT NULL COMMENT '价格',
    capacity INT NOT NULL DEFAULT 1 COMMENT '容量(人数)',
    description TEXT COMMENT '描述',
    active TINYINT NOT NULL DEFAULT 1 COMMENT '是否有效: 0-无效, 1-有效',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_active (active)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='房间类型表';

-- Channel Table (Booking Channel)
CREATE TABLE IF NOT EXISTS channel (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    channel_name VARCHAR(100) NOT NULL COMMENT '渠道名称',
    channel_code VARCHAR(50) NOT NULL COMMENT '渠道编码',
    contact_person VARCHAR(50) COMMENT '联系人',
    contact_phone VARCHAR(20) COMMENT '联系电话',
    commission_rate DECIMAL(5, 2) NOT NULL DEFAULT 0.00 COMMENT '佣金比例(%)',
    active TINYINT NOT NULL DEFAULT 1 COMMENT '是否有效: 0-无效, 1-有效',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    UNIQUE KEY uk_channel_code (channel_code),
    INDEX idx_active (active)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='渠道表';

-- Booking Table
CREATE TABLE IF NOT EXISTS booking (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    booking_no VARCHAR(50) NOT NULL COMMENT '预订编号',
    guest_name VARCHAR(50) NOT NULL COMMENT '客人姓名',
    guest_phone VARCHAR(20) NOT NULL COMMENT '客人电话',
    id_card VARCHAR(18) NOT NULL COMMENT '身份证号',
    room_type_id BIGINT NOT NULL COMMENT '房间类型ID',
    channel_id BIGINT NOT NULL COMMENT '渠道ID',
    check_in_date DATE NOT NULL COMMENT '入住日期',
    check_out_date DATE NOT NULL COMMENT '退房日期',
    total_amount DECIMAL(10, 2) NOT NULL COMMENT '总金额',
    status TINYINT NOT NULL DEFAULT 0 COMMENT '状态: 0-待确认, 1-已确认, 2-已入住, 3-已完成, 4-已取消',
    remark TEXT COMMENT '备注',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    UNIQUE KEY uk_booking_no (booking_no),
    INDEX idx_room_type (room_type_id),
    INDEX idx_channel (channel_id),
    INDEX idx_status (status),
    INDEX idx_create_time (create_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='预订表';

-- Sample Data
INSERT INTO room_type (type_name, price, capacity, description) VALUES
('标准间', 299.00, 2, '舒适的标准间配备基本设施'),
('豪华间', 499.00, 2, '豪华装修，空间更大'),
('套房', 899.00, 4, '豪华套房配备客厅和厨房'),
('大床房', 359.00, 2, '配备大床的舒适房间');

INSERT INTO channel (channel_name, channel_code, contact_person, contact_phone, commission_rate) VALUES
('携程', 'CTRIP', '张经理', '13800138000', 10.00),
('美团', 'MEITUAN', '李经理', '13800138001', 8.00),
('飞猪', 'FLIGGY', '王经理', '13800138002', 9.50),
('官网直销', 'DIRECT', '客服', '400-888-8888', 0.00);

-- Room Table
CREATE TABLE IF NOT EXISTS room (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  room_no VARCHAR(32) NOT NULL,
  room_type_id BIGINT NOT NULL,
  is_hourly TINYINT NOT NULL DEFAULT 0,
  status TINYINT NOT NULL DEFAULT 0,
  active TINYINT NOT NULL DEFAULT 1,
  create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  UNIQUE KEY uk_room_no (room_no),
  KEY idx_room_type (room_type_id),
  KEY idx_room_active (active)
);

-- Room Price Table
CREATE TABLE IF NOT EXISTS room_price (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  room_type_id BIGINT NOT NULL,
  price_date DATE NOT NULL,
  price DECIMAL(10,2) NOT NULL,
  UNIQUE KEY uk_room_price (room_type_id, price_date)
);

-- Room Request Log Table
CREATE TABLE IF NOT EXISTS room_req_log (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  room_id BIGINT NOT NULL,
  req_date DATE NOT NULL,
  content VARCHAR(255) NOT NULL,
  create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  KEY idx_req_room_date (room_id, req_date)
);

-- Sample Room Data
INSERT INTO room (room_no, room_type_id, is_hourly, status) VALUES
('1001', 1, 0, 0),
('1002', 1, 0, 1),
('1003', 2, 0, 2),
('1004', 3, 1, 0),
('1005', 1, 0, 1),
('1006', 1, 0, 0),
('2001', 4, 1, 1),
('2002', 3, 0, 2),
('2003', 1, 0, 0),
('2004', 2, 0, 1),
('2005', 4, 1, 0),
('2006', 3, 0, 1);

-- System User Table
CREATE TABLE IF NOT EXISTS sys_user (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  username VARCHAR(50) NOT NULL,
  password_hash VARCHAR(100) NOT NULL,
  role VARCHAR(20) NOT NULL,
  active TINYINT NOT NULL DEFAULT 1,
  create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  UNIQUE KEY uk_username (username)
);

-- Init Users (幂等)
INSERT INTO sys_user (username, password_hash, role, active)
SELECT 'superadmin', '$2b$10$wvrb3mBhcEoRiGJ0DkG/1OXFf9dFUAjcJxZe9VTttVErL1oZGnP6a', 'SUPER_ADMIN', 1
WHERE NOT EXISTS (SELECT 1 FROM sys_user WHERE username = 'superadmin');

INSERT INTO sys_user (username, password_hash, role, active)
SELECT 'admin', '$2b$10$wvrb3mBhcEoRiGJ0DkG/1OXFf9dFUAjcJxZe9VTttVErL1oZGnP6a', 'ADMIN', 1
WHERE NOT EXISTS (SELECT 1 FROM sys_user WHERE username = 'admin');

INSERT INTO sys_user (username, password_hash, role, active)
SELECT 'user1', '$2b$10$wvrb3mBhcEoRiGJ0DkG/1OXFf9dFUAjcJxZe9VTttVErL1oZGnP6a', 'USER', 1
WHERE NOT EXISTS (SELECT 1 FROM sys_user WHERE username = 'user1');

-- Task Table
CREATE TABLE IF NOT EXISTS task (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  room_id BIGINT,
  title VARCHAR(100) NOT NULL,
  content TEXT,
  remark VARCHAR(255),
  status TINYINT NOT NULL DEFAULT 0 COMMENT '0-待处理 1-处理中 2-已完成',
  created_by VARCHAR(50) NOT NULL,
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  assigned_to VARCHAR(50),
  assigned_at DATETIME,
  done_by VARCHAR(50),
  done_at DATETIME,
  KEY idx_status (status),
  KEY idx_assigned_to (assigned_to),
  KEY idx_created_by (created_by)
);

-- Stay Table
CREATE TABLE IF NOT EXISTS stay (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  room_id BIGINT NOT NULL,
  channel_id BIGINT NULL,
  check_in_at DATETIME NOT NULL,
  expected_check_out_at DATETIME NOT NULL,
  check_out_at DATETIME NULL,
  status TINYINT NOT NULL DEFAULT 1, -- 1在住 2已退房
  remark VARCHAR(255) NULL,
  active TINYINT DEFAULT 1,
  create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  KEY idx_stay_room_status (room_id, status),
  KEY idx_stay_exp_out (expected_check_out_at)
);

-- Stay Guest Table
CREATE TABLE IF NOT EXISTS stay_guest (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  stay_id BIGINT NOT NULL,
  name VARCHAR(50) NOT NULL,
  gender TINYINT NOT NULL, -- 0女 1男
  id_card VARCHAR(18) NOT NULL,
  is_main TINYINT NOT NULL DEFAULT 1,
  active TINYINT DEFAULT 1,
  create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  KEY idx_guest_stay (stay_id),
  KEY idx_guest_idcard (id_card),
  KEY idx_guest_name (name)
);
