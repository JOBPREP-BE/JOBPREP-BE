package io.dev.jobprep.domain.study.domain.entity.enums;

import static io.dev.jobprep.exception.code.ErrorCode400.INVALID_POSITION_ARG;

import io.dev.jobprep.domain.study.exception.StudyException;
import java.util.Arrays;
import lombok.Getter;

@Getter
public enum Position {

    NONE("무관"),
    PROGRAMMING("개발"),
    DESIGN("디자인"),
    PLANNING("기획"),
    MARKETING("마케팅"),
    FINANCE("재무/회계"),
    HR("인사"),
    ;

    private final String description;

    Position(String description) {
        this.description = description;
    }

    public static Position from(String description) {
        return Arrays.stream(Position.values())
            .filter(pos -> pos.description.equals(description))
            .findFirst()
            .orElseThrow(() -> new StudyException(INVALID_POSITION_ARG));
    }
}
