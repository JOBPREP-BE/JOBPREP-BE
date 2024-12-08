package io.dev.jobprep.domain.experience_master_cl.domain.enums;

import io.dev.jobprep.domain.experience_master_cl.exception.ExpMasterClException;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

import static io.dev.jobprep.exception.code.ErrorCode400.INVALID_INPUT_VALUE;


@Getter
@AllArgsConstructor
public enum MasterClProcess {
    PREPARATION("진행 전"),
    IN_PROGRESS("진행 중"),
    FINALIZED("완료");

    private final String message;

    public static MasterClProcess from(String value) {
        for (MasterClProcess process : MasterClProcess.values()) {
            if (Objects.equals(process.getMessage(), value)) {
                return process;
            }
        }
        throw new ExpMasterClException(INVALID_INPUT_VALUE);
    }
}
