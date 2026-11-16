CREATE TABLE IF NOT EXISTS operation_log (
 id BIGINT PRIMARY KEY AUTO_INCREMENT,
 operator_type VARCHAR(32) NOT NULL,
 operator_account VARCHAR(100) NOT NULL,
 action VARCHAR(100) NOT NULL,
 target_type VARCHAR(50), target_id BIGINT, detail VARCHAR(1000),
 created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
 INDEX idx_operation_log_created(created_at), INDEX idx_operation_log_action(action)
);
