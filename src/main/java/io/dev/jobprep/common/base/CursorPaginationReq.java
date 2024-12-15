package io.dev.jobprep.common.base;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Min;
import java.util.Objects;
import lombok.Getter;
import org.springframework.data.domain.Sort.Direction;

@Getter
public class CursorPaginationReq<T> {

    private final T cursorId;

    @Min(1)
    private final int pageSize;

    private final Direction sortOrder;

    public CursorPaginationReq(@Nullable T cursorId, int pageSize, @Nullable Direction sortOrder) {
        this.cursorId = cursorId;
        this.pageSize = pageSize;
        this.sortOrder = Objects.requireNonNullElse(sortOrder, Direction.DESC);
    }

}