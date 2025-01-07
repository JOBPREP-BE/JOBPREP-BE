CREATE TABLE IF NOT EXISTS application_status (
    id    BIGINT	NOT NULL    AUTO_INCREMENT,
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
    id    BIGINT	NOT NULL    AUTO_INCREMENT,
    user_id	BIGINT	NOT NULL,
    question	VARCHAR(255)	NULL,
    category	ENUM('역량','인성')	NOT NULL	DEFAULT '인성',
    answer 	TEXT	NULL,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS experience_master_cl (
    id	BIGINT	NOT NULL    AUTO_INCREMENT,
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
    id	BIGINT	NOT NULL    AUTO_INCREMENT,
    user_id	BIGINT	NOT NULL,
    essential_material	TEXT	NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS study (
    id	BIGINT	NOT NULL    AUTO_INCREMENT,
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
    id	BIGINT	NOT NULL    AUTO_INCREMENT,
    user_id	BIGINT	NOT NULL,
    study_id	BIGINT	NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS users (
    id	BIGINT	NOT NULL    AUTO_INCREMENT,
    username	VARCHAR(32)	NOT NULL,
    email	VARCHAR(32)	NOT NULL	COMMENT 'UNIQUE 유저 식별 값',
    user_role	ENUM('ADMIN','NORMAL')	NOT NULL	DEFAULT 'NORMAL'	COMMENT 'NORMAL, ADMIN',
    penalty_updated_at	TIMESTAMP	NULL,
    created_at	TIMESTAMP	NOT NULL,
    deleted_at	TIMESTAMP	NULL	COMMENT 'soft delete',
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS study_schedule (
    id	BIGINT	NOT NULL    AUTO_INCREMENT,
    study_id	BIGINT	NOT NULL,
    start_date	TIMESTAMP	NULL	COMMENT '1주차 변경 불가',
    week_number	SMALLINT	NOT NULL	COMMENT '최대 duration_weeks만큼 생성 가능',
    PRIMARY KEY (id)
);

ALTER TABLE study ADD COLUMN user_id BIGINT NOT NULL COMMENT '스터디 생성자 ID';

ALTER TABLE study ADD CONSTRAINT unique_study_name UNIQUE (study_name);

ALTER TABLE study
MODIFY COLUMN study_status ENUM('RECRUITING', 'RECRUITMENT_CLOSED', 'IN_PROGRESS', 'FINISHED') NOT NULL DEFAULT 'RECRUITING';

ALTER TABLE study
MODIFY COLUMN position ENUM('NONE', 'PROGRAMMING', 'DESIGN','PLANNING', 'MARKETING', 'FINANCE', 'HR') NOT NULL DEFAULT 'NONE';

ALTER TABLE job_interview
MODIFY COLUMN category ENUM('ABILITY', 'PERSONALITY') NOT NULL DEFAULT 'PERSONALITY';

ALTER TABLE application_status
CHANGE company_link url VARCHAR(2048);

ALTER TABLE application_status
MODIFY COLUMN application_progress ENUM('NOT_STARTED', 'IN_PROGRESS', 'SUCCEED', 'FAILED') NOT NULL DEFAULT 'NOT_STARTED';

ALTER TABLE application_status
MODIFY COLUMN application_process ENUM('DOCUMENT_SCREENING', 'APTITUDE_CODING_TEST', 'FIRST_INTERVIEW', 'FINAL_INTERVIEW') NOT NULL DEFAULT 'DOCUMENT_SCREENING';

ALTER TABLE application_status
ALTER COLUMN application_progress DROP DEFAULT;

ALTER TABLE application_status
ALTER COLUMN application_process DROP DEFAULT;

ALTER TABLE experience_master_cl
MODIFY COLUMN master_cl_process ENUM('PREPARATION', 'IN_PROGRESS', 'FINALIZED') NOT NULL DEFAULT 'PREPARATION';

ALTER TABLE experience_master_cl
MODIFY COLUMN exp_anal_process ENUM('PREPARATION', 'IN_PROGRESS', 'FINALIZED') NOT NULL DEFAULT 'PREPARATION';

ALTER TABLE job_interview ADD COLUMN is_default TINYINT(1) NOT NULL DEFAULT 0;

CREATE TABLE IF NOT EXISTS white_list (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    access_ip VARCHAR(20) NOT NULL,
    active TINYINT NOT NULL DEFAULT 0,
    updated_at TIMESTAMP NULL,
    PRIMARY KEY (id)
);

ALTER TABLE application_status
MODIFY application_progress ENUM ('NOT_STARTED', 'IN_PROGRESS', 'SUCCEED', 'FAILED') NULL;

ALTER TABLE application_status
MODIFY application_process ENUM ('DOCUMENT_SCREENING', 'APTITUDE_CODING_TEST', 'FIRST_INTERVIEW', 'FINAL_INTERVIEW') NULL;

CREATE INDEX study_id_study_status__index
ON study (study_status desc, deleted_at asc, id desc);