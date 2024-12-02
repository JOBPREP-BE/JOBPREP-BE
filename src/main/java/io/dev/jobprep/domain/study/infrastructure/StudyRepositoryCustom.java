package io.dev.jobprep.domain.study.infrastructure;

import io.dev.jobprep.domain.study.application.dto.res.StudyWithStartDateDto;
import java.util.Optional;

public interface StudyRepositoryCustom {

    Optional<StudyWithStartDateDto> getStudyWithStartDate(Long studyId);
}
