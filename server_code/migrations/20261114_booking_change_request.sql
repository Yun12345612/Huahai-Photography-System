CREATE TABLE IF NOT EXISTS booking_change_request (
 id BIGINT PRIMARY KEY AUTO_INCREMENT,
 booking_id BIGINT NOT NULL,
 request_type VARCHAR(20) NOT NULL,
 requested_date DATE NULL,
 requested_slot VARCHAR(40) NULL,
 reason VARCHAR(500) NULL,
 status VARCHAR(20) NOT NULL DEFAULT 'pending',
 admin_note VARCHAR(500) NULL,
 created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
 handled_at TIMESTAMP NULL,
 INDEX idx_booking_change_booking (booking_id),
 INDEX idx_booking_change_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
