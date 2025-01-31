package io.dev.jobprep.common.base;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Min;
import java.util.Objects;
import lombok.Getter;
import org.springframework.data.domain.Sort.Direction;

@Getter
public abstract class BaseCursorPaginationReq {

    @Min(1)
    private final int pageSize;

    private final Direction sortOrder;

    public BaseCursorPaginationReq(int pageSize, @Nullable Direction sortOrder) {
        this.pageSize = pageSize;
        this.sortOrder = Objects.requireNonNullElse(sortOrder, Direction.DESC);
    }

}
