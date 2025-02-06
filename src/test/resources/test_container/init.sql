create table if not exists `jobprep-mysql-test`.application_status
(
    id                   bigint auto_increment
    primary key,
    user_id              bigint                                                                                    not null,
    company              varchar(32)                                                                               null,
    position             varchar(32)                                                                               null,
    application_progress enum ('NOT_STARTED', 'IN_PROGRESS', 'SUCCEED', 'FAILED')                                  null,
    application_process  enum ('DOCUMENT_SCREENING', 'APTITUDE_CODING_TEST', 'FIRST_INTERVIEW', 'FINAL_INTERVIEW') null,
    application_date     timestamp                                                                                 null,
    due_date             timestamp                                                                                 null,
    url                  varchar(2048)                                                                             null,
    cover_letter         text                                                                                      null comment '작성 용도가 아닌, 저장 용도'
    );

create table if not exists `jobprep-mysql-test`.essential_material
(
    id                 bigint auto_increment
    primary key,
    user_id            bigint not null,
    essential_material text   not null
);

create table if not exists `jobprep-mysql-test`.experience_master_cl
(
    id                bigint auto_increment
    primary key,
    user_id           bigint                                                                 not null,
    material          varchar(100)                                                           null,
    emphasis          varchar(100)                                                           null,
    exp_anal_process  enum ('PREPARATION', 'IN_PROGRESS', 'FINALIZED') default 'PREPARATION' not null,
    master_cl_process enum ('PREPARATION', 'IN_PROGRESS', 'FINALIZED') default 'PREPARATION' not null,
    exp_anal          text                                                                   null,
    master_cl         text                                                                   null,
    active            tinyint(1)                                       default 1             not null comment '삭제해도 끝까지 가지고 있기'
    );

create table if not exists `jobprep-mysql-test`.job_interview
(
    id         bigint auto_increment
    primary key,
    user_id    bigint                                                not null,
    question   varchar(255)                                          null,
    category   enum ('ABILITY', 'PERSONALITY') default 'PERSONALITY' not null,
    answer     text                                                  null,
    is_default tinyint(1)                      default 0             not null
    );

create table if not exists `jobprep-mysql-test`.study
(
    id             bigint auto_increment
    primary key,
    study_name     varchar(20)                                                                                           not null,
    position       enum ('NONE', 'PROGRAMMING', 'DESIGN', 'PLANNING', 'MARKETING', 'FINANCE', 'HR') default 'NONE'       not null,
    study_status   enum ('RECRUITING', 'RECRUITMENT_CLOSED', 'IN_PROGRESS', 'FINISHED')             default 'RECRUITING' not null,
    head_count     smallint                                                                         default 3            not null,
    duration_weeks smallint                                                                         default 3            not null,
    google_link    varchar(200)                                                                                          null,
    discord_link   varchar(200)                                                                                          null,
    kakao_link     varchar(200)                                                                                          null,
    created_at     timestamp                                                                                             not null,
    deleted_at     timestamp                                                                                             null comment 'soft delete',
    constraint unique_study_name
    unique (study_name)
    );

create index study_id_study_status__index
    on `jobprep-mysql-test`.study (study_status desc, deleted_at asc, id desc);

create table if not exists `jobprep-mysql-test`.study_schedule
(
    id          bigint auto_increment
    primary key,
    study_id    bigint    not null,
    start_date  timestamp null comment '1주차 변경 불가',
    week_number smallint  not null comment '최대 duration_weeks만큼 생성 가능'
);

create table if not exists `jobprep-mysql-test`.user_study
(
    id       bigint auto_increment
    primary key,
    user_id  bigint not null,
    study_id bigint not null
);

create table if not exists `jobprep-mysql-test`.users
(
    id                 bigint auto_increment
    primary key,
    username           varchar(32)                                              not null,
    email              varchar(32)                                              not null comment 'UNIQUE 유저 식별 값',
    penalty_updated_at timestamp                                                null,
    created_at         timestamp                                                not null,
    deleted_at         timestamp                                                null comment 'soft delete',
    user_role          enum ('ROLE_ADMIN', 'ROLE_NORMAL') default 'ROLE_NORMAL' null
    );

create table if not exists `jobprep-mysql-test`.white_list
(
    id         bigint auto_increment
    primary key,
    access_ip  varchar(20)       not null,
    active     tinyint default 0 not null,
    updated_at timestamp         null
    );

INSERT INTO users (username, email, user_role, penalty_updated_at, created_at, deleted_at) VALUES
   ('testUser1', 'test1@example.com', 'ROLE_NORMAL', null, current_timestamp, null);