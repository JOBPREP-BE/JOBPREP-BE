package io.dev.jobprep.exception.code;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode400 implements ErrorCode {
    PATH_PARAMETER_BAD_REQUEST("E00-COMMON-001", "잘못된 경로 파라미터입니다."),
    INVALID_INPUT_VALUE("E00-COMMON-002", "기본 유효성 검사에 실패하였습니다."),
    ILLEGAL_INPUT_ARG("E00-COMMON-03", "유효하지 않은 입력 값입니다."),

    INVALID_STUDY_STATUS_TO_RECRUIT("E01-STUDY-001", "스터디가 현재 모집중이 아닙니다."),
    STUDY_WEEK_NUMBER_EXCEED("E01-STUDY-002", "스터디는 3주 이상 진행할 수 없습니다."),
    ALREADY_CREATED_STUDY("E01-STUDY-003", "스터디는 한 번에 하나만 생성할 수 있습니다."),
    ALREADY_FINISHED_STUDY("E01-STUDY-004", "해당 스터디는 이미 종료되었습니다."),
    ALREADY_DELETED_STUDY("E01-STUDY-005", "해당 스터디는 이미 삭제되었습니다."),
    STUDY_GATHERED_USER_EXCEED("E01-STUDY-006", "해당 스터디의 모집 인원이 모두 채워졌습니다."),
    ALREADY_GATHERED_STUDY("E01-STUDY-007", "이미 참여 중인 스터디가 있습니다."),
    ALREADY_PASSED_DUE_DATE("E01-STUDY-008", "스터디의 모집 기간이 지났습니다."),
    INVALID_POSITION_ARG("E01-STUDY-009", "스터디 직무 입력이 잘못되었습니다."),
    INVALID_STATUS_ARG("E01-STUDY-010", "스터디 상태가 잘못되었습니다."),
    NON_GATHERED_USER("E01-STUDY-011", "해당 스터디에 유저가 존재하지 않습니다."),
    DUPLICATE_STUDY_NAME("E00-STUDY-01", "해당 스터디 이름이 이미 존재합니다."),
    INVALID_START_DATE("E00-STUDY-002", "스터디 시작 시간은 미래 시간이어야 합니다."),

    USER_ACCOUNT_ALREADY_EXISTS("E01-USER-001", "해당 이메일로 가입된 계정이 이미 존재합니다."),
    ALREADY_PENALIZED_USER("E01-USER-002", "이미 페널티가 부여된 유저입니다."),

    ALREADY_DELETED_INTERVIEW("E01-JOB-INTERVIEW-001", "해당 면접 데이터는 이미 삭제되었습니다."),
    INVALID_INTERVIEW_STATUS("E01-JOB-INTERVIEW-002", "해당 면접 카테고리가 잘못되었습니다."),

    INVALID_APPLICATION_PROGRESS_ARG("E01-APPLICATIONSTATUS-001", "지원 상황 입력이 잘못되었습니다."),
    INVALID_APPLICATION_PROCESS_ARG("E01-APPLICATIONSTATUS-002", "전형 단계 입력이 잘못되었습니다."),

    ALREADY_DELETED_MASTER_CL("E01-EXP-MASTER-001", "해당 자소서는 이미 삭제되었습니다."),
    INVALID_PROCESS_MASTER_CL("E01-EXP-MASTER-002", "해당 자소서는 알맞은 과정이 아닙니다."),
    INVALID_FIELD_MASTER_CL("E01-EXP-MASTER-003", "해당 필드는 마스터 자소서에 없습니다."),

    CHAT_ROOM_DISABLED("E01-CHATROOM-001", "해당 채팅방은 비활성화되었습니다."),
    NON_GATHERED_CHAT_USER("E01-CHATROOM-002", "해당 채팅방의 참여자가 아닙니다."),
    CHAT_ROOM_ALREADY_EXIST("E01-CHATROOM-003", "이미 생성된 채팅방이 있습니다."),
    ;

    private final HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
    private final String code;
    private final String message;

    ErrorCode400(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
