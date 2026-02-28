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
