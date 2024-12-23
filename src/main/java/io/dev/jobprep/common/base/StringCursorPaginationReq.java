package io.dev.jobprep.common.base;

import jakarta.annotation.Nullable;
import lombok.Getter;
import org.springframework.data.domain.Sort.Direction;

@Getter
public class StringCursorPaginationReq extends BaseCursorPaginationReq {

    private final String cursorId;

    public StringCursorPaginationReq(@Nullable String cursorId, int pageSize, Direction sortOrder) {
        super(pageSize, sortOrder);
        this.cursorId = cursorId;
    }
}
