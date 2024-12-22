package io.dev.jobprep.domain.experience_master_cl.infrastructure;

import io.dev.jobprep.domain.experience_master_cl.domain.ExpMasterCl;

import java.util.List;

public interface ExpMasterClRepositoryCustom {
    List<ExpMasterCl> findByConditionWithPagination(
            Long userId, Long cursorId, int pageSize
    );
}
