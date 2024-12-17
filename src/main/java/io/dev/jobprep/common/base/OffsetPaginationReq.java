package io.dev.jobprep.common.base;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class OffsetPaginationReq {

    private final Integer page;

    @Min(1)
    private final Integer pageGroupSize;

    @Min(1)
    private final Integer pageSize;

    public OffsetPaginationReq(Integer page, @NotNull Integer pageGroupSize, @NotNull Integer pageSize) {
        this.page = page;
        this.pageGroupSize = pageGroupSize;
        this.pageSize = pageSize;
    }

}
