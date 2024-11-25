CREATE TABLE IF NOT EXISTS application_status (
                                      id    BIGINT	NOT NULL,
                                      user_id   BIGINT	NOT NULL,
                                      company	VARCHAR(32)	NULL,
                                      position	VARCHAR(32)	NULL,
                                      application_progress	ENUM('진행 전','진행 중','합격','탈락')	NOT NULL	DEFAULT '진행 전',
                                      application_process	ENUM('서류 전형','인적성/코테','1차 면접','2차면접')	NOT NULL	DEFAULT '서류 전형',
                                      application_date	TIMESTAMP	NULL,
                                      due_date	TIMESTAMP	NULL,
                                      company_link	VARCHAR(200)	NULL,
                                      cover_letter	TEXT	NULL	COMMENT '작성 용도가 아닌, 저장 용도',
                                      PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS job_interview (
                                 id    BIGINT	NOT NULL,
                                 user_id	BIGINT	NOT NULL,
                                 question	VARCHAR(255)	NULL,
                                 category	ENUM('역량','인성')	NOT NULL	DEFAULT '인성',
                                 answer 	TEXT	NULL,
                                 PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS experience_master_cl (
                                        id	BIGINT	NOT NULL,
                                        user_id	BIGINT	NOT NULL,
                                        material	VARCHAR(100)	NULL,
                                        emphasis	VARCHAR(100)	NULL,
                                        exp_anal_process	ENUM('진행 전','진행 중','완료')	NOT NULL	DEFAULT '진행 전',
                                        master_cl_process	ENUM('진행 전','진행 중','완료')	NOT NULL	DEFAULT '진행 전',
                                        exp_anal	TEXT	NULL,
                                        master_cl	TEXT	NULL,
                                        active	TINYINT(1)	NOT NULL	DEFAULT 1	COMMENT '삭제해도 끝까지 가지고 있기',
                                        PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS essential_material (
                                      id	BIGINT	NOT NULL,
                                      user_id	BIGINT	NOT NULL,
                                      essential_material	TEXT	NOT NULL,
                                      PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS study (
                         id	BIGINT	NOT NULL,
                         study_name	VARCHAR(20)	NOT NULL,
                         position	ENUM('상관없음','개발','디자인','기획','마케팅','영업','재무/회계','인사')	NOT NULL	DEFAULT '상관없음',
                         study_status	ENUM('진행중','모집중','모집완료')	NOT NULL	DEFAULT '모집중',
                         head_count	SMALLINT	NOT NULL	DEFAULT 3,
                         duration_weeks	SMALLINT	NOT NULL	DEFAULT 3,
                         google_link	VARCHAR(200)	NULL,
                         discord_link	VARCHAR(200)	NULL,
                         kakao_link	VARCHAR(200)	NULL,
                         created_at	TIMESTAMP	NOT NULL,
                         deleted_at	TIMESTAMP	NULL	COMMENT 'soft delete',
                         PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS user_study (
                              id	BIGINT	NOT NULL,
                              user_id	BIGINT	NOT NULL,
                              study_id	BIGINT	NOT NULL,
                              PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS users (
                         id	BIGINT	NOT NULL,
                         username	VARCHAR(32)	NOT NULL,
                         email	VARCHAR(32)	NOT NULL	COMMENT 'UNIQUE 유저 식별 값',
                         user_role	ENUM('ADMIN','NORMAL')	NOT NULL	DEFAULT 'NORMAL'	COMMENT 'NORMAL, ADMIN',
                         penalty_updated_at	TIMESTAMP	NULL,
                         created_at	TIMESTAMP	NOT NULL,
                         deleted_at	TIMESTAMP	NULL	COMMENT 'soft delete',
                         PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS study_schedule (
                                  id	BIGINT	NOT NULL,
                                  study_id	BIGINT	NOT NULL,
                                  start_date	TIMESTAMP	NULL	COMMENT '1주차 변경 불가',
                                  week_number	SMALLINT	NOT NULL	COMMENT '최대 duration_weeks만큼 생성 가능',
                                  PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS help_chat_rooms (
                                   id	BIGINT	NOT NULL,
                                   user_id	BIGINT	NOT NULL,
                                   admin_id	BIGINT	NOT NULL,
                                   created_at	TIMESTAMP	NOT NULL,
                                   updated_at	TIMESTAMP	NOT NULL,
                                   status	ENUM('활성화', '비활성화')	NOT NULL	DEFAULT '활성화',
                                   PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS chat_messages (
                                 id	BIGINT	NOT NULL,
                                 chat_room_id	BIGINT	NOT NULL,
                                 sender_id	BIGINT	NOT NULL,
                                 sender_type	ENUM('NORMAL', 'ADMIN')	NOT NULL,
                                 message	TEXT	NOT NULL,
                                 created_at	TIMESTAMP	NOT NULL,
                                 is_read	TINYINT(1)	NOT NULL	DEFAULT 1,
                                 PRIMARY KEY (id)
);
