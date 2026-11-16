ALTER TABLE system_message ADD COLUMN reminder_key VARCHAR(200) NULL;
CREATE UNIQUE INDEX uk_system_message_reminder ON system_message(reminder_key);
