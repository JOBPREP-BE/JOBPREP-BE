package io.dev.jobprep.domain.applicationstatus.infrastructure;

import io.dev.jobprep.domain.applicationstatus.domain.entity.ApplicationStatus;
import java.util.List;

public interface ApplicationStatusRepositoryCustom {

    List<ApplicationStatus> findByConditionWithPagination(
        Long userId, Long cursorId, int pageSize
    );
}
