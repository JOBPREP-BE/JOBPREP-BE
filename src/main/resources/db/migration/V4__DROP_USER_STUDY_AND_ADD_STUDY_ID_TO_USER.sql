DROP TABLE user_study;
ALTER TABLE users
ADD COLUMN study_id BIGINT NOT NULL COMMENT '참여 스터디 ID'
