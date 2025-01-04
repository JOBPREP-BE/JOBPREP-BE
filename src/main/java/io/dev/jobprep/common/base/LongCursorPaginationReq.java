package io.dev.jobprep.common.base;

import jakarta.annotation.Nullable;
import lombok.Getter;
import org.springframework.data.domain.Sort.Direction;

@Getter
public class LongCursorPaginationReq extends BaseCursorPaginationReq {

    private final Long cursorId;

    public LongCursorPaginationReq(@Nullable Long cursorId, int pageSize, Direction sortOrder) {
        super(pageSize, sortOrder);
        this.cursorId = cursorId;
    }
}
