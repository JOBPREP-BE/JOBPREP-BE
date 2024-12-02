CREATE TABLE IF NOT EXISTS user_study (
                                          id	BIGINT	NOT NULL,
                                          user_id	BIGINT	NOT NULL,
                                          study_id	BIGINT	NOT NULL,
                                          PRIMARY KEY (id)
);
ALTER TABLE users DROP COLUMN study_id;
