CREATE TABLE IF NOT EXISTS booking_refund (
 id BIGINT PRIMARY KEY AUTO_INCREMENT, booking_id BIGINT NOT NULL,
 amount DECIMAL(12,2) NOT NULL, reason VARCHAR(500) NOT NULL,
 status VARCHAR(20) NOT NULL DEFAULT 'pending', admin_note VARCHAR(500) NULL,
 result_note VARCHAR(500) NULL, handled_by VARCHAR(200) NULL,
 created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, handled_at TIMESTAMP NULL, completed_at TIMESTAMP NULL,
 UNIQUE KEY uk_refund_booking(booking_id), INDEX idx_refund_status(status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
