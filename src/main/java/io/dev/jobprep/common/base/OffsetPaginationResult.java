package io.dev.jobprep.common.base;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.Getter;

@Schema(description = "오프셋 기반 페이지네이션 결과에 대한 일괄 응답")
@Getter
public class OffsetPaginationResult<T> {

    private static final Integer MIN = 1;

    @Schema(description = "데이터 목록")
    private final List<T> data;

    @Schema(description = "반환된 페이지의 전체 데이터 수", example = "30", implementation = Integer.class)
    private final Integer numberOfElements;

    @Schema(description = "요청한 데이터 수", example = "6", implementation = Integer.class)
    private final Integer size;

    @Schema(description = "반환된 페이지 그룹 수", example = "5", implementation = Integer.class)
    private final Integer groupSize;

    @Schema(description = "반환된 페이지 그룹의 첫 번째 페이지 번호", example = "1", implementation = Integer.class)
    private final Integer currentPage;

    @Schema(description = "전체 데이터 수")
    private final Long totalNumberOfElements;

    @Schema(description = "전체 페이지 수")
    private final Integer totalPages;

    private OffsetPaginationResult(
        List<T> data,
        Integer size,
        Integer currentPage,
        Long totalNumberOfElements
    ) {
        this.data = data;
        this.numberOfElements = data.size();
        this.size = size;
        this.groupSize = calculateGroupSizeForElements(numberOfElements, size);
        this.currentPage = currentPage;
        this.totalNumberOfElements = totalNumberOfElements;
        this.totalPages = (int) Math.ceil((double) totalNumberOfElements / size);
    }

    private Integer calculateGroupSizeForElements(Integer numberOfElements, Integer size) {
        if (size != 0) {
            return (numberOfElements / size) + (numberOfElements % size) != 0 ? 1 : 0;
        }
        return MIN;
    }

    public static <T> OffsetPaginationResult<T> fromDataWithOffsetPageInfo(
        List<T> data, Integer size, Integer currentPage, Long totalNumberOfElements
    ) {
        return new OffsetPaginationResult<>(data, size, currentPage, totalNumberOfElements);
    }
}
