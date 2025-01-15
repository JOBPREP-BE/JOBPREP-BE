package io.dev.jobprep.domain.study.infrastructure;

import io.dev.jobprep.domain.study.application.dto.res.StudyWithStartDateDto;
import io.dev.jobprep.domain.study.domain.entity.Study;
import java.util.List;
import java.util.Optional;

public interface StudyRepositoryCustom {

    Optional<StudyWithStartDateDto> getStudyWithStartDate(Long studyId);

    List<Study> findNonDeletedStudyWithPagination(Long cursorId, int pageSize);

    List<Study> findRecruitingStudyWithPagination(int page, int pageSize, int pageGroupSize);
}
