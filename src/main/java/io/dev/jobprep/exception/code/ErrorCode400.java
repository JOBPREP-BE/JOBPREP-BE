package io.dev.jobprep.exception.code;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode400 implements ErrorCode {
    PATH_PARAMETER_BAD_REQUEST("E00-COMMON-001", "잘못된 경로 파라미터입니다."),
    INVALID_INPUT_VALUE("E00-COMMON-002", "기본 유효성 검사에 실패하였습니다."),

    INVALID_STUDY_STATUS_TO_RECRUIT("E01-STUDY-001", "스터디가 현재 모집중이 아닙니다."),
    STUDY_WEEK_NUMBER_EXCEED("E01-STUDY-002", "스터디는 3주 이상 진행할 수 없습니다."),
    ALREADY_CREATED_STUDY("E01-STUDY-003", "스터디는 한 번에 하나만 생성할 수 있습니다."),
    ALREADY_FINISHED_STUDY("E01-STUDY-004", "해당 스터디는 이미 종료되었습니다."),
    ALREADY_DELETED_STUDY("E01-STUDY-005", "해당 스터디는 이미 삭제되었습니다."),
    STUDY_GATHERED_USER_EXCEED("E01-STUDY-006", "해당 스터디의 모집 인원이 모두 채워졌습니다."),
    ALREADY_GATHERED_STUDY("E01-STUDY-007", "이미 참여 중인 스터디가 있습니다."),
    ALREADY_PASSED_DUE_DATE("E01-STUDY-008", "스터디의 모집 기간이 지났습니다."),
    ;

    private final HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
    private final String code;
    private final String message;

    ErrorCode400(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
